package com.openbravo.pos.logging;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.BeanFactoryDataSingle;

/**
 * Data Logic Logs DB handler
 * @author stas
 */
public class DataLogicLogs extends BeanFactoryDataSingle {
    protected Session s;
    private TableDefinition tlogs;

    @Override
    public void init(Session s) {
        this.s = s;
        tlogs = new TableDefinition(s,
            "LOGS"
            , new String[] {
                "ID", "COMPONENT", "USERID", "CONTENT", "TS"
            }
            , new String[] {
                "ID", AppLocal.getIntString("label.component"), AppLocal.getIntString("label.userid"), AppLocal.getIntString("label.content"), AppLocal.getIntString("label.ts")
            }
            , new Datas[] {
                Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
            }
            , new Formats[] {
                Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING
            }
            , new int[] {0}
        );
    }

    public SentenceList getLogsList() {
        return new StaticSentence(s
            , new QBFBuilder(
                "SELECT ID, COMPONENT, USERID, TS FROM LOGS WHERE ?(QBF_FILTER) ORDER BY NAME",
                new String[] {"ID", "COMPONENT", "TS"}
            )
            , new SerializerWriteBasic(
                new Datas[] {
                    Datas.STRING, Datas.STRING, Datas.STRING
                }
            )
            , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        POSLog l = new POSLog(dr.getString(0));
                        l.setComponent(dr.getString(1));
                        l.setUserId(dr.getString(2));
                        l.setTimeStamp(dr.getString(3));
                        return l;
                    }
            }
        );
    }

    public void quickAdd(String id, String component, String uid, String content) throws BasicException {
        new StaticSentence(s
            , "INSERT INTO LOGS (ID, COMPONENT, USERID, CONTENT) VALUES (?, ?, ?, ?)"
            , new SerializerWriteBasic(
                new Datas[] {
                    Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING
                }
            )
        ).exec(new Object[] {id.toString(), component, uid, content});
    }

    public final TableDefinition getTableLogs() {
        return tlogs;
    }
}
