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

// Modifications by Adrian Romero & Mikel Irurita

package com.openbravo.pos.payment;

import java.io.*;
import java.net.*;
import java.text.NumberFormat;
import java.text.DecimalFormat;

import com.openbravo.pos.forms.*;
import com.openbravo.pos.util.AltEncrypter;

public class PaymentGatewayAuthorizeNet implements PaymentGateway {
    
    private static String ENDPOINTADDRESS;
    private static final String OPERATIONVALIDATE = "AUTH_CAPTURE";
    private static final String OPERATIONREFUND = "CREDIT";
    private static final String OK = "1";
    
    private String m_sCommerceID;
    private String m_sCommercePassword;
    private boolean m_bTestMode;

    /** Creates a new instance of PaymentGatewayAuthorizeNet */
    public PaymentGatewayAuthorizeNet(AppProperties props) {
        // Grab some configuration variables
        m_sCommerceID = props.getProperty("payment.commerceid");
        
        AltEncrypter cypher = new AltEncrypter("cypherkey" + props.getProperty("payment.commerceid"));
        this.m_sCommercePassword = cypher.decrypt(props.getProperty("payment.commercepassword").substring(6));

        m_bTestMode = Boolean.valueOf(props.getProperty("payment.testmode")).booleanValue();
        
        ENDPOINTADDRESS = (m_bTestMode) 
                ? "https://test.authorize.net/gateway/transact.dll"
                : "https://secure.authorize.net/gateway/transact.dll";
    }  
    
    public PaymentGatewayAuthorizeNet() {
        
    }

    @Override
    public void execute(PaymentInfoMagcard payinfo) {

        StringBuffer sb = new StringBuffer();
        try {
            //test -> login:7q6Lu8X7 / pass:9YM4aUP5Ej77a854
            sb.append("x_login=");        
            sb.append(URLEncoder.encode(m_sCommerceID, "UTF-8"));

            sb.append("&x_tran_key=");
            sb.append(URLEncoder.encode(m_sCommercePassword, "UTF-8"));
             
            sb.append("&x_amount=");
            NumberFormat formatter = new DecimalFormat("0000.00");
            String amount = formatter.format(Math.abs(payinfo.getTotal()));
            sb.append(URLEncoder.encode(amount.replace(',', '.'), "UTF-8"));
             
            sb.append("&x_card_num=");
            sb.append(URLEncoder.encode(payinfo.getCardNumber(), "UTF-8"));
                
            sb.append("&x_exp_date=");
            String tmp = payinfo.getExpirationDate();
            sb.append(URLEncoder.encode(tmp, "UTF-8"));
             
            String[] cc_name = payinfo.getHolderName().split(" ");
            sb.append("&x_first_name=");
            if (cc_name.length > 0) {
               sb.append(URLEncoder.encode(cc_name[0], "UTF-8"));
            }
            sb.append("&x_last_name=");
            if (cc_name.length > 1) {
               sb.append(URLEncoder.encode(cc_name[1], "UTF-8"));
            }
            
            sb.append("&x_method=CC");
            sb.append("&x_version=3.1");
            sb.append("&x_delim_data=TRUE");
            sb.append("&x_delim_char=|");
            sb.append("&x_relay_response=FALSE");
            sb.append("&x_test_request=");
            sb.append(m_bTestMode);
            
            //PAYMENT
            if (payinfo.getTotal() >= 0.0) {
                sb.append("&x_type=");
                sb.append(OPERATIONVALIDATE);
                //sb.append("&x_card_code=340"); //CCV
            }
            //REFUND
            else {
                sb.append("&x_type=");
                sb.append(OPERATIONREFUND);
                sb.append("&x_trans_id=");
                sb.append(payinfo.getTransactionID());
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
            String returned = in.readLine();
            
            payinfo.setReturnMessage(returned);
            in.close();	                     // fin

            String[] ccRep = returned.split("\\|");
            
            if (OK.equals(ccRep[0])) {
                //ccRep[4] = authorization, ccRep[6]=transactionId
                //In test mode transactionID always is 0
                payinfo.paymentOK((String) ccRep[4], (String)ccRep[6], returned);
            } else { 
                payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), ccRep[0] + " -- " + ccRep[3]);
            }
        } catch (UnsupportedEncodingException eUE) {
            // no pasa nunca
            payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice"), eUE.getMessage());
        } catch (MalformedURLException eMURL) {
            // no pasa nunca    
            payinfo.paymentError(AppLocal.getIntString("message.paymentexceptionservice"), eMURL.getMessage());
        } catch(IOException e){
            payinfo.paymentError(AppLocal.getIntString("message.paymenterror"), e.getMessage());
        }
        
    }

}