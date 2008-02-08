
package com.openbravo.pos.ticket;

import java.util.*;

public class Signumprovider {
    
    private Set m_positives = new HashSet();
    private Set m_negatives = new HashSet();
    
    /** Creates a new instance of Signumprovider */
    public Signumprovider() {
    }
    
    public void addPositive(Object key) {
        m_positives.add(key);
    }
    
    public void addNegative(Object key) {
        m_negatives.add(key);
    }
    
    public Double correctSignum(Object key, Double value) {
        if (m_positives.contains(key)) {
            return value.doubleValue() < 0.0 ? new Double(-value.doubleValue()) : value;
        } else if (m_negatives.contains(key)) {
            return value.doubleValue() > 0.0 ? new Double(-value.doubleValue()) : value;
        } else {
            return value;
        }        
    }    
}
