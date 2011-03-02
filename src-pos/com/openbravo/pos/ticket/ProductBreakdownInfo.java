/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.ticket;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import java.util.List;

/**
 *
 * @author purbano
 */
public class ProductBreakdownInfo extends ProductInfo {
    private List m_aRawMaterials; // TODO: Establecer el tipo concreto de las materias primas
    

    @Override
    public String printPriceBuy() {
        return super.printPriceBuy();
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        super.readValues(dr);
    }
    
}
