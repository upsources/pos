/**
 * WebServiceImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.openbravo.ws.customers;

public interface WebServiceImpl extends java.rmi.Remote {
    public com.openbravo.ws.customers.Customer[] getCustomers(int in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public com.openbravo.ws.customers.Customer getCustomer(int in0, int in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException;
    public com.openbravo.ws.customers.Customer getCustomer(int in0, java.lang.String in1, java.lang.String in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException;
    public java.lang.Boolean updateCustomer(com.openbravo.ws.customers.BusinessPartner in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public int[] getCustomerAddresses(int in0, int in1, java.lang.String in2, java.lang.String in3) throws java.rmi.RemoteException;
    public com.openbravo.ws.customers.Location getCustomerLocation(int in0, int in1, int in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException;
    public java.lang.Boolean updateAddress(com.openbravo.ws.customers.Location in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
    public com.openbravo.ws.customers.Contact getCustomerContact(int in0, int in1, int in2, java.lang.String in3, java.lang.String in4) throws java.rmi.RemoteException;
    public java.lang.Boolean updateContact(com.openbravo.ws.customers.Contact in0, java.lang.String in1, java.lang.String in2) throws java.rmi.RemoteException;
}
