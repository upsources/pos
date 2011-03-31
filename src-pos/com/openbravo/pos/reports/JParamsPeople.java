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

package com.openbravo.pos.reports;

import com.openbravo.pos.forms.AppView;
import java.awt.Component;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;

public class JParamsPeople extends javax.swing.JPanel implements ReportEditorCreator {

    public String userName;
    private AppView app;

    /** Creates new form JParamsClosedPos */
    public JParamsPeople() {
    }

    public void init(AppView app) {
        this.app = app;
    }

    public void activate() throws BasicException {
        userName = app.getAppUserView().getUser().getId();
    }
    
    public SerializerWrite getSerializerWrite() {
        return new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING});
    }

    public Component getComponent() {
        return this;
    }
    
    public Object createValue() throws BasicException {
        Object user = Formats.STRING.parseValue(userName);
        if (user == null) {
            return new Object[] {QBFCompareEnum.COMP_NONE, null};
        } else {
            return new Object[] {QBFCompareEnum.COMP_RE, user};
        }
    }    
}

