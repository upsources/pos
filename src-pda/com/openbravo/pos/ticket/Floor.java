/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openbravo.pos.ticket;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author openbravo
 */
public class Floor implements Serializable{

    private String id;
    private String name;
    private String image;
    private Set tables;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set getTables() {
        return tables;
    }

    public void setTables(Set tables) {
        this.tables = tables;
    }
}
