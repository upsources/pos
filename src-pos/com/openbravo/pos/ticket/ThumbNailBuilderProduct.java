//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
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
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package com.openbravo.pos.ticket;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class ThumbNailBuilderProduct {
    
    public static final int PRICE_NONE = 0;
    public static final int PRICE_SELL = 1;
    public static final int PRICE_SELLTAX = 2;
    
    private Image m_imgdefault;
    private int m_width;
    private int m_height;
    private int m_iPriceMode;
    
    /** Creates a new instance of ThumbNailBuilderText */
    public ThumbNailBuilderProduct(int width, int height, Image img) {
        this(width, height, img, PRICE_NONE);
    }    
    public ThumbNailBuilderProduct(int width, int height, Image img, int ipricemode) {
        m_width = width;
        m_height = height;     
        m_imgdefault = img;
        m_iPriceMode = ipricemode;
    }    
    
    public Image getThumbNail(ProductInfoExt product) {

        BufferedImage thumb = new BufferedImage(m_width, m_height, BufferedImage.TYPE_4BYTE_ABGR);

        Graphics2D g2d = thumb.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        //g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        g2d.setColor(new Color(0, 0, 0, 0)); // Transparent

        g2d.fillRect(0, 0, m_width, m_height);
        
        // Que imagen imprimo
        if (product.getImage() == null) {
            if (m_imgdefault != null && m_iPriceMode == PRICE_NONE) {
                drawScaledImage(g2d, m_imgdefault);
            }
        } else {
            drawScaledImage(g2d, product.getImage());
        }
        
        // Que texto imprimo
        if (m_iPriceMode == PRICE_SELL) {
            drawImage(g2d, product.getImage());
            drawText(g2d, product.printPriceSell());
        } else if (m_iPriceMode == PRICE_SELLTAX) {
            drawImage(g2d, product.getImage());
            drawText(g2d, product.printPriceSellTax());
        } else {
            drawImageWithDefault(g2d, product.getImage());
        }
        
        g2d.dispose();

        return thumb;            
    }    
    
    private void drawText(Graphics2D g2d, String sPrintText) {
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD));
        Rectangle2D r2d = g2d.getFont().getStringBounds(sPrintText, g2d.getFontRenderContext());

        g2d.setPaint(new GradientPaint(0, m_height/2, new Color(255,255,255,200), 0, m_height, new Color(200,200,200,150)));
        g2d.fillOval(0 , m_height /2, m_width, m_height);

        g2d.setColor(Color.BLACK);
        g2d.drawString(sPrintText, (float) ((m_width - r2d.getWidth()) / 2.0), (float) ((1.3 * m_height + r2d.getHeight()) / 2.0));
    }
    
    private void drawImageWithDefault(Graphics2D g2d, Image img) {
        if (img != null) {
            drawScaledImage(g2d, img);
        } else if (m_imgdefault != null) {
            drawScaledImage(g2d, m_imgdefault);
        }
    }
    
    private void drawImage(Graphics2D g2d, Image img) {
        if (img != null) {
            drawScaledImage(g2d, img);
        }
    }    
    
    private void drawScaledImage(Graphics2D g2d, Image img) {
        double scalex = (double) m_width / (double) img.getWidth(null);
        double scaley = (double) m_height / (double) img.getHeight(null);

        if (scalex < scaley) {
            g2d.drawImage(img, 0,(int) ((m_height - img.getHeight(null) * scalex) / 2.0)
            , m_width, (int) (img.getHeight(null) * scalex),  null);
        } else {
           g2d.drawImage(img, (int) ((m_width - img.getWidth(null) * scaley) / 2.0), 0
           , (int) (img.getWidth(null) * scaley), m_height, null);
        }        
    }
}
