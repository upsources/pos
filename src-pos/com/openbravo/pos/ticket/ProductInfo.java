//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This file is modified as part of fastfood branch of Openbravo POS. 2008
//    These modifications are copyright Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
//    e-mail: imasd@opensistemas.com
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
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package com.openbravo.pos.ticket;

import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.format.Formats;

/**
 * Modified by:
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class ProductInfo implements SerializableRead, IKeyed /* , Externalizable, SerializableWrite */ {
    
    protected String m_ID;
    protected String m_sRef;
    protected String m_sCode;
    protected String m_sName;
    protected boolean m_bCom;
    protected boolean m_bScale;
    protected String m_sCategoryID;
    
    protected TaxInfo m_TaxInfo;
    protected double m_dPriceBuy;
    protected double m_dPriceSell;
    
    /** Creates new ProductInfo */
    public ProductInfo() {
        m_ID = null;
        m_sRef = "0000";
        m_sCode = "0000";
        m_sName = null;
        m_bCom = false;
        m_bScale = false;
        m_sCategoryID = null;
        m_TaxInfo = null;
        m_dPriceBuy = 0.0;
        m_dPriceSell = 0.0;
    }
    
    public void readValues(DataRead dr) throws BasicException {
        m_ID = dr.getString(1);
        m_sRef = dr.getString(2);
        m_sCode = dr.getString(3);
        m_sName = dr.getString(4);
        m_bCom = dr.getBoolean(5).booleanValue();
        m_bScale = dr.getBoolean(6).booleanValue();
        m_dPriceBuy = dr.getDouble(7).doubleValue();
        m_dPriceSell = dr.getDouble(8).doubleValue();
        m_TaxInfo = new TaxInfo(dr.getString(9), dr.getString(10), dr.getDouble(11).doubleValue());    
        m_sCategoryID = dr.getString(12);
    }
   
    public final String getID() {
        return m_ID;
    }
    
    public final void setID(String id) {
        m_ID = id;
    }
    
    public final String getReference(){
        return m_sRef;
    }
    public final void setReference(String sRef){
        m_sRef = sRef;
    }    
    public final String getCode(){
        return m_sCode;
    }
    public final void setCode(String sCode){
        m_sCode = sCode;
    }
    public final String getName() {            
        return m_sName;
    }
    public final void setName(String sName){            
        m_sName = sName;
    }
    public final boolean isCom() {            
        return m_bCom;
    }
    public final void setCom(boolean bValue){            
        m_bCom = bValue;
    }
    public final boolean isScale() {            
        return m_bScale;
    }
    public final void setScale(boolean bValue){            
        m_bScale = bValue;
    }
    public final String getCategoryID() {
        return m_sCategoryID;
    }
    public final void setCategoryID(String sCategoryID) {
        m_sCategoryID = sCategoryID;
    }
    public final void setTaxInfo(TaxInfo tax) {
        m_TaxInfo = tax;
    }
    public final TaxInfo getTaxInfo() {
        return m_TaxInfo;
    }
    public final String getTaxID() {
        return m_TaxInfo == null ? null : m_TaxInfo.getId();
    }
//    public final void setTaxID(Integer iTaxID) {
//        m_iTaxID = iTaxID;
//    }    
    public final String getTaxName() {
        return m_TaxInfo == null ? null : m_TaxInfo.getName();
    }
//    public final void setTaxName(String sName) {
//        m_sTaxName = sName;
//    }
    public final double getTaxRate() {
        return m_TaxInfo == null ? 0.0 : m_TaxInfo.getRate();
    }
//    public final void setTaxRate(double dRate) {
//        m_dTaxRate = dRate;
//    }
    public final double getPriceBuy(){
        return m_dPriceBuy;
    }    
    public final void setPriceBuy(double dPrice) {
        m_dPriceBuy = dPrice;
    }        
    public final double getPriceSell(){        
        return m_dPriceSell;
    }
    public final void setPriceSell(double dPrice) {        
        m_dPriceSell = dPrice;
    }      
    public final double getPriceSellTax() {
        return m_dPriceSell * (1.0 + getTaxRate());
    }
    
    public String printPriceBuy() {
        return (getPriceBuy()==0)?
            Formats.STRING.formatValue(""):
            Formats.CURRENCY.formatValue(new Double(getPriceBuy()));
    }    
    
    public String printPriceSell() {
        return Formats.CURRENCY.formatValue(new Double(getPriceSell()));
    }    
    public String printPriceSellTax() {
        return Formats.CURRENCY.formatValue(new Double(getPriceSellTax()));
    }     
    
    public final String toString() {
        return m_sRef + " - " + m_sName;
    }

    public Object getKey() {
        return m_ID;
    }
}