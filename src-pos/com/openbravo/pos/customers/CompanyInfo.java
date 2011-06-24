package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;

/**
 * Company Info Wrapper
 * @author stas
 */
public class CompanyInfo implements SerializableRead {
    protected String companyName;
    protected String cui;
    protected String nrReg;
    protected String address;
    protected String postalCode;
    protected String city;
    protected String region;
    protected String country;
    protected String phone;
    protected String fax;
    protected String email;
    protected String notes;

    public CompanyInfo() {}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCui() {
        return cui;
    }

    public void setCui(String cui) {
        this.cui = cui;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNrReg() {
        return nrReg;
    }

    public void setNrReg(String nrReg) {
        this.nrReg = nrReg;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        companyName = dr.getString(1);
        cui = dr.getString(2);
        nrReg = dr.getString(3);
        address = dr.getString(4);
        postalCode = dr.getString(5);
        city = dr.getString(6);
        region = dr.getString(7);
        country = dr.getString(8);
        phone = dr.getString(9);
        fax = dr.getString(10);
        email = dr.getString(11);
        notes = dr.getString(12);
    }
}
