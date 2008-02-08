/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openbravo.possync;

import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.BeanFactoryCache;
import com.openbravo.pos.forms.BeanFactoryException;
import com.openbravo.pos.forms.DataLogicSystem;

/**
 *
 * @author adrian
 */
public class ProductsSyncCreate extends BeanFactoryCache {
    
    public Object constructBean(AppView app) throws BeanFactoryException {
        
        DataLogicSystem dlSystem = (DataLogicSystem) app.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
        DataLogicIntegration dli = (DataLogicIntegration) app.getBean("com.openbravo.possync.DataLogicIntegration");

        ProductsSync bean = new ProductsSync(dlSystem, dli);
        return bean;
    }
}
