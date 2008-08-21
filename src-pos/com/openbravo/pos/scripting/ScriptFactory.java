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

package com.openbravo.pos.scripting;

/**
 *
 * @author adrianromero
 * Created on 5 de marzo de 2007, 19:56
 *
 */
public class ScriptFactory {
    
    public static final String VELOCITY = "velocity";
    public static final String BEANSHELL = "beanshell";
    public static final String RHINO = "rhino";
    
    /** Creates a new instance of ScriptFactory */
    private ScriptFactory() {
    }
    
    public static ScriptEngine getScriptEngine(String name) throws ScriptException {
        if (VELOCITY.equals(name)) {
            return new ScriptEngineVelocity();
        } else if (BEANSHELL.equals(name)) {
            return new ScriptEngineBeanshell();
//        } else if (RHINO.equals(name)) {
//            return new ScriptEngineRhino();
//        } else if (name.startsWith("generic:")) {
//            return new ScriptEngineGeneric(name.substring(8));            
        } else {
            throw new ScriptException("Script engine not found: " + name);
        }
    }    
}
