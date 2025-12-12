package com.mycompany.oodjassignment.academicperformanceGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.mycompany.oodjassignment.functions.Database;

public class ChartPanel extends JPanel {
    private String chartType;
    private Database database = new Database();
    
    public ChartPanel(String chartType) {
        this.chartType = chartType;
        this.setPreferredSize(new Dimension(1000, 600)); 
    }
    
    public void updateChartType(String newChartType) {
        this.chartType = newChartType;
        repaint();
    }
    
    private void drawGPADistributionChart(Graphics2D g2d) {
        int[] values = database.getGPADistributionData();
        String[] labels = {"A", "B+, B", "C+, C", "D", "F"};
        String title = "Grade Distribution";
        
        drawBarChart(g2d, values, labels, title);
    }
    
    private void drawDepartmentPerformanceChart(Graphics2D g2d) {
        Map<String, Double> deptData = database.getDepartmentPerformanceData();
        
        // Prepare data
        List<Double> gpaValues = new ArrayList<>();
        List<String> deptLabels = new ArrayList<>();
        
        // adding the department and gpa in dept data
        for (Map.Entry<String, Double> entry : deptData.entrySet()) {
            deptLabels.add(entry.getKey());  
            gpaValues.add(entry.getValue()); 
        }
        
        // Sort the data by CGPA descending for better visualization
        List<int[]> sortedData = new ArrayList<>();
        for (int i = 0; i < gpaValues.size(); i++) {
            sortedData.add(new int[]{i, (int) Math.round(gpaValues.get(i) * 100)});
        }
        
        // Sort by value descending
        sortedData.sort((a, b) -> Integer.compare(b[1], a[1])); 
        
        // Convert to sorted arrays
        int[] values = new int[sortedData.size()];
        String[] labels = new String[sortedData.size()];
        
        for (int i = 0; i < sortedData.size(); i++) {
            int originalIndex = sortedData.get(i)[0];
            values[i] = sortedData.get(i)[1];
            labels[i] = deptLabels.get(originalIndex);
        }
        
        drawBarChart(g2d, values, labels, "Department Average GPA");
    }
    
    private void drawCoursePerformanceChart(Graphics2D g2d) {
        Map<String, Double> courseData = database.getCoursePerformanceData();
        
        // Prepare data for ALL courses
        List<Double> gpaValues = new ArrayList<>();
        List<String> courseLabels = new ArrayList<>();
        
        // adding the course and gpa in course data
        for (Map.Entry<String, Double> entry : courseData.entrySet()) {
            courseLabels.add(entry.getKey());  
            gpaValues.add(entry.getValue());   
        }
        
        // Sort the data by GPA descending for better visualization
        List<int[]> sortedData = new ArrayList<>();
        for (int i = 0; i < gpaValues.size(); i++) {
            sortedData.add(new int[]{i, (int) Math.round(gpaValues.get(i) * 100)});
        }
        
        sortedData.sort((a, b) -> Integer.compare(b[1], a[1])); // Sort by value descending
        
        // Convert to sorted arrays
        int[] values = new int[sortedData.size()];
        String[] labels = new String[sortedData.size()];
        
        for (int i = 0; i < sortedData.size(); i++) {
            int originalIndex = sortedData.get(i)[0];
            values[i] = sortedData.get(i)[1];
            labels[i] = courseLabels.get(originalIndex);
        }
        
        drawBarChart(g2d, values, labels, "Course Average GPA");
    }
    
    private void drawSemesterPerformanceChart(Graphics2D g2d) {
        Map<Integer, Double> semesterData = database.getSemesterPerformanceData();
        
        // Prepare data maintaining chronological order
        List<Double> gpaValues = new ArrayList<>();
        List<String> semesterLabels = new ArrayList<>();
        
        // Sort semesters in ascending order (chronological)
        List<Integer> sortedSemesters = new ArrayList<>(semesterData.keySet());
        java.util.Collections.sort(sortedSemesters);
        
        for (Integer semester : sortedSemesters) {
            gpaValues.add(semesterData.get(semester));
            semesterLabels.add("Sem " + semester);
        }
        
        // Convert to arrays
        double[] gpaArray = gpaValues.stream().mapToDouble(Double::doubleValue).toArray();
        String[] labelArray = semesterLabels.toArray(new String[0]);
        
        // Convert to int array for chart (scale up by 100 for better visualization)
        int[] values = new int[gpaArray.length];
        for (int i = 0; i < gpaArray.length; i++) {
            values[i] = (int) Math.round(gpaArray[i] * 100);
        }
        
        drawBarChart(g2d, values, labelArray, "Semester Average GPA");
    }
    
