package com.openbravo.pos.logging;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

/**
 * Log Object
 * @author stas
 */
public class POSLog implements SerializableRead, IKeyed {
    private String id;
    private String component;
    private String userId;
    private String content;
    private String ts;

    POSLog(String id) {
        this.id = id;
    }

    void setComponent(String component) {
        this.component = component;
    }

    void setUserId(String uid) {
        this.userId = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    void setTimeStamp(String ts) {
        this.ts = ts;
    }

    @Override
    public void readValues(DataRead dr) throws BasicException {
        this.id = dr.getString(1);
        this.component = dr.getString(2);
        this.userId = dr.getString(3);
        this.content = dr.getString(4);
        this.ts = dr.getString(5);
    }

    @Override
    public Object getKey() {
        return this.id;
    }

}
