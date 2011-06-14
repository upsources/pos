package com.openbravo.pos.logging;

import java.io.Serializable;

/**
 * Log Object
 * @author stas
 */
class POSLog implements Serializable {
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

}
