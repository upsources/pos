/**
 * <p>Title: AIM Java Version 1.4.1_02-b06</p>
 * <p>Description: Advanced Integration Method</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Authorize.Net</p>
 * @author Authorize.Net
 * @version 3.1
 */

/**
 *  Based on sample code and snipptes provided by:
 *  Patrick Phelan, phelan@choicelogic.com
 *  Roedy Green, Canadian Mind Products
 */

// Modifications by Adrian Romero

package com.openbravo.pos.payment;

import java.io.*;
import java.net.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import com.openbravo.pos.forms.*;

public class PaymentGatewayAuthorizeNet implements PaymentGateway {
    
    private static final String ENDPOINTADDRESS = "https://secure.authorize.net/gateway/transact.dll";
    private static final String OPERATIONVALIDATE = "AUTH_CAPTURE";
    private static final String OPERATIONREFUND = "CREDIT";
    
    private String m_sCommerceID;
    private String m_sCommercePassword;
    private boolean m_bTestMode;

    /** Creates a new instance of PaymentGatewayAuthorizeNet */
    public PaymentGatewayAuthorizeNet(AppProperties props) {
        // Grab some configuration variables
        m_sCommerceID = props.getProperty("payment.commerceid");
        m_sCommercePassword = props.getProperty("payment.commercepassword");
        m_bTestMode = Boolean.valueOf(props.getProperty("payment.testmode")).booleanValue();
    }  

    public void execute(PaymentInfoMagcard payinfo) {
        
        // dependiendo del total payinfo debe ser un pago o una devolucion...
        // por ahora solo se realizan pagos...
        
        // Se podria comprobar la instancia de payinfo,
        // PaymentInfoMagcard o PaymentInfoMagcardRefund        
        if (payinfo.getTotal() > 0.0) {
            try {
                StringBuffer sb = new StringBuffer();

                sb.append("x_login=");        
                sb.append(URLEncoder.encode(m_sCommerceID, "UTF-8"));
                
                sb.append("&x_password=");
                sb.append(URLEncoder.encode(m_sCommercePassword, "UTF-8"));
                
                //sb.append("x_tran_key=ssssss");     // reemplazr
                
                sb.append("&x_version=3.1");
                
                sb.append("&x_test_request=");  
                sb.append(m_bTestMode);
                
                sb.append("&x_method=CC");
                
                sb.append("&x_type=");
                sb.append(OPERATIONVALIDATE);
                
                sb.append("&x_amount=");
                NumberFormat formatter = new DecimalFormat("000.00");
                String amount = formatter.format(payinfo.getTotal());
                sb.append(URLEncoder.encode((String)amount, "UTF-8"));

                sb.append("&x_delim_data=TRUE");
                sb.append("&x_delim_char=|");
                sb.append("&x_relay_response=FALSE");

                // CC information
                sb.append("&x_exp_date=");
                String tmp = payinfo.getExpirationDate();
                sb.append(tmp.charAt(2));
                sb.append(tmp.charAt(3));
                sb.append(tmp.charAt(0));
                sb.append(tmp.charAt(1));
                
                sb.append("&x_card_num=");
                sb.append(URLEncoder.encode(payinfo.getCardNumber(), "UTF-8"));

                // no requerido
                sb.append("&x_description=Shop+Transaction");
                
                String[] cc_name = payinfo.getHolderName().split(" ");
                sb.append("&x_first_name=");
                if (cc_name.length > 0) {
                    sb.append(URLEncoder.encode(cc_name[0], "UTF-8"));
                }
                sb.append("&x_last_name=");
                if (cc_name.length > 1) {
                    sb.append(URLEncoder.encode(cc_name[1], "UTF-8"));
                }

                // open secure connection
                URL url = new URL(ENDPOINTADDRESS);

                URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                // not necessarily required but fixes a bug with some servers
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                // POST the data in the string buffer
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.write(sb.toString().getBytes());
                out.flush();
                out.close();

                // process and read the gateway response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                line = in.readLine();
                in.close();	                     // fin


                String[] ccRep = line.split("\\|");
                
                if ("1".equals(ccRep[0])) {
                    payinfo.paymentOK((String) ccRep[4]); 
                } else { 
                    payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\n" + ccRep[0] + " -- " + ccRep[3]);
                }

            } catch (UnsupportedEncodingException eUE) {
                // no pasa nunca
                payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice") + "\n" + eUE.getMessage());
            } catch (MalformedURLException eMURL) {
                // no pasa nunca    
                payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice") + "\n" + eMURL.getMessage());
            } catch(IOException e){
                payinfo.paymentError(AppLocal.getIntString("message.paymenterror") + "\n" + e.getMessage());
            }
        } else {
            // devoluciones no soportadas actualmente
            payinfo.paymentError(AppLocal.getIntString("message.paymentrefundsnotsupported"));
        }
    }

}