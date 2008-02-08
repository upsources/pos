//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
//    http://sourceforge.net/projects/
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

import bsh.EvalError;
import bsh.Interpreter;

/**
 *
 * @author adrianromero
 * Created on 5 de marzo de 2007, 19:57
 *
 */
class ScriptEngineBeanshell implements ScriptEngine {

    private Interpreter i;
    
    /** Creates a new instance of ScriptEngineBeanshell */
    public ScriptEngineBeanshell() {
        i = new Interpreter();
    }
    
    public void put(String key, Object value) {
        
        try {
            i.set(key, value);
        } catch (EvalError e) {
        }
    }
    
    public Object get(String key) {
        
        try {
            return i.get(key);
        } catch (EvalError e) {
            return null;
        }
    }
    
    public Object eval(String src) throws ScriptException {

        try {
            return i.eval(src);  
        } catch (EvalError e) {
            throw new ScriptException(e.getMessage(), e);
        }        
    }   
}