    private void drawBarChart(Graphics2D g2d, int[] values, String[] labels, String title) {
        if (values.length == 0) {
            g2d.drawString("No data available", 350, 250);
            return;
        }
        
        // Find max value for scaling
        int maxValue = 0;
        for (int value : values) {
            if (value > maxValue){
                maxValue = value;
            }
        }

        if (maxValue == 0) {
            maxValue = 1; // Avoid division by zero
        }
        // Set up chart dimensions
        int width = getWidth();
        int height = getHeight();
        
        // Adjust margins based on number of data points for better fit
        int topMargin = 50;
        int rightMargin = 50;
        
        // More flexible bottom margin calculation
        int bottomMargin;
        if (labels.length <= 8) {
            bottomMargin = 120;
        } 

        else if (labels.length <= 15) {
            bottomMargin = 150;
        } 

        else if (labels.length <= 25) {
            bottomMargin = 180;
        } 

        else {
            bottomMargin = Math.min(250, 100 + labels.length * 8); // Cap at reasonable height
        }
        
        // Adjust left margin based on the length of the longest label
        FontMetrics fm = g2d.getFontMetrics();
        int maxLabelWidth = 0;
        for (String label : labels) {

            int labelWidth = fm.stringWidth(label);
            
            if (labelWidth > maxLabelWidth) {
                maxLabelWidth = labelWidth;
            }
        }
        int leftMargin = Math.max(100, 50 + maxLabelWidth); // Make left margin accommodate long labels
        
        int chartWidth = width - leftMargin - rightMargin;
        int chartHeight = height - topMargin - bottomMargin;
        
        // Draw title
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        FontMetrics titleFm = g2d.getFontMetrics();
        int titleWidth = titleFm.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, 30);
        
        // Draw Y-axis labels and grid
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        fm = g2d.getFontMetrics();
        
        int numYLabels = 10;
        for (int i = 0; i <= numYLabels; i++) {
            int yValue = (int) ((double) i / numYLabels * maxValue);
            int y = topMargin + chartHeight - (int) ((double) i / numYLabels * chartHeight);
            
            // Draw grid line
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawLine(leftMargin, y, leftMargin + chartWidth, y);
            
            // Draw Y-axis label
            g2d.setColor(Color.BLACK);
            String label = String.valueOf(yValue / 100.0); // Scale back to actual GPA value
            int labelWidth = fm.stringWidth(label);
            g2d.drawString(label, leftMargin - labelWidth - 5, y + 4);
        }
        
        // Draw X-axis and Y-axis lines
        g2d.setColor(Color.BLACK);
        g2d.drawLine(leftMargin, topMargin, leftMargin, topMargin + chartHeight);
        g2d.drawLine(leftMargin, topMargin + chartHeight, leftMargin + chartWidth, topMargin + chartHeight);
        
        // Calculate bar dimensions to fit all bars properly
        int availableWidth = chartWidth;
        
        // Flexible approach for bar width and spacing based on data count
        int barWidth;
        int spacing;
        
        if (values.length <= 8) {
            // For small datasets, use standard dimensions
            barWidth = 40;
            spacing = Math.max(5, (availableWidth - (barWidth * values.length)) / Math.max(values.length, 1));
        } 

        else if (values.length <= 15) {
            // Medium datasets
            barWidth = 25;
            spacing = 10;
        } 

        else if (values.length <= 30) {
            // Larger datasets
            barWidth = 15;
            spacing = 6;
        } 
        
        else {
            // For very large datasets, use minimal dimensions
            barWidth = Math.max(5, availableWidth / Math.max(values.length, 1));
            spacing = Math.max(1, (availableWidth - (barWidth * values.length)) / Math.max(values.length, 1) / 2);
        }
        
        int totalBarWidth = barWidth * values.length + spacing * (values.length - 1);
        int startX = leftMargin + (availableWidth - totalBarWidth) / 2;
        
