/**
 * ExternalSalesImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.openbravo.ws.externalsales;

public interface ExternalSalesImpl extends java.rmi.Remote {
    public com.openbravo.ws.externalsales.Product[] getProductsCatalog(int in0, int in1, int in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException;
    public com.openbravo.ws.externalsales.ProductPlus[] getProductsPlusCatalog(int in0, int in1, int in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException;
    public void uploadOrders(int in0, int in1, int in2, com.openbravo.ws.externalsales.Order[] in3, java.lang.String in4, java.lang.String in5) throws java.rmi.RemoteException;
    public com.openbravo.ws.externalsales.Order[] getOrders(int in0, int in1, com.openbravo.ws.externalsales.OrderIdentifier[] in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException;
}
