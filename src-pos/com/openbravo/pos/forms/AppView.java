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
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package com.openbravo.pos.forms;

import java.util.Date;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.printer.*;
import com.openbravo.pos.scale.DeviceScale;
import com.openbravo.pos.scanpal2.DeviceScanner;

/**
 *
 * @author adrianromero
 */
public interface AppView {
    
    public DeviceScale getDeviceScale();
    public DeviceTicket getDeviceTicket();
    public DeviceScanner getDeviceScanner();
      
    public Session getSession();
    public AppProperties getProperties();
    public Object getBean(String beanfactory) throws BeanFactoryException;
     
    public void setActiveCash(String value, Date dStart, Date dEnd);
    public String getActiveCashIndex();
    public Date getActiveCashDateStart();
    public Date getActiveCashDateEnd();
    
    public String getInventoryLocation();
    
    public void waitCursorBegin();
    public void waitCursorEnd();
    
    public AppUserView getAppUserView();
}

