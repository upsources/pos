package com.openbravo.pos.logging;

import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import java.util.UUID;

/**
 * Some basic logging feature
 * @author stas
 */
public class POSLogger {
    private String component;
    private String user_id = null;
    private DataLogicLogs dlLogs;
    private AppView app;

    public POSLogger ( AppView a, String component ) {
        this.component = component;
        this.app = a;
        loadDataLogic();
    }

    public POSLogger ( AppView a, String component, String uid ) {
        this.component = component;
        this.user_id = uid;
        this.app = a;
        loadDataLogic();
    }

    private void loadDataLogic() {
        dlLogs = (DataLogicLogs) app.getBean("com.openbravo.pos.logging.DataLogicLogs");
    }

    public void log ( String content ) {
        try {
            dlLogs.quickAdd(
                UUID.randomUUID().toString(),
                this.component,
                this.user_id,
                content
            );
        } catch (BasicException ex) {}
    }
}
