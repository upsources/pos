//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
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

package com.openbravo.pos.sales;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.pos.printer.*;

import com.openbravo.pos.forms.JPanelView;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.JProductFinder;
import com.openbravo.pos.scale.ScaleException;
import com.openbravo.pos.payment.JPaymentSelect;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.HashMapKeyed;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.catalog.CatalogSelector;
import com.openbravo.pos.catalog.JCatalogSubgroups;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.customers.JCustomerFinder;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.forms.BeanFactoryApp;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.payment.JPaymentSelectReceipt;
import com.openbravo.pos.payment.JPaymentSelectRefund;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.DiscountInfo;
import com.openbravo.pos.ticket.TariffInfo;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.ticket.TicketProductInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

  
/**
 *
 * @author adrianromero
 * Modified by:
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */

public abstract class JPanelTicket extends JPanel implements JPanelView, BeanFactoryApp, TicketsEditor {
    // Variable numerica
    private final static int NUMBERZERO = 0;
    private final static int NUMBERVALID = 1;
    
    private final static int NUMBER_INPUTZERO = 0;
    private final static int NUMBER_INPUTZERODEC = 1;
    private final static int NUMBER_INPUTINT = 2;
    private final static int NUMBER_INPUTDEC = 3; 
    private final static int NUMBER_PORZERO = 4; 
    private final static int NUMBER_PORZERODEC = 5; 
    private final static int NUMBER_PORINT = 6; 
    private final static int NUMBER_PORDEC = 7; 
    
    protected final static int PRODUCT_SINGLE = 0;
    protected final static int PRODUCT_COMPOSITION = 1;
    protected final static int PRODUCT_SUBGROUP = 2;

    protected JTicketLines m_ticketlines;
        
    // private Template m_tempLine;
    private TicketParser m_TTP;
    
    protected TicketInfo m_oTicket; 
    protected Object m_oTicketExt; 
    
    private boolean m_bIsSubproduct;
    protected boolean m_bIsRefundPanel;
    
    // Estas tres variables forman el estado...
    private int m_iNumberStatus;
    private int m_iNumberStatusInput;
    private int m_iNumberStatusPor;
    private StringBuffer m_sBarcode;
            
    private JTicketsBag m_ticketsbag;
    
    private SentenceList m_senttax;
    private Map<Object, TaxInfo> taxmap;
    private ComboBoxValModel m_TaxModel;
    
    private SentenceList m_senttariff;
    private java.util.List m_TariffList;
    private ComboBoxValModel m_TariffModel;
    
    private ScriptObject scriptobjinst;
    protected JPanelButtons m_jbtnconfig;
    
    protected double m_dMultiply;
    protected int m_iProduct;
    protected Component m_catalog;
    protected CatalogSelector m_cat;

    protected AppView m_App;
    protected DataLogicSystem dlSystem;
    protected DataLogicSales dlSales;
    protected DataLogicCustomers dlCustomers;
    
    private JPaymentSelect paymentdialogreceipt;
    private JPaymentSelect paymentdialogrefund;

    /** Creates new form JTicketView */
    public JPanelTicket() {
        
        initComponents ();   
    }
   
    public void init(AppView app) throws BeanFactoryException {
        
        m_App = app;
        m_bIsRefundPanel = false;
        dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        dlSales = (DataLogicSales) m_App.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        dlCustomers = (DataLogicCustomers) m_App.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");
        
        // borramos el boton de bascula si no hay bascula conectada
        if (!m_App.getDeviceScale().existsScale()) {
            m_jbtnScale.setVisible(false);
        }
        
        m_ticketsbag = getJTicketsBag();
        m_jPanelBag.add(m_ticketsbag.getBagComponent(), BorderLayout.CENTER);
        add(m_ticketsbag.getNullComponent(), "null");

        m_ticketlines = new JTicketLines(dlSystem.getResourceAsXML("Ticket.Line"));
        m_jPanelCentral.add(m_ticketlines, java.awt.BorderLayout.CENTER);
        
        m_TTP = new TicketParser(m_App.getDeviceTicket(), dlSystem);
               
        // Los botones configurables...
        scriptobjinst = new ScriptObject();
        m_jbtnconfig = new JPanelButtons("Ticket.Buttons", scriptobjinst);
        m_jButtonsExt.add(m_jbtnconfig);           
       
        // El panel de los productos o de las lineas...
        m_catalog = getSouthComponent();
        catcontainer.add(m_catalog, BorderLayout.CENTER);
        m_iProduct = PRODUCT_SINGLE;
        
        // El modelo de impuestos
        m_senttax = dlSales.getTaxList();
        m_TaxModel = new ComboBoxValModel();
        
        // Grupos de tarifas
        m_senttariff = dlSales.getTariffAreaList();
        m_TariffModel = new ComboBoxValModel();

        m_jTariff.addActionListener(new ChangeTariffArea());
        
        // ponemos a cero el estado
        stateToZero();  
        
        // inicializamos
        m_oTicket = null;
        m_oTicketExt = null;
    }
    
    public Object getBean() {
        return this;
    }

    protected Component getSouthAuxComponent() {
        m_cat = new JCatalogSubgroups(dlSales,
                "true".equals(m_jbtnconfig.getProperty("pricevisible")),
                "true".equals(m_jbtnconfig.getProperty("taxesincluded")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-width", "64")),
                Integer.parseInt(m_jbtnconfig.getProperty("img-height", "54")));
        m_cat.getComponent().setPreferredSize(new Dimension(
                0,
                Integer.parseInt(m_jbtnconfig.getProperty("cat-height", "245"))));
        m_cat.addActionListener(new CatalogListener());
        ((JCatalogSubgroups)m_cat).setGuideMode(true);
        return m_cat.getComponent();
    }
    
    public JComponent getComponent() {
        return this;
    }

    public void activate() throws BasicException {
        
        paymentdialogreceipt = JPaymentSelectReceipt.getDialog(this);
        paymentdialogreceipt.init(m_App);
        paymentdialogrefund = JPaymentSelectRefund.getDialog(this); 
        paymentdialogrefund.init(m_App);
        
        m_ticketsbag.activate();

        // impuestos incluidos seleccionado ?
        m_jaddtax.setSelected("true".equals(m_jbtnconfig.getProperty("taxesincluded")));

        // Inicializamos el combo de los impuestos.
        java.util.List<TaxInfo> taxlist = m_senttax.list();
        taxmap = new HashMapKeyed<TaxInfo>(taxlist);
        m_TaxModel = new ComboBoxValModel(taxlist);
        m_jTax.setModel(m_TaxModel);

        String taxesid = m_jbtnconfig.getProperty("taxesid");
        if (taxesid == null) {
            if (m_jTax.getItemCount() > 0) {
                m_jTax.setSelectedIndex(0);
            }
        } else {
            m_TaxModel.setSelectedKey(taxesid);
        }               
        
        
        // Inicializamos el combo de los grupos de tarifas
        m_TariffList = m_senttariff.list();
        
        if(m_TariffList.size()>0){
            m_TariffModel = new ComboBoxValModel(m_TariffList);
            m_jTariff.setModel(m_TariffModel);
            updateTariffCombo();
            m_jTariffPanel.setVisible(true);
        } else
            m_jTariffPanel.setVisible(false); // Si no hay grupos de tarifas, no mostramos combos ni etiquetas
                
        
        
        // Authorization for buttons
        m_jDelete.setEnabled(m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits"));
        m_jbtnconfig.setPermissions(m_App.getAppUserView().getUser());          
    }
    
    public boolean deactivate() {
        return m_ticketsbag.deactivate();
    }

    protected abstract JTicketsBag getJTicketsBag();
    protected abstract Component getSouthComponent();
    
    public void setActiveTicket(TicketInfo oTicket, Object oTicketExt) {
        
        m_oTicket = oTicket;
        m_oTicketExt = oTicketExt;
        
        CardLayout cl = (CardLayout)(getLayout());
        
        if (m_oTicket == null) {        
            m_jTicketId.setText(null);            
            m_ticketlines.clearTicketLines();
           
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null);
            m_jDiscount.setText(null);
        
            stateToZero();
            
            // Muestro el panel de nulos.
            cl.show(this, "null");  

        } else {         
            
            // The ticket name
            m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));

            // Limpiamos todas las filas y anadimos las del ticket actual
            m_ticketlines.clearTicketLines();

            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                m_ticketlines.addTicketLine(m_oTicket.getLine(i));
            }
            printPartialTotals();
            stateToZero();
            
