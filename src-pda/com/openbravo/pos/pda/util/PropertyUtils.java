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

package com.openbravo.pos.pda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author jaroslawwozniak
 */
public class PropertyUtils {

    private Properties m_propsconfig;
    private File configFile;
    private final String APP_ID = "openbravopos";

    public PropertyUtils() {
        init(getDefaultConfig());
    }

    private void init(File configfile) {
        this.configFile = configfile;
        load();
    }

    private File getDefaultConfig() {
        return new File(new File(System.getProperty("user.home")), APP_ID + ".properties");
    }

    private void load() {
        // Cargo las propiedades
        try {
            InputStream in = new FileInputStream(configFile);
            if (in != null) {
                m_propsconfig = new Properties();
                m_propsconfig.load(in);
                in.close();
            }
        } catch (IOException e) {
        }
    }

    public String getProperty(String sKey) {
        return m_propsconfig.getProperty(sKey);
    }

    public String getDriverName() {
        return m_propsconfig.getProperty("db.driver");
    }

    public String getUrl() {
        return m_propsconfig.getProperty("db.URL");
    }

    public String getDBUser() {
        return m_propsconfig.getProperty("db.user");
    }

    public String getDBPassword() {
        CryptUtils cypher = new CryptUtils("cypherkey" + getDBUser());

        return cypher.decrypt(m_propsconfig.getProperty("db.password").substring(6));
    }
}
