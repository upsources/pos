//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 51 Franklin Street, Fifth floor, Boston, MA  02110-1301  USA
package com.openbravo.pos.ticket;

import java.io.Serializable;

/**
 *
 * @author jaroslawwozniak
 */
public class TaxInfo implements Serializable {

    private static final long serialVersionUID = -2705212098856473043L;
    private String id;
    private String name;
    private String taxcategoryid;
    private String taxcustcategoryid;
    private String parentid;

    private double rate;
    private boolean cascade;
    private Integer order;

    public boolean isCascade() {
        return cascade;
    }

    public void setCascade(boolean cascade) {
        this.cascade = cascade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTaxcategoryid() {
        return taxcategoryid;
    }

    public void setTaxcategoryid(String taxcategoryid) {
        this.taxcategoryid = taxcategoryid;
    }

    public String getTaxcustcategoryid() {
        return taxcustcategoryid;
    }

    public void setTaxcustcategoryid(String taxcustcategoryid) {
        this.taxcustcategoryid = taxcustcategoryid;
    }
}
