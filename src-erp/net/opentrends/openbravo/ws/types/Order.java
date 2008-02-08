/**
 * Order.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package net.opentrends.openbravo.ws.types;

public class Order  implements java.io.Serializable {
    private net.opentrends.openbravo.ws.types.BPartner businessPartner;

    private net.opentrends.openbravo.ws.types.OrderLine[] lines;

    private net.opentrends.openbravo.ws.types.OrderIdentifier orderId;

    private net.opentrends.openbravo.ws.types.Payment[] payment;

    private int state;

    public Order() {
    }

    public Order(
           net.opentrends.openbravo.ws.types.BPartner businessPartner,
           net.opentrends.openbravo.ws.types.OrderLine[] lines,
           net.opentrends.openbravo.ws.types.OrderIdentifier orderId,
           net.opentrends.openbravo.ws.types.Payment[] payment,
           int state) {
           this.businessPartner = businessPartner;
           this.lines = lines;
           this.orderId = orderId;
           this.payment = payment;
           this.state = state;
    }


    /**
     * Gets the businessPartner value for this Order.
     * 
     * @return businessPartner
     */
    public net.opentrends.openbravo.ws.types.BPartner getBusinessPartner() {
        return businessPartner;
    }


    /**
     * Sets the businessPartner value for this Order.
     * 
     * @param businessPartner
     */
    public void setBusinessPartner(net.opentrends.openbravo.ws.types.BPartner businessPartner) {
        this.businessPartner = businessPartner;
    }


    /**
     * Gets the lines value for this Order.
     * 
     * @return lines
     */
    public net.opentrends.openbravo.ws.types.OrderLine[] getLines() {
        return lines;
    }


    /**
     * Sets the lines value for this Order.
     * 
     * @param lines
     */
    public void setLines(net.opentrends.openbravo.ws.types.OrderLine[] lines) {
        this.lines = lines;
    }


    /**
     * Gets the orderId value for this Order.
     * 
     * @return orderId
     */
    public net.opentrends.openbravo.ws.types.OrderIdentifier getOrderId() {
        return orderId;
    }


    /**
     * Sets the orderId value for this Order.
     * 
     * @param orderId
     */
    public void setOrderId(net.opentrends.openbravo.ws.types.OrderIdentifier orderId) {
        this.orderId = orderId;
    }


    /**
     * Gets the payment value for this Order.
     * 
     * @return payment
     */
    public net.opentrends.openbravo.ws.types.Payment[] getPayment() {
        return payment;
    }


    /**
     * Sets the payment value for this Order.
     * 
     * @param payment
     */
    public void setPayment(net.opentrends.openbravo.ws.types.Payment[] payment) {
        this.payment = payment;
    }


    /**
     * Gets the state value for this Order.
     * 
     * @return state
     */
    public int getState() {
        return state;
    }


    /**
     * Sets the state value for this Order.
     * 
     * @param state
     */
    public void setState(int state) {
        this.state = state;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Order)) return false;
        Order other = (Order) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.businessPartner==null && other.getBusinessPartner()==null) || 
             (this.businessPartner!=null &&
              this.businessPartner.equals(other.getBusinessPartner()))) &&
            ((this.lines==null && other.getLines()==null) || 
             (this.lines!=null &&
              java.util.Arrays.equals(this.lines, other.getLines()))) &&
            ((this.orderId==null && other.getOrderId()==null) || 
             (this.orderId!=null &&
              this.orderId.equals(other.getOrderId()))) &&
            ((this.payment==null && other.getPayment()==null) || 
             (this.payment!=null &&
              java.util.Arrays.equals(this.payment, other.getPayment()))) &&
            this.state == other.getState();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getBusinessPartner() != null) {
            _hashCode += getBusinessPartner().hashCode();
        }
        if (getLines() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLines());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLines(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOrderId() != null) {
            _hashCode += getOrderId().hashCode();
        }
        if (getPayment() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPayment());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPayment(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getState();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Order.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://192.168.1.130:8880/openbravo/services/ExternalSales", "Order"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("businessPartner");
        elemField.setXmlName(new javax.xml.namespace.QName("", "businessPartner"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://192.168.1.130:8880/openbravo/services/ExternalSales", "BPartner"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lines");
        elemField.setXmlName(new javax.xml.namespace.QName("", "lines"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://192.168.1.130:8880/openbravo/services/ExternalSales", "OrderLine"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "orderId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://192.168.1.130:8880/openbravo/services/ExternalSales", "OrderIdentifier"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("payment");
        elemField.setXmlName(new javax.xml.namespace.QName("", "payment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://192.168.1.130:8880/openbravo/services/ExternalSales", "Payment"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("state");
        elemField.setXmlName(new javax.xml.namespace.QName("", "state"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
