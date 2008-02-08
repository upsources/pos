/**
 * ExternalSalesImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package net.opentrends.openbravo.ws.types;

public interface ExternalSalesImpl extends java.rmi.Remote {
    public java.lang.String test2(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String test() throws java.rmi.RemoteException;
    public net.opentrends.openbravo.ws.types.Product[] getProductsCatalog(int in0, int in1, int in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException;
    public void uploadOrders(int in0, int in1, int in2, net.opentrends.openbravo.ws.types.Order[] in3, java.lang.String in4, java.lang.String in5) throws java.rmi.RemoteException;
    public net.opentrends.openbravo.ws.types.Order[] getOrders(int in0, int in1, net.opentrends.openbravo.ws.types.OrderIdentifier[] in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException;
}
