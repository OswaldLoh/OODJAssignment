package com.mycompany.oodjassignment.functions;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableSorter {
    private TableRowSorter<TableModel> rowSorter;

    public TableSorter (DefaultTableModel tableModel, JTable table) {
        this.rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(this.rowSorter);
    }

    public void sortTable (int column, String type) {
        int index;
        if (type.equals("ID")) {
            index = 1;
        } else {
            index = 0;
        }
        this.rowSorter.setComparator(column, (a, b) -> {
            int n1 = Integer.parseInt(a.toString().substring(index));
            int n2 = Integer.parseInt(b.toString().substring(index));
            return Integer.compare(n1, n2);
        });
    }
}