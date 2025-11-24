package com.mycompany.oodjassignment.Eligibility;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Custom cell renderer used to highlight eligible students in green
 * and non-eligible students in light red. Helps lecturers visually
 * identify students who need attention.
 */
public class EligibilityCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        int eligibleCol = 4; // "Eligible" column index
        Object eligibleValue = table.getValueAt(row, eligibleCol);

        // Only apply custom colouring when not selected
        if (!isSelected) {
            if ("Yes".equals(eligibleValue)) {
                c.setBackground(new Color(200, 255, 200)); // green for eligible
            } else if ("No".equals(eligibleValue)) {
                c.setBackground(new Color(255, 210, 210)); // red for not eligible
            } else {
                c.setBackground(Color.WHITE);
            }
        } else {
            c.setBackground(table.getSelectionBackground());
        }

        return c;
    }
}