            // Muestro el panel de tickets.
            cl.show(this, "ticket");  
            
            // activo el tecleador...
            m_jKeyFactory.setText(null);       
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    m_jKeyFactory.requestFocus();
                }
            });
            
            // Pongo la tarifa del ticket en el combo
            if(m_jTariff!=null)
                updateTariffCombo();
        }
    }
    
    public void updateTariffCombo(){
        updateTariffCombo(m_oTicket);
    }
    
    public void updateTariffCombo(TicketInfo ticket){
        if(ticket!=null){
            if(m_jTariff!=null){
                for (int i= 0; i < m_jTariff.getItemCount(); i++){
                    try{
                        TariffInfo tariff = (TariffInfo) m_jTariff.getItemAt(i);
                        if(tariff!=null){
                            if (tariff.getID().equals(ticket.getTariffArea()))
                                m_jTariff.setSelectedIndex(i);
                        }
                    }catch(ClassCastException e){

                    }
                }

                if(m_jTariff.getSelectedIndex() < 0 && m_jTariff.getItemCount() > 0)
                    if(ticket.getTicketId()==0)  // Si el ticket no se ha vendido todavía, le ponemos por defecto el primer grupo de tarifas.
                        m_jTariff.setSelectedIndex(0); 

                        
            }
        }
        
        
    }

    public TicketInfo getActiveTicket() {
        return m_oTicket;
    }
       
    private void printPartialTotals(){
               
        if (m_oTicket.getLinesCount() == 0) {
            m_jSubtotalEuros.setText(null);
            m_jTaxesEuros.setText(null);
            m_jTotalEuros.setText(null);
        } else {
            m_jSubtotalEuros.setText(Formats.CURRENCY.formatValue(new Double(m_oTicket.getSubTotal())));
            m_jTaxesEuros.setText(Formats.CURRENCY.formatValue(new Double(m_oTicket.getTax())));
            m_jTotalEuros.setText(Formats.CURRENCY.formatValue(new Double(m_oTicket.getTotal())));
        }
        
        DiscountInfo disc = m_oTicket.getGlobalDiscount();
        if (disc == null) m_jDiscount.setText("");
        else m_jDiscount.setText(disc.getName());
    }
    
    protected void paintTicketLine(int index, TicketLineInfo oLine) {
        if (executeEvent("ticket.setline", new ScriptArg("index", index), new ScriptArg("line", oLine)) == null) {
            m_ticketlines.setTicketLine(index, oLine);
            m_ticketlines.setSelectedIndex(index);

            visorTicketLine(oLine); // Y al visor tambien...
            printPartialTotals();   
            stateToZero();  

            // event receipt
            executeEvent("ticket.change");
        }
   }

    private void addTicketLine(ProductInfoExt oProduct, double dMul, double dPrice) {   
        TicketLineInfo l = new TicketLineInfo(oProduct, dMul, dPrice, oProduct.getTaxInfo(), (java.util.Properties) (oProduct.getProperties().clone()));
        l.setSubproduct(m_bIsSubproduct);
        addTicketLine(l);
    }
    
    protected void addTicketLine(TicketLineInfo oLine) {
        // Actualizamos el precio si el grupo de tarificacion no es el de por defecto
        if (m_oTicket.getTariffArea() != null && !oLine.isSubproduct()) {
            List<ProductInfoExt> prods = null;
            
            // Obtenemos los precios del grupo de tarifas seleccionados
            try {
                prods = dlSales.getTariffProds(m_oTicket.getTariffArea());
            } catch (BasicException ex) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.errorchangetariff"), ex);
                msg.show(JPanelTicket.this);
            }
            
            //Comprobamos si hay precio modificado para ese producto
            Iterator<ProductInfoExt> it = prods.iterator();
            while (it.hasNext()) { 
                ProductInfoExt p = it.next();
                if ( p.getID().equals(oLine.getProduct().getId()) ) oLine.setPrice(p.getPriceSell());
            }
        }
        
        // Diferenciamos si el producto es auxiliar (o descuento) de uno normal.
        if (executeEvent("ticket.addline", new ScriptArg("line", oLine)) == null) {
            if (oLine.getProduct().isCom()) {
                // Comentario entonces donde se pueda
                int i = m_ticketlines.getSelectedIndex();

                // me salto el primer producto normal...
                if (i >= 0 && !m_oTicket.getLine(i).getProduct().isCom()) {
                    i++;
                }

                // me salto todos los productos auxiliares...
                while (i >= 0 && i < m_oTicket.getLinesCount() && m_oTicket.getLine(i).getProduct().isCom()) {
                    i++;
                }

                if (i >= 0) {
                    m_oTicket.insertLine(i, oLine);
                    m_ticketlines.insertTicketLine(i, oLine); // Pintamos la linea en la vista...                 
                } else {
                    Toolkit.getDefaultToolkit().beep();                                   
                }
            } else {    
                // Producto normal, entonces al final
                m_oTicket.addLine(oLine);            
                m_ticketlines.addTicketLine(oLine); // Pintamos la linea en la vista... 
            }
  
            visorTicketLine(oLine);
            printPartialTotals();   
            stateToZero();  

            // event receipt
            executeEvent("ticket.change");
        }
    }    
    
    private void removeTicketLine(int i) {
        //La linea a borrar es un descuento
        if (m_oTicket.getLine(i).isDiscount()) {
            // Borramos el descuento y actualizamos el producto al que se aplica
            m_oTicket.removeLine(i);
            m_ticketlines.removeTicketLine(i--);
            m_oTicket.decreaseLocalDiscounts();
            
            while(i >= 0 && (m_oTicket.getLine(i).isProductCom() || m_oTicket.getLine(i).isSubproduct()) ) {
                i--;
            }
            if (i >= 0) m_oTicket.getLine(i).setDiscountInfo(null);
            
        // La linea a borrar es una auxiliar o subproducto, con lo que solo hay que borrar esta linea
        } else if (executeEvent("ticket.removeline", new ScriptArg("index", i)) == null) {
            if (m_oTicket.getLine(i).getProduct().isCom()) {
                // Es un producto auxiliar, lo borro y santas pascuas.
                m_oTicket.removeLine(i);
                m_ticketlines.removeTicketLine(i);   
            } else {
                // Es un producto normal, lo borro.
                m_oTicket.removeLine(i);
                m_ticketlines.removeTicketLine(i); 
                // Y todos lo auxiliaries que hubiera debajo.
                while(i < m_oTicket.getLinesCount() && (m_oTicket.getLine(i).getProduct().isCom() 
                            || m_oTicket.getLine(i).isDiscount()) ) {
                    if (m_oTicket.getLine(i).isDiscount()) 
                        m_oTicket.decreaseLocalDiscounts();
                    m_oTicket.removeLine(i);
                    m_ticketlines.removeTicketLine(i); 
                }
            }

            visorTicketLine(null); // borro el visor 
            printPartialTotals(); // pinto los totales parciales...                           
            stateToZero(); // Pongo a cero    

            // event receipt
            executeEvent("ticket.change");
        }
    }
       
    private Object executeEvent(String eventkey, ScriptArg ... args) {
        try {
            String code = m_jbtnconfig.getEvent(eventkey);
            if (code != null) {
                return scriptobjinst.evalScript(code, args);
            }
        } catch (ScriptException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotexecute"), e);
            msg.show(this);
        }
        return null;                           
    }
    
    private void removeLocalDiscountLine(int i){
        //Avanzamos a la linea siguiente
        ++i;

        // Buscamos si tiene una linea de descuento a borrar
        while(i < m_oTicket.getLinesCount() && 
                (m_oTicket.getLine(i).isProductCom() || m_oTicket.getLine(i).isDiscount()) ) {

            if (m_oTicket.getLine(i).isDiscount()) {
                m_oTicket.removeLine(i);
                m_oTicket.decreaseLocalDiscounts();
                m_ticketlines.removeTicketLine(i); 
                break;
            }
            i++;
        }
    
        visorTicketLine(null); // borro el visor 
        printPartialTotals(); // pinto los totales parciales...                           
        stateToZero(); // Pongo a cero                               
    }
    
    private void addLocalDiscountLine(TicketLineInfo oLine){
        if (!m_oTicket.isLocalDiscountLocked()) {
            m_oTicket.increaseLocalDiscounts();
            
            double dPriceTax = 0;
            TicketLineInfo t = new TicketLineInfo();
            DiscountInfo d = oLine.getDiscountInfo();
            TicketProductInfo p = t.getProduct();

            p.setName(d.getName());
            p.setCom(true);
            t.setTaxInfo(oLine.getTaxInfo());
//            p.setId(null);
            t.setDiscount(true);
            t.setTaxInfo(oLine.getTaxInfo());
            t.setMultiply(oLine.getMultiply());
            
            // El precio depende de si el descuento es porcentual o monetario
            dPriceTax = (d.isPercentage())? 
                        oLine.getPriceTax() * d.getQuantity():
                        d.getQuantity();
            t.setPriceTax(-dPriceTax);

            addTicketLine(t);
        } else {
            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotmixdiscounts")).show(this);           
        }        
    }

    private void setGlobalDiscount(DiscountInfo disc) {
        m_oTicket.setGlobalDiscount(disc);
        m_jDiscount.setText(disc.getName());
        
        printPartialTotals();
    }
    
    
    private ProductInfoExt getInputProduct() {
        ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
        oProduct.setReference(null);
        oProduct.setCode(null);
        oProduct.setName("");
        oProduct.setPriceSell(includeTaxes(getInputValue()));  
        oProduct.setTaxInfo((TaxInfo) m_TaxModel.getSelectedItem());
        return oProduct;
    }
    
    private double includeTaxes(double dValue) {
        if (m_jaddtax.isSelected()) {
            TaxInfo tax = (TaxInfo) m_TaxModel.getSelectedItem();
            double dTaxRate = tax == null ? 0.0 : tax.getRate();           
            return dValue / (1.0 + dTaxRate);      
        } else {
            return dValue;
        }
    }
    
    private double getInputValue() {
        try {
            return Double.parseDouble(m_jPrice.getText());
        } catch (NumberFormatException e){
            return 0.0;
        }
    }

    private double getPorValue() {
        try {
            return Double.parseDouble(m_jPor.getText().substring(1));                
        } catch (NumberFormatException e){
            return 1.0;
        } catch (StringIndexOutOfBoundsException e){
            return 1.0;
        }
    }
    
    private void stateToZero(){
        m_jPor.setText("");
        m_jPrice.setText("");
        m_sBarcode = new StringBuffer();

        m_iNumberStatus = NUMBER_INPUTZERO;
        m_iNumberStatusInput = NUMBERZERO;
        m_iNumberStatusPor = NUMBERZERO;
    }
    
    private void incProductByCode(String sCode) {
    // precondicion: sCode != null
        
        try {
            ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {                  
                Toolkit.getDefaultToolkit().beep();                   
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);           
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                incProduct(oProduct);
            }
        } catch (BasicException eData) {
            stateToZero();           
            new MessageInf(eData).show(this);           
        }
    }
    
    private void incProductByCodePrice(String sCode, double dPriceSell) {
    // precondicion: sCode != null
        
        try {
            ProductInfoExt oProduct = dlSales.getProductInfoByCode(sCode);
            if (oProduct == null) {                  
                Toolkit.getDefaultToolkit().beep();                   
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noproduct")).show(this);           
                stateToZero();
            } else {
                // Se anade directamente una unidad con el precio y todo
                if (m_jaddtax.isSelected()) {
                    // debemos quitarle los impuestos ya que el precio es con iva incluido...
                    addTicketLine(oProduct, 1.0, dPriceSell / (1.0 + oProduct.getTaxRate()));
                } else {
                    addTicketLine(oProduct, 1.0, dPriceSell);
                }                
            }
        } catch (BasicException eData) {
            stateToZero();
            new MessageInf(eData).show(this);               
        }
    }
    
    private void incProduct(ProductInfoExt prod) {
        
        if (prod.isScale() && m_App.getDeviceScale().existsScale()) {
            try {
                Double value = m_App.getDeviceScale().readWeight();
                if (value != null) {
                    incProduct(value.doubleValue(), prod);
                }
            } catch (ScaleException e) {
                Toolkit.getDefaultToolkit().beep();                
                new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);           
                stateToZero(); 
            }
        } else {
            // No es un producto que se pese o no hay balanza
            incProduct(1.0, prod);
        }
    }
    
    private void incProduct(double dPor, ProductInfoExt prod) {
        // precondicion: prod != null
        addTicketLine(prod, dPor, prod.getPriceSell());       
    }
       
    protected void buttonTransition(ProductInfoExt prod) {
        m_bIsSubproduct = false;
        
        if (m_iProduct == PRODUCT_SUBGROUP && prod != null) {
            prod.setCom(true);
            prod.setPriceSell(0.0);
            m_bIsSubproduct = true;
            
            if (m_dMultiply != 1.0) {
                m_iNumberStatusInput = NUMBERVALID;
                m_iNumberStatusPor = NUMBERZERO;
                m_jPrice.setText( String.valueOf(m_dMultiply));
            }
        } else if (m_iProduct == PRODUCT_COMPOSITION) {
            m_dMultiply = (getInputValue()==0)? 1.0: getInputValue();
            m_iProduct = PRODUCT_SUBGROUP;
        }
        
         if (m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
            incProduct(prod);
        } else if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
            incProduct(getInputValue(), prod);
        } else {
            Toolkit.getDefaultToolkit().beep();
        }       
    }
    protected void buttonTransition(DiscountInfo disc) {
        if (m_oTicket.getLocalDiscountsCount() == 0 && m_oTicket.getGlobalDiscount() == null) {
            m_oTicket.setLocalDiscountLock(true);
            setGlobalDiscount(disc);
            
        } else if (m_oTicket.getLocalDiscountsCount() == 0 && m_oTicket.getGlobalDiscount() != null) {
            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.oneglobaldiscount")).show(this);           
        } else {
            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotmixdiscounts")).show(this);           
        }
    }
    
    private void setSubgroupMode(boolean value) {
        m_jOptions.setEnabled(!value);
        m_jPanTicket.setEnabled(!value);
        m_jContEntries.setEnabled(!value);
        
        enableComponents(m_jOptions, !value);
        enableComponents(m_jPanTicket, !value);
        enableComponents(m_jContEntries, !value);
    }
   
    private void enableComponents(Container cont, boolean value) {
        
        for (Component c: cont.getComponents()) {
            try {
                c.setEnabled(value);
                enableComponents((Container) c, value);
            } catch (Exception e) {
            }
        }
    }  
    
    public void changeCatalog() {
        catcontainer.remove(m_catalog);
        setSubgroupMode(m_iProduct == PRODUCT_SUBGROUP);
        
        m_catalog = (m_iProduct == PRODUCT_SUBGROUP)? getSouthAuxComponent(): getSouthComponent();
        catcontainer.add(m_catalog, BorderLayout.CENTER);
        catcontainer.updateUI();
    }
    
    private void stateTransition(char cTrans) {

        if (cTrans == '\n') {
            // Codigo de barras introducido
            if (m_sBarcode.length() > 0) {            
                String sCode = m_sBarcode.toString();
                if (sCode.startsWith("c")) {
                    // barcode of a customers card
                    try {
                        CustomerInfo newcustomer = dlCustomers.findCustomer(sCode);
                        if (newcustomer == null) {
                            Toolkit.getDefaultToolkit().beep();                   
                            new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer")).show(this);           
                        } else {
                            m_oTicket.setCustomer(newcustomer);
                            m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));
                        }
                    } catch (BasicException e) {
                        Toolkit.getDefaultToolkit().beep();                   
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.nocustomer"), e).show(this);           
                    }
                    stateToZero();
                } else if (sCode.length() == 13 && sCode.startsWith("250")) {
                    // barcode of the other machine
                    ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
                    oProduct.setReference(null); // para que no se grabe
                    oProduct.setCode(sCode);
                    oProduct.setName("Ticket " + sCode.substring(3, 7));
                    oProduct.setPriceSell(Double.parseDouble(sCode.substring(7, 12)) / 100);   
                    oProduct.setTaxInfo((TaxInfo) m_TaxModel.getSelectedItem());
                    // Se anade directamente una unidad con el precio y todo
                    addTicketLine(oProduct, 1.0, includeTaxes(oProduct.getPriceSell()));
                } else if (sCode.length() == 13 && sCode.startsWith("210")) {
                    // barcode of a weigth product
                    incProductByCodePrice(sCode.substring(0, 7), Double.parseDouble(sCode.substring(7, 12)) / 100);
                } else {
                    incProductByCode(sCode);
                }
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        } else {
            // otro caracter
            // Esto es para el codigo de barras...
            m_sBarcode.append(cTrans);

            // Esto es para el los productos normales...
            if (cTrans == '\u007f') { 
                stateToZero();

            } else if ((cTrans == '0') 
                    && (m_iNumberStatus == NUMBER_INPUTZERO)) {
                m_jPrice.setText("0");            
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                    && (m_iNumberStatus == NUMBER_INPUTZERO)) { 
                // Un numero entero
                m_jPrice.setText(Character.toString(cTrans));
                m_iNumberStatus = NUMBER_INPUTINT;    
                m_iNumberStatusInput = NUMBERVALID;
            } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                       && (m_iNumberStatus == NUMBER_INPUTINT)) { 
                // Un numero entero
                m_jPrice.setText(m_jPrice.getText() + cTrans);

            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTZERO) {
                m_jPrice.setText("0.");
                m_iNumberStatus = NUMBER_INPUTZERODEC;            
            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_INPUTINT) {
                m_jPrice.setText(m_jPrice.getText() + ".");
                m_iNumberStatus = NUMBER_INPUTDEC;

            } else if ((cTrans == '0')
                       && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) { 
                // Un numero decimal
                m_jPrice.setText(m_jPrice.getText() + cTrans);
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                       && (m_iNumberStatus == NUMBER_INPUTZERODEC || m_iNumberStatus == NUMBER_INPUTDEC)) { 
                // Un numero decimal
                m_jPrice.setText(m_jPrice.getText() + cTrans);
                m_iNumberStatus = NUMBER_INPUTDEC;
                m_iNumberStatusInput = NUMBERVALID;

            } else if (cTrans == '*' 
                    && (m_iNumberStatus == NUMBER_INPUTINT || m_iNumberStatus == NUMBER_INPUTDEC)) {
                m_jPor.setText("x");
                m_iNumberStatus = NUMBER_PORZERO;            
            } else if (cTrans == '*' 
                    && (m_iNumberStatus == NUMBER_INPUTZERO || m_iNumberStatus == NUMBER_INPUTZERODEC)) {
                m_jPrice.setText("0");
                m_jPor.setText("x");
                m_iNumberStatus = NUMBER_PORZERO;       

            } else if ((cTrans == '0') 
                    && (m_iNumberStatus == NUMBER_PORZERO)) {
                m_jPor.setText("x0");            
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                    && (m_iNumberStatus == NUMBER_PORZERO)) { 
                // Un numero entero
                m_jPor.setText("x" + Character.toString(cTrans));
                m_iNumberStatus = NUMBER_PORINT;            
                m_iNumberStatusPor = NUMBERVALID;
            } else if ((cTrans == '0' || cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                       && (m_iNumberStatus == NUMBER_PORINT)) { 
                // Un numero entero
                m_jPor.setText(m_jPor.getText() + cTrans);

            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORZERO) {
                m_jPor.setText("x0.");
                m_iNumberStatus = NUMBER_PORZERODEC;            
            } else if (cTrans == '.' && m_iNumberStatus == NUMBER_PORINT) {
                m_jPor.setText(m_jPor.getText() + ".");
                m_iNumberStatus = NUMBER_PORDEC;

            } else if ((cTrans == '0')
                       && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) { 
                // Un numero decimal
                m_jPor.setText(m_jPor.getText() + cTrans);
            } else if ((cTrans == '1' || cTrans == '2' || cTrans == '3' || cTrans == '4' || cTrans == '5' || cTrans == '6' || cTrans == '7' || cTrans == '8' || cTrans == '9')
                       && (m_iNumberStatus == NUMBER_PORZERODEC || m_iNumberStatus == NUMBER_PORDEC)) { 
                // Un numero decimal
                m_jPor.setText(m_jPor.getText() + cTrans);
                m_iNumberStatus = NUMBER_PORDEC;            
                m_iNumberStatusPor = NUMBERVALID;  
            
            } else if (cTrans == '\u00a7' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
                // Scale button pressed and a number typed as a price
                if (m_App.getDeviceScale().existsScale() && m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits")) {
                    try {
                        Double value = m_App.getDeviceScale().readWeight();
                        if (value != null) {
                            addTicketLine(getInputProduct(), value.doubleValue(), includeTaxes(getInputValue()));
                        }
                    } catch (ScaleException e) {
                        Toolkit.getDefaultToolkit().beep();
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);           
                        stateToZero(); 
                    }
                } else {
                    // No existe la balanza;
                    Toolkit.getDefaultToolkit().beep();
                }
            } else if (cTrans == '\u00a7' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
                // Scale button pressed and no number typed.
                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else if (m_App.getDeviceScale().existsScale()) {
                    try {
                        Double value = m_App.getDeviceScale().readWeight();
                        if (value != null) {
                            TicketLineInfo oLine = m_oTicket.getLine(i);
                            oLine.setMultiply(value.doubleValue());
                            oLine.setPrice(Math.abs(oLine.getPrice()));
                            paintTicketLine(i, oLine);
                        }
                    } catch (ScaleException e) {
                        // Error de pesada.
                        Toolkit.getDefaultToolkit().beep();
                        new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.noweight"), e).show(this);           
                        stateToZero(); 
                    }
                } else {
                    // No existe la balanza;
                    Toolkit.getDefaultToolkit().beep();
                }      
                
            // Anadimos un producto mas a la linea seleccionada
            } else if (cTrans == '+' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    // Sumamos uno a la seleccionada...
                    TicketLineInfo oLine = m_oTicket.getLine(i);
                    oLine.addOneMore();
                    paintTicketLine(i, oLine); 
                }

            // Eliminamos un producto mas a la linea seleccionada
            } else if (cTrans == '-' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERZERO) {
                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    // Restamos uno a la seleccionada...
                    TicketLineInfo oLine = m_oTicket.getLine(i);
                    oLine.remOneMore();
                    if (oLine.getMultiply() <= 0.0) {                   
                        removeTicketLine(i); // elimino la linea
                    } else {
                        paintTicketLine(i, oLine);                   
                    }
                }

            // Ponemos n productos a la linea seleccionada
            } else if (cTrans == '+' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID) {
                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    double dPor = getPorValue();
                    TicketLineInfo oLine = m_oTicket.getLine(i);
                    oLine.setMultiply(dPor);
                    oLine.setPrice(Math.abs(oLine.getPrice()));
                    paintTicketLine(i, oLine); 
                }

            // Ponemos n productos negativos a la linea seleccionada
            } else if (cTrans == '-' 
                    && m_iNumberStatusInput == NUMBERZERO && m_iNumberStatusPor == NUMBERVALID) {
                int i = m_ticketlines.getSelectedIndex();
                if (i < 0){
                    Toolkit.getDefaultToolkit().beep();
                } else {
                    double dPor = getPorValue();
                    TicketLineInfo oLine = m_oTicket.getLine(i);
                    oLine.setMultiply(dPor);
                    oLine.setPrice(-Math.abs(oLine.getPrice()));
                    paintTicketLine(i, oLine);                
                }

            // Anadimos 1 producto
            } else if (cTrans == '+' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
                    && m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits")) {
                addTicketLine(getInputProduct(), 1.0, includeTaxes(getInputValue()));

            // Anadimos 1 producto con precio negativo
            } else if (cTrans == '-' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO
                    && m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits")) {
                addTicketLine(getInputProduct(), 1.0, -includeTaxes(getInputValue()));

            // Anadimos n productos
            } else if (cTrans == '+' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
                    && m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits")) {
                addTicketLine(getInputProduct(), getPorValue(), includeTaxes(getInputValue()));

            // Anadimos n productos con precio negativo ?
            } else if (cTrans == '-' 
                    && m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERVALID
                    && m_App.getAppUserView().getUser().hasPermission("com.openbravo.pos.sales.JPanelTicketEdits")) {
                addTicketLine(getInputProduct(), getPorValue(), -includeTaxes(getInputValue()));

            // Totals() Igual;
            } else if (cTrans == ' ' || cTrans == '=') {
                if (m_oTicket.getLinesCount() > 0) {
                    if (closeTicket(m_oTicket, m_oTicketExt)) {
                        // Ends edition of current receipt
                        m_ticketsbag.deleteTicket();  
                    }
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        }
    }
    
    private boolean closeTicket(TicketInfo ticket, Object ticketext) {
        
        // Muestro el total
        printTicket("Printer.TicketTotal", ticket, ticketext);

        // reset the payment info
        ticket.resetPayments();

        // Select the Payments information
        JPaymentSelect paymentdialog = ticket.getTotal() >= 0.0 
                ? paymentdialogreceipt
                : paymentdialogrefund;
        paymentdialog.setPrintSelected("true".equals(m_jbtnconfig.getProperty("printselected", "true")));

        if (paymentdialog.showDialog(ticket.getTotal(), ticket.getCustomer())) {

            // assign the payments selected.
            ticket.setPayments(paymentdialog.getSelectedPayments());

            // Asigno los valores definitivos del ticket...
            ticket.setUser(m_App.getAppUserView().getUser().getUserInfo()); // El usuario que lo cobra
            ticket.setActiveCash(m_App.getActiveCashIndex());
            ticket.setDate(new Date()); // Le pongo la fecha de cobro

            // Save the receipt and assign a receipt number
            try {
                dlSales.saveTicket(ticket, m_App.getInventoryLocation());                       
            } catch (BasicException eData) {
                MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosaveticket"), eData);
                msg.show(this);
            }

            // Print receipt.
            printTicket(paymentdialog.isPrintSelected()
                    ? "Printer.Ticket"
                    : "Printer.Ticket2", ticket, ticketext);
            return true;
        } else {
            return false;
        }
    }
       
    private void printTicket(String sresourcename, TicketInfo ticket, Object ticketext) {

        String sresource = dlSystem.getResourceAsXML(sresourcename);
        if (sresource == null) {
            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"));
            msg.show(JPanelTicket.this);
        } else {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("taxes", taxmap);
                script.put("ticket", ticket);
                script.put("place", ticketext);
                script.put("tariffname", getTariffName(m_oTicket.getTariffArea()) );
                m_TTP.printTicket(script.eval(sresource).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintticket"), e);
                msg.show(JPanelTicket.this);
            }
        }
    }

    private void visorTicketLine(TicketLineInfo oLine){
        if (oLine == null) { 
             m_App.getDeviceTicket().getDeviceDisplay().clearVisor();
        } else {                 
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.VELOCITY);
                script.put("ticketline", oLine);
                m_TTP.printTicket(script.eval(dlSystem.getResourceAsXML("Printer.TicketLine")).toString());
            } catch (ScriptException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JPanelTicket.this);
            } catch (TicketPrinterException e) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.cannotprintline"), e);
                msg.show(JPanelTicket.this);
            }
        } 
    }    

    private String getTariffName (String id) {
        if (id == null) return "";
        
        String name = "";
        try {
            java.util.List<TariffInfo> tariffs = (dlSales.getTariffAreaList()).list();
            for (TariffInfo t : tariffs) {
                if (t.getID().equals(m_oTicket.getTariffArea())) {
                    name = t.getName();
                    break;
                }
            }
        } catch (BasicException ex) {
            name = "";
        }
        return name;
    }
    
    public static class ScriptArg {
        private String key;
        private Object value;
        
        public ScriptArg(String key, Object value) {
            this.key = key;
            this.value = value;
        }
        public String getKey() {
            return key;
        }
        public Object getValue() {
            return value;
        }
    }
    
    public class ScriptObject {
        
        int selectedindex;
        
        private ScriptObject() {
        }
        
        public double getInputValue() {
            if (m_iNumberStatusInput == NUMBERVALID && m_iNumberStatusPor == NUMBERZERO) {
                return JPanelTicket.this.getInputValue();
            } else {
                return 0.0;
            }
        }
               
        public TicketLineInfo getSelectedLine() {
            int i = m_ticketlines.getSelectedIndex();
            if (i < 0){
                return null;
            } else {
                return m_oTicket.getLine(i);             
            }
        }
        
        public int getSelectedIndex() {
            return selectedindex;
        }
        
        public void setSelectedIndex(int i) {
            selectedindex = i;
        }

        public void addTicketLine(String sname, TaxInfo tax, double dmult, double dpricesell) {
            ProductInfoExt oProduct = new ProductInfoExt(); // Es un ticket
            oProduct.setReference(null);
            oProduct.setCode(null);
            oProduct.setName(sname);
            oProduct.setPriceSell(dpricesell);  
            oProduct.setTaxInfo(tax);
            
            JPanelTicket.this.addTicketLine(oProduct, dmult, dpricesell);
        }      
        
        public void printTicket(String sresourcename) {
            JPanelTicket.this.printTicket(sresourcename, m_oTicket, m_oTicketExt);   
        }
        
        public String getResourceAsXML(String sresourcename) {
            return dlSystem.getResourceAsXML(sresourcename);
        }
            
        public BufferedImage getResourceAsImage(String sresourcename) {
            return dlSystem.getResourceAsImage(sresourcename);
        }
        
        public Object evalScript(String code, ScriptArg... args) throws ScriptException {
            try {
                ScriptEngine script = ScriptFactory.getScriptEngine(ScriptFactory.BEANSHELL);
                script.put("ticket", m_oTicket);
                script.put("taxes", taxmap);
                script.put("place", m_oTicketExt);
                script.put("user", m_App.getAppUserView().getUser());
                script.put("sales", this);
                
                // more arguments
                for(ScriptArg arg : args) {
                    script.put(arg.getKey(), arg.getValue());
                }             

                selectedindex = m_ticketlines.getSelectedIndex();    

                return script.eval(code);
                
            } finally {
                // repaint current ticket
                setActiveTicket(m_oTicket, m_oTicketExt);
                // select line
                if (selectedindex >= 0 && selectedindex < m_oTicket.getLinesCount()) {
                    m_ticketlines.setSelectedIndex(selectedindex);
                } else if (m_oTicket.getLinesCount() > 0) {
                    m_ticketlines.setSelectedIndex(m_oTicket.getLinesCount() - 1);
                }
            }
        }
    }

    
    public class ChangeTariffArea implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            TicketLineInfo l;
            List<ProductInfoExt> tprods = null;
            
            TariffInfo tariff = (TariffInfo) m_jTariff.getSelectedItem();
            m_oTicket.setTariffArea(tariff.getID());
            
            // Obtenemos los precios del grupo de tarifas seleccionados
            try {
                tprods = dlSales.getTariffProds(tariff.getID());
            } catch (BasicException ex) {
                MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.errorchangetariff"), ex);
                msg.show(JPanelTicket.this);
            }
            
            //Actualizamos las lineas del ticket
            for (int i = 0; i < m_oTicket.getLinesCount(); i++) {
                boolean founded = false;
                l = m_oTicket.getLine(i);
                
                // Cambiamos el precio solo si el precio actual es distinto de 0.0 € y es un producto auxiliar
                if (l.getPrice() != 0.0 && !l.getProduct().isCom()) {
                    Iterator<ProductInfoExt> it = tprods.iterator();

                    //Comprobamos si hay precio modificado para ese producto
                    while (it.hasNext() && !founded) { 
                        ProductInfoExt p = it.next();
                        if ( p.getID().equals(l.getProduct().getId()) ) {
                            l.setPrice(p.getPriceSell());
                            founded = true;
                        }
                    }

                    //Si no hay precio en el grupo de tarifas, ponemos el precio normal del producto
                    if (!founded) {
                        //Buscamos el precio original
                        try {
                            ProductInfoExt p = dlSales.getProductInfo(l.getProduct().getId());
                            if (p != null) l.setPrice(p.getPriceSell());
                        } catch (BasicException ex) {
                            MessageInf msg = new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("message.errorchangetariff"), ex);
                            msg.show(JPanelTicket.this);
                        }   
                    }
                }
                
                paintTicketLine(i, l);
            }
            
        }
    }
    
    
    protected class CatalogListener implements ActionListener {
        private void reloadCatalog () {
            changeCatalog();
            try {
                m_cat.loadCatalog();
            } catch (BasicException ex) {}
        }
        
        public void actionPerformed(ActionEvent e) {
            //Si se ha seleccionado un producto...
            if ( (e.getSource()).getClass().equals(ProductInfoExt.class) ) {
                ProductInfoExt prod = ((ProductInfoExt) e.getSource());
                
                // Terminamos de procesar una composición.
                if (e.getActionCommand().equals("-1")) {
                    m_iProduct = PRODUCT_SINGLE;
                    reloadCatalog();

                } else { 
                    if (prod.getCategoryID().equals("0")) { //Empezamos a procesar una composicion
                        m_iProduct = PRODUCT_COMPOSITION;
                        buttonTransition(prod);
                        reloadCatalog();
                        m_cat.showCatalogPanel(prod.getID());
                    } else 
                        buttonTransition(prod);
                } 
            //Si se ha seleccionado un descuento...
            } else if ( (e.getSource()).getClass().equals(DiscountInfo.class) ) {
                DiscountInfo d = ((DiscountInfo) e.getSource());
                buttonTransition(d);
                
            // Si se ha seleccionado cualquier otra cosa...
            } else {
                // Si es una orden de cancelar la venta de una composición
                if ( e.getActionCommand().equals("cancelSubgroupSale")){

                    int i=m_oTicket.getLinesCount();
                    TicketLineInfo line = m_oTicket.getLine(--i);
                    //Quito todas las líneas que son subproductos (puesto que está recién añadido, pertenecen al menú que estamos cancelando
                    while( (i>0) && (line.isSubproduct()) ){                        
                        m_oTicket.removeLine(i);
                        m_ticketlines.removeTicketLine(i);
                        
                        line= m_oTicket.getLine(--i);
                    }
                    // Quito la línea siguiente, perteneciente al menú en sí
                    if(i >= 0){
                        m_oTicket.removeLine(i);
                        m_ticketlines.removeTicketLine(i);                        
                    }

                    // Actualizo el interfaz
                    m_iProduct = PRODUCT_SINGLE;
                    reloadCatalog();
                }
            }
        }
        
    }
    
    protected class CatalogSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {      
            
            if (!e.getValueIsAdjusting()) {
                int i = m_ticketlines.getSelectedIndex();
                
                // Buscamos el primer producto no Auxiliar.
                while (i >= 0 && m_oTicket.getLine(i).getProduct().isCom()) {
                    i--;
                }
                        
                // Mostramos el panel de catalogo adecuado...
                if (i >= 0) {
                    m_cat.showCatalogPanel(m_oTicket.getLine(i).getProduct().getId());
                } else {
                    m_cat.showCatalogPanel(null);
                }
            }
        }  
    }
    

/** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the FormEditor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        m_jPanContainer = new javax.swing.JPanel();
        m_jOptions = new javax.swing.JPanel();
        m_jButtons = new javax.swing.JPanel();
        m_jTicketId = new javax.swing.JLabel();
        btnCustomer = new javax.swing.JButton();
        m_jEditLine1 = new javax.swing.JButton();
        m_jPanelBag = new javax.swing.JPanel();
        m_jButtonsExt = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        m_jbtnScale = new javax.swing.JButton();
        m_jPanTicket = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        m_jUp = new javax.swing.JButton();
        m_jDown = new javax.swing.JButton();
        m_jDelete = new javax.swing.JButton();
        m_jList = new javax.swing.JButton();
        m_jEditLine = new javax.swing.JButton();
        m_jPanelCentral = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        m_jPanTotals = new javax.swing.JPanel();
        m_jTotalEuros = new javax.swing.JLabel();
        m_jLblTotalEuros1 = new javax.swing.JLabel();
        m_jSubtotalEuros = new javax.swing.JLabel();
        m_jTaxesEuros = new javax.swing.JLabel();
        m_jLblTotalEuros2 = new javax.swing.JLabel();
        m_jLblTotalEuros3 = new javax.swing.JLabel();
        m_jLblTotalEuros4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        m_jDiscount = new javax.swing.JLabel();
        m_jBtnDelGlobalDiscount = new javax.swing.JButton();
        m_jTariffPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        m_jTariff = new javax.swing.JComboBox();
        m_jContEntries = new javax.swing.JPanel();
        m_jPanEntries = new javax.swing.JPanel();
        m_jNumberKeys = new com.openbravo.beans.JNumberKeys();
        jPanel9 = new javax.swing.JPanel();
        m_jPrice = new javax.swing.JLabel();
        m_jPor = new javax.swing.JLabel();
        m_jEnter = new javax.swing.JButton();
        m_jTax = new javax.swing.JComboBox();
        m_jaddtax = new javax.swing.JToggleButton();
        m_jKeyFactory = new javax.swing.JTextField();
        catcontainer = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 204, 153));
        setLayout(new java.awt.CardLayout());

        m_jPanContainer.setLayout(new java.awt.BorderLayout());

        m_jOptions.setLayout(new java.awt.BorderLayout());

        m_jTicketId.setBackground(java.awt.Color.white);
        m_jTicketId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        m_jTicketId.setOpaque(true);
        m_jTicketId.setPreferredSize(new java.awt.Dimension(160, 25));
        m_jTicketId.setRequestFocusEnabled(false);
        m_jButtons.add(m_jTicketId);

        btnCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/kuser.png"))); // NOI18N
        btnCustomer.setFocusPainted(false);
        btnCustomer.setFocusable(false);
        btnCustomer.setMargin(new java.awt.Insets(8, 14, 8, 14));
        btnCustomer.setRequestFocusEnabled(false);
        btnCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerActionPerformed(evt);
            }
        });
        m_jButtons.add(btnCustomer);

        m_jEditLine1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editcut.png"))); // NOI18N
        m_jEditLine1.setFocusPainted(false);
        m_jEditLine1.setFocusable(false);
        m_jEditLine1.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEditLine1.setRequestFocusEnabled(false);
        m_jEditLine1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditLine1ActionPerformed(evt);
            }
        });
        m_jButtons.add(m_jEditLine1);

        m_jOptions.add(m_jButtons, java.awt.BorderLayout.WEST);

        m_jPanelBag.setLayout(new java.awt.BorderLayout());

        m_jButtonsExt.setLayout(new javax.swing.BoxLayout(m_jButtonsExt, javax.swing.BoxLayout.LINE_AXIS));

        m_jbtnScale.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/ark216.png"))); // NOI18N
        m_jbtnScale.setText(AppLocal.getIntString("button.scale")); // NOI18N
        m_jbtnScale.setFocusPainted(false);
        m_jbtnScale.setFocusable(false);
        m_jbtnScale.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jbtnScale.setRequestFocusEnabled(false);
        m_jbtnScale.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jbtnScaleActionPerformed(evt);
            }
        });
        jPanel1.add(m_jbtnScale);

        m_jButtonsExt.add(jPanel1);

        m_jPanelBag.add(m_jButtonsExt, java.awt.BorderLayout.EAST);

        m_jOptions.add(m_jPanelBag, java.awt.BorderLayout.CENTER);

        m_jPanContainer.add(m_jOptions, java.awt.BorderLayout.NORTH);

        m_jPanTicket.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        m_jPanTicket.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        jPanel2.setLayout(new java.awt.GridLayout(0, 1, 5, 5));

        m_jUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1uparrow22.png"))); // NOI18N
        m_jUp.setFocusPainted(false);
        m_jUp.setFocusable(false);
        m_jUp.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jUp.setRequestFocusEnabled(false);
        m_jUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jUpActionPerformed(evt);
            }
        });
        jPanel2.add(m_jUp);

        m_jDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/1downarrow22.png"))); // NOI18N
        m_jDown.setFocusPainted(false);
        m_jDown.setFocusable(false);
        m_jDown.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDown.setRequestFocusEnabled(false);
        m_jDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDownActionPerformed(evt);
            }
        });
        jPanel2.add(m_jDown);

        m_jDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/locationbar_erase.png"))); // NOI18N
        m_jDelete.setFocusPainted(false);
        m_jDelete.setFocusable(false);
        m_jDelete.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jDelete.setRequestFocusEnabled(false);
        m_jDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jDeleteActionPerformed(evt);
            }
        });
        jPanel2.add(m_jDelete);

        m_jList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search22.png"))); // NOI18N
        m_jList.setFocusPainted(false);
        m_jList.setFocusable(false);
        m_jList.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jList.setRequestFocusEnabled(false);
        m_jList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jListActionPerformed(evt);
            }
        });
        jPanel2.add(m_jList);

        m_jEditLine.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line.png"))); // NOI18N
        m_jEditLine.setFocusPainted(false);
        m_jEditLine.setFocusable(false);
        m_jEditLine.setMargin(new java.awt.Insets(8, 14, 8, 14));
        m_jEditLine.setRequestFocusEnabled(false);
        m_jEditLine.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEditLineActionPerformed(evt);
            }
        });
        jPanel2.add(m_jEditLine);

        jPanel5.add(jPanel2, java.awt.BorderLayout.NORTH);

        m_jPanTicket.add(jPanel5, java.awt.BorderLayout.EAST);

        m_jPanelCentral.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        m_jPanTotals.setLayout(new java.awt.GridBagLayout());

        m_jTotalEuros.setBackground(java.awt.Color.white);
        m_jTotalEuros.setFont(new java.awt.Font("Dialog", 1, 14));
        m_jTotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTotalEuros.setOpaque(true);
        m_jTotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTotalEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        m_jPanTotals.add(m_jTotalEuros, gridBagConstraints);

        m_jLblTotalEuros1.setText(AppLocal.getIntString("label.totalcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        m_jPanTotals.add(m_jLblTotalEuros1, gridBagConstraints);

        m_jSubtotalEuros.setBackground(java.awt.Color.white);
        m_jSubtotalEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jSubtotalEuros.setOpaque(true);
        m_jSubtotalEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jSubtotalEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        m_jPanTotals.add(m_jSubtotalEuros, gridBagConstraints);

        m_jTaxesEuros.setBackground(java.awt.Color.white);
        m_jTaxesEuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jTaxesEuros.setOpaque(true);
        m_jTaxesEuros.setPreferredSize(new java.awt.Dimension(150, 25));
        m_jTaxesEuros.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 3);
        m_jPanTotals.add(m_jTaxesEuros, gridBagConstraints);

        m_jLblTotalEuros2.setText(AppLocal.getIntString("label.taxcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        m_jPanTotals.add(m_jLblTotalEuros2, gridBagConstraints);

        m_jLblTotalEuros3.setText(AppLocal.getIntString("label.subtotalcash")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        m_jPanTotals.add(m_jLblTotalEuros3, gridBagConstraints);

        m_jLblTotalEuros4.setText(AppLocal.getIntString("label.discount")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        m_jPanTotals.add(m_jLblTotalEuros4, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        m_jDiscount.setBackground(java.awt.Color.white);
        m_jDiscount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        m_jDiscount.setOpaque(true);
        m_jDiscount.setPreferredSize(new java.awt.Dimension(115, 25));
        m_jDiscount.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel3.add(m_jDiscount, gridBagConstraints);

        m_jBtnDelGlobalDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        m_jBtnDelGlobalDiscount.setMaximumSize(new java.awt.Dimension(26, 26));
        m_jBtnDelGlobalDiscount.setMinimumSize(new java.awt.Dimension(26, 26));
        m_jBtnDelGlobalDiscount.setPreferredSize(new java.awt.Dimension(26, 26));
        m_jBtnDelGlobalDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jBtnDelGlobalDiscountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel3.add(m_jBtnDelGlobalDiscount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 3);
        m_jPanTotals.add(jPanel3, gridBagConstraints);

        jPanel4.add(m_jPanTotals);

        m_jPanelCentral.add(jPanel4, java.awt.BorderLayout.SOUTH);

        m_jTariffPanel.setLayout(new java.awt.GridLayout(1, 0));

        m_jTariff.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "bad" }));
        m_jTariff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jTariffActionPerformed(evt);
            }
        });
        jLabel5.setText(AppLocal.getIntString("Menu.TariffArea")); // NOI18N
        m_jTariffPanel.add(jLabel5);
        m_jTariffPanel.add(m_jTariff);

        m_jPanelCentral.add(m_jTariffPanel, java.awt.BorderLayout.NORTH);

        m_jPanTicket.add(m_jPanelCentral, java.awt.BorderLayout.CENTER);

        m_jPanContainer.add(m_jPanTicket, java.awt.BorderLayout.CENTER);

        m_jContEntries.setLayout(new java.awt.BorderLayout());

        m_jPanEntries.setLayout(new javax.swing.BoxLayout(m_jPanEntries, javax.swing.BoxLayout.Y_AXIS));

        m_jNumberKeys.addJNumberEventListener(new com.openbravo.beans.JNumberEventListener() {
            public void keyPerformed(com.openbravo.beans.JNumberEvent evt) {
                m_jNumberKeysKeyPerformed(evt);
            }
        });
        m_jPanEntries.add(m_jNumberKeys);

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        m_jPrice.setBackground(java.awt.Color.white);
        m_jPrice.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPrice.setOpaque(true);
        m_jPrice.setPreferredSize(new java.awt.Dimension(100, 22));
        m_jPrice.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel9.add(m_jPrice, gridBagConstraints);

        m_jPor.setBackground(java.awt.Color.white);
        m_jPor.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        m_jPor.setOpaque(true);
        m_jPor.setPreferredSize(new java.awt.Dimension(22, 22));
        m_jPor.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel9.add(m_jPor, gridBagConstraints);

        m_jEnter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/barcode.png"))); // NOI18N
        m_jEnter.setFocusPainted(false);
        m_jEnter.setFocusable(false);
        m_jEnter.setRequestFocusEnabled(false);
        m_jEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jEnterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel9.add(m_jEnter, gridBagConstraints);

        m_jTax.setFocusable(false);
        m_jTax.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel9.add(m_jTax, gridBagConstraints);

        m_jaddtax.setText("+");
        m_jaddtax.setFocusPainted(false);
        m_jaddtax.setFocusable(false);
        m_jaddtax.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel9.add(m_jaddtax, gridBagConstraints);

        m_jPanEntries.add(jPanel9);

        m_jKeyFactory.setPreferredSize(new java.awt.Dimension(1, 1));
        m_jKeyFactory.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                m_jKeyFactoryKeyTyped(evt);
            }
        });
        m_jPanEntries.add(m_jKeyFactory);

        m_jContEntries.add(m_jPanEntries, java.awt.BorderLayout.NORTH);

        m_jPanContainer.add(m_jContEntries, java.awt.BorderLayout.EAST);

        catcontainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        catcontainer.setLayout(new java.awt.BorderLayout());
        m_jPanContainer.add(catcontainer, java.awt.BorderLayout.SOUTH);

        add(m_jPanContainer, "ticket");
    }// </editor-fold>//GEN-END:initComponents

    private void m_jTariffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jTariffActionPerformed
        try{
            TariffInfo tariff = (TariffInfo) m_jTariff.getSelectedItem();
            if(tariff!=null)
                m_oTicket.setTariffArea(tariff.getID());
        }catch(java.lang.ClassCastException e){
            
        }
    }//GEN-LAST:event_m_jTariffActionPerformed

    private void m_jbtnScaleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jbtnScaleActionPerformed

        stateTransition('\u00a7');
        
    }//GEN-LAST:event_m_jbtnScaleActionPerformed

    private void m_jEditLineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLineActionPerformed
         int i = m_ticketlines.getSelectedIndex();
        if (i < 0){
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {
            TicketLineInfo oLine = m_oTicket.getLine(i);
            double mult = oLine.getMultiply();
            
            //Si contiene un descuento, lo eliminamos
            if (oLine.getDiscountInfo() != null) removeLocalDiscountLine(i);
            
            // Llammos al panel para editar la linea
            if (JProductLineEdit.showMessage(this, m_App, oLine, !m_bIsRefundPanel)) {
                // se ha modificado la linea
                paintTicketLine(i, oLine); 
            }
            
            //Si contiene un descuento, lo añadimos
            if (oLine.getDiscountInfo() != null) addLocalDiscountLine(oLine);
            
            //Si no es un subproducto y se ha modificado la cantidad, 
            //hay que averiguar si es una composicion y en tal caso propagar el cambio.
            if (oLine.getMultiply() != mult && !oLine.isSubproduct()) {
                mult = oLine.getMultiply();
                
                while(++i < m_oTicket.getLinesCount() &&
                            ((oLine = (TicketLineInfo) m_oTicket.getLine(i)).getProduct().isCom() 
                            || oLine.isDiscount()) ) {
                    oLine.setMultiply(mult);
                    m_ticketlines.removeTicketLine(i);
                    m_ticketlines.insertTicketLine(i, oLine);
                }               
            }
        }

    }//GEN-LAST:event_m_jEditLineActionPerformed

    private void m_jEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEnterActionPerformed

        stateTransition('\n');

    }//GEN-LAST:event_m_jEnterActionPerformed

    private void m_jNumberKeysKeyPerformed(com.openbravo.beans.JNumberEvent evt) {//GEN-FIRST:event_m_jNumberKeysKeyPerformed

        stateTransition(evt.getKey());

    }//GEN-LAST:event_m_jNumberKeysKeyPerformed

    private void m_jKeyFactoryKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_jKeyFactoryKeyTyped

        m_jKeyFactory.setText(null);
        stateTransition(evt.getKeyChar());

    }//GEN-LAST:event_m_jKeyFactoryKeyTyped

    private void m_jDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDeleteActionPerformed

        int i = m_ticketlines.getSelectedIndex();
        if (i < 0){
            Toolkit.getDefaultToolkit().beep(); // No hay ninguna seleccionada
        } else {               
            removeTicketLine(i); // elimino la linea
        }   
        
    }//GEN-LAST:event_m_jDeleteActionPerformed

    private void m_jUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jUpActionPerformed

        m_ticketlines.selectionUp();

    }//GEN-LAST:event_m_jUpActionPerformed

    private void m_jDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jDownActionPerformed

        m_ticketlines.selectionDown();

    }//GEN-LAST:event_m_jDownActionPerformed

    private void m_jListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jListActionPerformed

        ProductInfoExt prod = JProductFinder.showMessage(JPanelTicket.this, dlSales);    
        if (prod != null) {
            buttonTransition(prod);
        }
        
    }//GEN-LAST:event_m_jListActionPerformed

    private void btnCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerActionPerformed

        JCustomerFinder finder = JCustomerFinder.getCustomerFinder(this, dlCustomers);
        finder.search(m_oTicket.getCustomer());
        finder.setVisible(true);
        m_oTicket.setCustomer(finder.getSelectedCustomer());     
        // The ticket name
        m_jTicketId.setText(m_oTicket.getName(m_oTicketExt));
        
}//GEN-LAST:event_btnCustomerActionPerformed

    private void m_jBtnDelGlobalDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jBtnDelGlobalDiscountActionPerformed
        m_oTicket.setGlobalDiscount(null);
        m_oTicket.setLocalDiscountLock(false);
        
        printPartialTotals();
    }//GEN-LAST:event_m_jBtnDelGlobalDiscountActionPerformed

    private void m_jEditLine1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jEditLine1ActionPerformed

        if (m_oTicket.getLinesCount() > 0) {
            ReceiptSplit splitdialog = ReceiptSplit.getDialog(this, dlSystem.getResourceAsXML("Ticket.Line"), dlCustomers);
            
            TicketInfo ticket1 = m_oTicket.copyTicket();
            TicketInfo ticket2 = new TicketInfo();
            ticket2.setGlobalDiscount(m_oTicket.getGlobalDiscount());
            ticket2.setCustomer(m_oTicket.getCustomer());
            
            if (splitdialog.showDialog(ticket1, ticket2, m_oTicketExt)) {
                if (closeTicket(ticket2, m_oTicketExt)) { // already checked  that number of lines > 0                            
                    setActiveTicket(ticket1, m_oTicketExt);// repaint current ticket
                }
            }
        }
        
    }//GEN-LAST:event_m_jEditLine1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCustomer;
    private javax.swing.JPanel catcontainer;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton m_jBtnDelGlobalDiscount;
    private javax.swing.JPanel m_jButtons;
    private javax.swing.JPanel m_jButtonsExt;
    private javax.swing.JPanel m_jContEntries;
    private javax.swing.JButton m_jDelete;
    private javax.swing.JLabel m_jDiscount;
    private javax.swing.JButton m_jDown;
    private javax.swing.JButton m_jEditLine;
    private javax.swing.JButton m_jEditLine1;
    private javax.swing.JButton m_jEnter;
    private javax.swing.JTextField m_jKeyFactory;
    private javax.swing.JLabel m_jLblTotalEuros1;
    private javax.swing.JLabel m_jLblTotalEuros2;
    private javax.swing.JLabel m_jLblTotalEuros3;
    private javax.swing.JLabel m_jLblTotalEuros4;
    private javax.swing.JButton m_jList;
    private com.openbravo.beans.JNumberKeys m_jNumberKeys;
    private javax.swing.JPanel m_jOptions;
    private javax.swing.JPanel m_jPanContainer;
    private javax.swing.JPanel m_jPanEntries;
    private javax.swing.JPanel m_jPanTicket;
    private javax.swing.JPanel m_jPanTotals;
    private javax.swing.JPanel m_jPanelBag;
    private javax.swing.JPanel m_jPanelCentral;
    private javax.swing.JLabel m_jPor;
    private javax.swing.JLabel m_jPrice;
    private javax.swing.JLabel m_jSubtotalEuros;
    protected javax.swing.JComboBox m_jTariff;
    private javax.swing.JPanel m_jTariffPanel;
    private javax.swing.JComboBox m_jTax;
    private javax.swing.JLabel m_jTaxesEuros;
    private javax.swing.JLabel m_jTicketId;
    private javax.swing.JLabel m_jTotalEuros;
    private javax.swing.JButton m_jUp;
    private javax.swing.JToggleButton m_jaddtax;
    private javax.swing.JButton m_jbtnScale;
    // End of variables declaration//GEN-END:variables

}