        for (int i = 0; i < values.length; i++) {
            // Calculate bar height and position
            int barHeight = (int) ((double) values[i] / maxValue * chartHeight);
            int x = startX + i * (barWidth + spacing);
            int y = topMargin + chartHeight - barHeight;
            
            // Draw bar with color based on value
            Color barColor = getBarColor(i, values.length);
            g2d.setColor(barColor);
            g2d.fillRect(x, y, barWidth, barHeight);
            
            // Draw bar border (only if bar is wide enough to see the border clearly)
            g2d.setColor(Color.BLACK);
            if (barWidth > 3) {
                g2d.drawRect(x, y, barWidth, barHeight);
            } 

            else {
                // For very thin bars, just draw the outline
                g2d.drawLine(x, y, x + barWidth - 1, y);
                g2d.drawLine(x, y, x, y + barHeight);
                g2d.drawLine(x + barWidth - 1, y, x + barWidth - 1, y + barHeight);
                g2d.drawLine(x, y + barHeight, x + barWidth - 1, y + barHeight);
            }
            
            // Draw X-axis label
            String label = labels[i];
            fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            int labelX = x + (barWidth - labelWidth) / 2;
            int labelY = topMargin + chartHeight + 40; // Increase the space between chart and labels
            
            // Special handling for semester performance chart to keep labels horizontal
            if (chartType.equals("Semester Performance") || chartType.equals("GPA Distribution") ||
                (labels.length == 5 && labels[0].equals("A") && labels[1].equals("B+, B"))) {
                // For Semester Performance and GPA Distribution, always display labels horizontally
                g2d.drawString(label, labelX, labelY);
            } 
            
            else {
                // For Department Performance and Course Performance charts, use more readable angle
                if (chartType.equals("Department Performance") || chartType.equals("Course Performance")) {
                    if (labels.length > 4 || labelWidth > 40) {
                        // Use a 45-degree angle for better readability of department/course names
                        double angle = -Math.PI/4; // 45 degrees
                        g2d.rotate(angle, x + barWidth/2, labelY); 
                        g2d.drawString(label, x + barWidth/2 - labelWidth/2, labelY + 10);
                        g2d.rotate(-angle, x + barWidth/2, labelY); 
                    } else {
                        // For fewer items or shorter names, keep horizontal
                        g2d.drawString(label, labelX, labelY);
                    }
                } 
                // For other chart types, use the original rotation logic
                else {
                    // Rotate labels for large datasets or long labels to save space and avoid graph overlap
                    if (labels.length > 6 || labelWidth > 30) {
                        g2d.rotate(-Math.PI/2.2, x + barWidth/2, labelY); 
                        g2d.drawString(label, x + barWidth/2 - labelWidth/2, labelY + 15); // Move down more to avoid overlap
                        g2d.rotate(Math.PI/2.2, x + barWidth/2, labelY); 
                    } 
                    
                    else if (labelWidth > barWidth) {
                        g2d.rotate(-Math.PI/3, x + barWidth/2, labelY);
                        g2d.drawString(label, x + barWidth/2 - labelWidth/2, labelY + 12);  // Adjust position to avoid overlap
                        g2d.rotate(Math.PI/3, x + barWidth/2, labelY);
                    } 
                    
                    else {
                        // For shorter labels, draw horizontally
                        g2d.drawString(label, labelX, labelY);
                    }
                }
            }
            
            // Draw value on top of bar if there's enough space
            if (barHeight > 8 && barWidth > 6) {
                String valueStr = String.valueOf(values[i] / 100.0);
                FontMetrics valueFm = g2d.getFontMetrics();
                int valueWidth = valueFm.stringWidth(valueStr);
                
                if (valueWidth <= barWidth) {
                    // Check if there's enough space to draw the value on top of the bar
                    if (barHeight > 20) { 
                        int valueLabelY = y + barHeight - 5; 
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(valueStr, x + (barWidth - valueWidth) / 2, valueLabelY);
                        g2d.setColor(barColor);
                    } 
                    
                    else {
                        // If bar is not tall enough to put the value inside, put it on top
                        int valueLabelY = y - 5;
                        g2d.setColor(Color.BLACK); 
                        g2d.drawString(valueStr, x + (barWidth - valueWidth) / 2, valueLabelY);
                        g2d.setColor(barColor);
                    }
                }
            }
        }
        
        // Draw legend if needed
        if (chartType.equals("GPA Distribution")) {
            drawGPADistributionLegend(g2d, width - 120, topMargin + 20);
        }
    }
    
    private Color getBarColor(int index, int total) {
        // Generate different colors for each bar
        float hue = (float) index / total;
        return Color.getHSBColor(hue, 0.7f, 0.9f);
    }
    
    private void drawGPADistributionLegend(Graphics2D g2d, int x, int y) {
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        
        String[] grades = {"A", "B+, B", "C+, C", "D", "F"};
        Color[] colors = {
            new Color(77, 175, 74),   // Green
            new Color(152, 78, 163),  // Purple
            new Color(55, 126, 184),  // Blue
            new Color(255, 127, 0),   // Orange
            new Color(228, 26, 28)    // Red
        };
        
        for (int i = 0; i < grades.length; i++) {
            // Draw color box
            g2d.setColor(colors[i]);
            g2d.fillRect(x, y + i * 20, 15, 15);
            
            // Draw border for color box
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y + i * 20, 15, 15);
            
            // Draw label
            g2d.drawString(grades[i], x + 20, y + i * 20 + 12);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        switch (chartType) {
            case "GPA Distribution":
                drawGPADistributionChart(g2d);
                break;
            case "Department Performance":
                drawDepartmentPerformanceChart(g2d);
                break;
            case "Course Performance":
                drawCoursePerformanceChart(g2d);
                break;
            case "Semester Performance":
                drawSemesterPerformanceChart(g2d);
                break;
        }
    }

}