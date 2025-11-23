package com.mycompany.oodjassignment.Eligibility;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Custom cell renderer to highlight eligible and non-eligible students.
 * - Green background for eligible students
 * - Red background for non-eligible students
 */
public class EligibilityCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        // Let the default renderer create the basic component first.
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Column index where eligibility result ("Yes"/"No") is stored.
        int eligibleColIndex = 4;
        Object eligibleValue = table.getValueAt(row, eligibleColIndex);

        // Only change background colours when the row is not selected.
        if (!isSelected) {
            if ("Yes".equals(eligibleValue)) {
                // Light green for eligible students.
                c.setBackground(new Color(200, 255, 200));
            } else if ("No".equals(eligibleValue)) {
                // Light red for non-eligible students.
                c.setBackground(new Color(255, 200, 200));
            } else {
                // Default white if something unexpected.
                c.setBackground(Color.WHITE);
            }
        } else {
            // Use default selection background when row is selected.
            c.setBackground(table.getSelectionBackground());
        }

        return c;
    }
}
