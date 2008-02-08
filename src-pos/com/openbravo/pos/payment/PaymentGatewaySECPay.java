// Modifications by Adrian Romero

package com.openbravo.pos.payment;

import java.util.*;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;

import com.openbravo.pos.forms.*;

import java.security.Security;
import javax.xml.namespace.QName;
import java.rmi.RemoteException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.rpc.ServiceException;
import com.openbravo.pos.ticket.TicketInfo;

public class PaymentGatewaySECPay implements PaymentGateway {
    
    private static final String ENDPOINTADDRESS = "https://www.secpay.com/java-bin/soap";
    private static final QName OPERATIONVALIDATE = new QName("SECCardService", "validateCardFull");
    private static final QName OPERATIONREFUND = new QName("SECCardService", "refundCardFull");
    
    private String m_sCommerceID;
    private String m_sCommercePassword;
    private String m_sCurrency;
    private boolean m_bTestMode;
    
    /** Creates a new instance of PaymentGatewaySECPay */
    public PaymentGatewaySECPay(AppProperties props) {
        
        // Propiedades del sistema
        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol" );
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            
        // Configuracion del pago
        m_sCommerceID = props.getProperty("payment.commerceid");
        m_sCommercePassword = props.getProperty("payment.commercepassword");
        m_bTestMode = Boolean.valueOf(props.getProperty("payment.testmode")).booleanValue();
        m_sCurrency = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
    }
    
    public void execute(PaymentInfoMagcard payinfo) {

        // dependiendo del total payinfo debe ser un pago o una devolucion...
        // por ahora solo se realizan pagos...
        
        // Se podria comprobar la instancia de payinfo,
        // PaymentInfoMagcard o PaymentInfoMagcardRefund
        if (payinfo.getTotal() > 0.0) {
            try {
                Service service = new Service();
                Call call = (Call) service.createCall();
                call.setTargetEndpointAddress(ENDPOINTADDRESS);
                call.setOperationName(OPERATIONVALIDATE);            
                Object[] methodParams = {
                    m_sCommerceID,
                    m_sCommercePassword,
                    payinfo.getTransactionID(),
                    "127.0.0.1",
                    payinfo.getHolderName(),
                    payinfo.getCardNumber(),
                    Double.toString(payinfo.getTotal()),
                    payinfo.getExpirationDate(),
                    "", // issue number
                    "", // start date
                    "", // order
                    "", // shipping
                    "", // billing
                    (m_bTestMode ? "test_status=true," : "") + "dups=false,currency=" + m_sCurrency //
                };
    //            Object[] methodParams = {
    //              "secpay",
    //              "secpay",
    //              "Twem0003",
    //              "127.0.0.1",
    //              "Mr Cardholder",
    //              "4444333322221111",
    //              "50.00",
    //              "0909",
    //              "",
    //              "",
    //              "",
    //              "",
    //              "",
    //              "test_status=true,dups=false,currency=EUR"
    //            };

                String returned = (String) call.invoke(methodParams);
                // "?valid=true&trans_id=Twem0003&code=A&auth_code=9999&message=TEST AUTH&currency=EUR&amount=50.0&test_status=true"       

                if (returned == null) {
                    payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nResponse empty.");
                } else {
                    Map props = new HashMap();
                    StringTokenizer tk = new java.util.StringTokenizer(returned, "?&");
                    while(tk.hasMoreTokens()) {
                        String sToken = tk.nextToken();
                        int i = sToken.indexOf('=');
                        if (i >= 0) {
                            props.put(URLDecoder.decode(sToken.substring(0, i), "UTF-8"), URLDecoder.decode(sToken.substring(i + 1), "UTF-8"));
                        } else {
                            props.put(URLDecoder.decode(sToken, "UTF-8"), null);
                        }                   
                    }

                    if ("true".equals(props.get("valid"))) {
                        // A  Transaction authorised by bank. auth_code available as bank reference
                        payinfo.paymentOK((String) props.get("auth_code"));
                    } else {
                        String sCode = (String) props.get("code");
                        if ("N".equals(sCode)) {
                            // N Transaction not authorised. Failure message text available to merchant
                            payinfo.paymentError(AppLocal.getIntString("message.paymentnotauthorised") + "\n" + (String) props.get("message"));
                        } else if ("C".equals(sCode)) {
                            // C Communication problem. Trying again later may well work
                            payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nCommunication problem. Trying again later may well work.");
                        } else if ("P:A".equals(sCode)) {
                            // P:A Pre-bank checks. Amount not supplied or invalid
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nAmount not supplied or invalid.");
                       } else if ("P:X".equals(sCode)) {
                            // P:X Pre-bank checks. Not all mandatory parameters supplied
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nNot all mandatory parameters supplied.");
                        } else if ("P:P".equals(sCode)) {
                            // P:P Pre-bank checks. Same payment presented twice
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nSame payment presented twice.");
                        } else if ("P:S".equals(sCode)) {
                            // P:S Pre-bank checks. Start date invalid
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nStart date invalid.");
                        } else if ("P:E".equals(sCode)) {
                            // P:E Pre-bank checks. Expiry date invalid
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nExpiry date invalid.");
                        } else if ("P:I".equals(sCode)) {
                            // P:I Pre-bank checks. Issue number invalid
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nIssue number invalid.");
                        } else if ("P:C".equals(sCode)) {
                            // P:C Pre-bank checks. Card number fails LUHN check
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nCard number fails LUHN check.");
                        } else if ("P:T".equals(sCode)) {
                            // P:T Pre-bank checks. Card type invalid - i.e. does not match card number prefix
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nCard type invalid - i.e. does not match card number prefix.");
                        } else if ("P:N".equals(sCode)) {
                            // P:N Pre-bank checks. Customer name not supplied
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nCustomer name not supplied.");
                        } else if ("P:M".equals(sCode)) {
                            // P:M Pre-bank checks. Merchant does not exist or not registered yet
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nMerchant does not exist or not registered yet.");
                        } else if ("P:B".equals(sCode)) {
                            // P:B Pre-bank checks. Merchant account for card type does not exist
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nMerchant account for card type does not exist.");
                        } else if ("P:D".equals(sCode)) {
                            // P:D Pre-bank checks. Merchant account for this currency does not exist
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nMerchant account for this currency does not exist.");
                        } else if ("P:V".equals(sCode)) {
                            // P:V Pre-bank checks. CVV2 security code mandatory and not supplied / invalid
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nCVV2 security code mandatory and not supplied / invalid.");
                        } else if ("P:R".equals(sCode)) {
                            // P:R Pre-bank checks. Transaction timed out awaiting a virtual circuit. Merchant may not have enough virtual circuits for the volume of business.
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nTransaction timed out awaiting a virtual circuit. Merchant may not have enough virtual circuits for the volume of business.");
                        } else if ("P:#".equals(sCode)) {
                            // P:# Pre-bank checks. No MD5 hash / token key set up against account 
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\nNo MD5 hash / token key set up against account.");
                        } else {
                             payinfo.paymentError(AppLocal.getIntString("message.paymenterrorunknown"));
                        }
                    }
                }
            } catch (UnsupportedEncodingException eUE) {
                // no pasa nunca
                payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice") + "\n" + eUE.getMessage());
            } catch (ServiceException serviceException) {
                payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice") + "\n" + serviceException.getMessage());
            } catch (RemoteException remoteException) {
                payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionremote") + "\n" + remoteException.getMessage());

            }
        } else {
            // devoluciones no soportadas actualmente
            payinfo.paymentError(AppLocal.getIntString("message.paymentrefundsnotsupported"));
        }
    }   
}
