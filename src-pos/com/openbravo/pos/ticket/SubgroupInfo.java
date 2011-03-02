//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2008 Open Sistemas de Información Internet, S.L.
//    http://www.opensistemas.com
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

import java.awt.image.*;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.ImageUtils;

/**
 *
 * @author  Luis Ig. Bacas Riveiro	lbacas@opensistemas.com
 * @author  Pablo J. Urbano Santos	purbano@opensistemas.com
 */
public class SubgroupInfo implements SerializableRead, SerializableWrite, IKeyed {

    private String m_sID;
    private String m_sName;
    private BufferedImage m_Image;
    
    /** Constructor por defecto
     */
    
    public SubgroupInfo() {
        m_sID = null;
        m_sName = null;
        m_Image = null;
    }
    
    /**
     * Devuelve el identificador del objeto
     * @return  id
     */
    public Object getKey() {
        return m_sID;
    }
    
    /**
     * Lee los parámetros del objeto del DataRead
     * @param dr DataRead del que lee los datos
     * @throws com.openbravo.basic.BasicException
     * @see DataRead
     */
    public void readValues(DataRead dr) throws BasicException {
        m_sID = dr.getString(1);
        m_sName = dr.getString(2);
        m_Image = ImageUtils.readImage(dr.getBytes(3));
    }
    
    /**
     * Crea un DataWrite a partir de los parámetros del objeto
     * @param dp DataWrite en el que escribe los datos
     * @throws com.openbravo.basic.BasicException
     * @see DataWrite
     */
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_sID);
        dp.setString(2, m_sName);
        dp.setBytes(3, ImageUtils.writeImage(m_Image));        
    }
    
    public void setID(String sID) {
        m_sID = sID;
    }
    
    public String getID() {
        return m_sID;
    }

    public String getName() {
        return m_sName;
    }
    
    public void setName(String sName) {
        m_sName = sName;
    }
    
    public BufferedImage getImage() {
        return m_Image;
    }
    
    public void setImage(BufferedImage img) {
        m_Image = img;
    }
    
    /**
     * Devuelve una cadena con el nombre del objeto
     * @return nombre
     */
    @Override
    public String toString(){
        return m_sName;
    }
}
