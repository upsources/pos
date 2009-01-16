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

import java.text.NumberFormat;

/**
 *
 * @author jaroslawwozniak
 */
public class FormatUtils {



    public static String formatCurrency(Object value) {
        NumberFormat m_currencyformat = NumberFormat.getCurrencyInstance();
        return m_currencyformat.format(((Number) value).doubleValue());
    }

    public  static String formatDouble(Object value) {
        NumberFormat doubleFormat = NumberFormat.getNumberInstance();

        return doubleFormat.format(((Number) value).doubleValue());
    }
}
