package com.openbravo.pos.logging;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.JPanelTable;
import javax.swing.ListCellRenderer;

/**
 * Logs panel
 * @author stas
 */
public class JLogsPanel extends JPanelTable {
    private TableDefinition tlogs;
    private LogsView jviewer;

    public JLogsPanel() {}

    public void activate() throws BasicException {
        jviewer.activate();
        super.activate();
    }

    protected void init() {
        DataLogicLogs dlLogs = (DataLogicLogs) app.getBean("com.openbravo.pos.logging.DataLogicLogs");
        tlogs = dlLogs.getTableLogs();
        jviewer = new LogsView(dlLogs);
    }

    @Override
    public EditorRecord getEditor() {
        return jviewer;
    }

    @Override
    public ListProvider getListProvider() {
        return new ListProviderCreator(tlogs);
    }

    @Override
    public SaveProvider getSaveProvider() {
        return null;
    }

    @Override
    public String getTitle() {
        return AppLocal.getIntString("Menu.Logs");
    }

    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tlogs.getRenderStringBasic(new int[]{4}));
    }

    @Override
    public Vectorer getVectorer() {
        return tlogs.getVectorerBasic(new int[]{4});
    }

    @Override
    public ComparatorCreator getComparatorCreator() {
        return tlogs.getComparatorCreator(new int[] {1,4});
    }

}
