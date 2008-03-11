/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.pos.util;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;

/**
 *
 * @author adrian
 */
public class BarcodeImage {
    
    public static Image getBarcode128(String value) {
        
        AbstractBarcodeBean barcode = new Code128Bean();
        barcode.setModuleWidth(1.0); 
        barcode.setBarHeight(40.0);
        barcode.setFontSize(10.0);
        barcode.setQuietZone(10.0);
        barcode.doQuietZone(true);                
        barcode.setMsgPosition(HumanReadablePlacement.HRP_NONE);
        BarcodeDimension dim = barcode.calcDimensions(value);
        int width = (int) dim.getWidth(0);
        int height = (int) dim.getHeight(0);        
        
        BufferedImage imgtext = new BufferedImage(width, height,  BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imgtext.createGraphics();
        
        g2d.setColor(Color.BLACK);
        
        try {
            barcode.generateBarcode(new Java2DCanvasProvider(g2d, 0), value);
        } catch (IllegalArgumentException e) {
            g2d.drawRect(0, 0, width, height);
            g2d.drawLine(0, 0, width, height);
            g2d.drawLine(width, 0, 0, height);
        }
        
        g2d.dispose();
        
        return imgtext;  
    }

}
