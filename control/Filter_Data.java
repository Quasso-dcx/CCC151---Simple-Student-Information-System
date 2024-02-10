package control;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/*
 *  Facilitates the filtering/searching of rows based on the input of the user 
 */
public class Filter_Data {
    private static DefaultTableModel def_model;
    private static TableRowSorter<DefaultTableModel> row_sorter;

    private static int row_count = -1;
    public Filter_Data() {
    }

    public static void rowFilter(JTable table, String input_search, int column_index) {
        def_model = (DefaultTableModel) table.getModel();
        row_sorter = new TableRowSorter<DefaultTableModel>(def_model);
        table.setRowSorter(row_sorter);
        table.getRowSorter().toggleSortOrder(0); // to still sort the filtered data based on the first column

        row_sorter.setRowFilter(RowFilter.regexFilter(input_search, column_index));
        row_count = table.getRowCount();
    }

    public static int rowCount(){
        return Filter_Data.row_count;
    }
}
