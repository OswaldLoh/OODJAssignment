package com.mycompany.oodjassignment.reportgeneratorGUI;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import com.mycompany.oodjassignment.classes.Course;
import com.mycompany.oodjassignment.classes.Grades;
import com.mycompany.oodjassignment.classes.Student;
import com.mycompany.oodjassignment.functions.Database;

public class SwingChartPanel extends JPanel {
    private String chartType;
    private Database database = new Database();
    
    public SwingChartPanel(String chartType) {
        this.chartType = chartType;
        this.setPreferredSize(new Dimension(1000, 600)); // Even larger size for better display
    }
    
    public void updateChartType(String newChartType) {
        this.chartType = newChartType;
        repaint();
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
    
    private void drawGPADistributionChart(Graphics2D g2d) {
        // Get all grades
        HashMap<String, Grades> gradeDB = database.getGradeDB();
        
        // Count grades in different GPA ranges
        int gradeA = 0, gradeB = 0, gradeC = 0, gradeD = 0, gradeF = 0;
        
        for (Grades grade : gradeDB.values()) {
            grade.setCourseObject(database.getCourse(grade.getCourseID()));
            String letterGrade = grade.getLetterGrade();
            
            switch (letterGrade) {
                case "A":
                case "A-":
                    gradeA++;
                    break;
                case "B+":
                case "B":
                case "B-":
                    gradeB++;
                    break;
                case "C+":
                case "C":
                    gradeC++;
                    break;
                case "D":
                    gradeD++;
                    break;
                case "F":
                    gradeF++;
                    break;
            }
        }
        
        int[] values = {gradeA, gradeB, gradeC, gradeD, gradeF};
        String[] labels = {"A & A-", "B+, B, B-", "C+, C", "D", "F"};
        String title = "Grade Distribution";
        
        drawBarChart(g2d, values, labels, title);
    }
    
    private void drawDepartmentPerformanceChart(Graphics2D g2d) {
        // Group students by department/major and calculate statistics
        HashMap<String, List<Student>> studentsByMajor = new HashMap<>();
        HashMap<String, Student> studentDB = database.getStudentDB();
        
        for (Student student : studentDB.values()) {
            String major = student.getMajor();
            studentsByMajor.computeIfAbsent(major, k -> new ArrayList<>()).add(student);
        }
        
        // Prepare data
        java.util.List<Double> gpaValues = new ArrayList<>();
        java.util.List<String> deptLabels = new ArrayList<>();
        
        for (Map.Entry<String, List<Student>> entry : studentsByMajor.entrySet()) {
            String major = entry.getKey();
            List<Student> students = entry.getValue();
            
            // Calculate true department CGPA by considering all grades from all students in the department
            double totalQualityPoints = 0.0;
            int totalCreditHours = 0;
            
            for (Student student : students) {
                List<Grades> studentGrades = database.getStudentAllGrades(student.getStudentID());
                
                for (Grades grade : studentGrades) {
                    Course course = database.getCourse(grade.getCourseID());
                    if (course != null) {
                        grade.setCourseObject(course);
                        
                        // Get the GPA for this specific grade/course
                        double courseGPA = grade.calculateGPA();
                        
                        // Get the credit hours for the course
                        int creditHours = course.getCredit();
                        
                        // Add to total quality points (GPA * credit hours)
                        totalQualityPoints += courseGPA * creditHours;
                        totalCreditHours += creditHours;
                    }
                }
            }
            
            // Calculate the true department CGPA
            double deptCGPA = (totalCreditHours > 0) ? totalQualityPoints / totalCreditHours : 0.0;
            
            if (totalCreditHours > 0) { // Only add departments with valid data
                gpaValues.add(deptCGPA);
                deptLabels.add(major);
            }
        }
        
        // Sort the data by CGPA descending for better visualization
        java.util.List<int[]> sortedData = new java.util.ArrayList<>();
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
            labels[i] = deptLabels.get(originalIndex);
        }
        
        drawBarChart(g2d, values, labels, "Department Performance");
    }
    
    private void drawCoursePerformanceChart(Graphics2D g2d) {
        // Get grades by course
        HashMap<String, List<Grades>> gradesByCourse = new HashMap<>();
        HashMap<String, Grades> gradeDB = database.getGradeDB();
        
        for (Grades grade : gradeDB.values()) {
            String courseId = grade.getCourseID();
            gradesByCourse.computeIfAbsent(courseId, k -> new ArrayList<>()).add(grade);
        }
        
        // Prepare data for ALL courses
        java.util.List<Double> gpaValues = new ArrayList<>();
        java.util.List<String> courseLabels = new ArrayList<>();
        
        for (Map.Entry<String, List<Grades>> entry : gradesByCourse.entrySet()) {
            String courseId = entry.getKey();
            List<Grades> grades = entry.getValue();
            
            Course course = database.getCourse(courseId);
            if (course == null) continue; // Skip if course not found
            
            // Calculate average GPA for this course
            double totalGPA = 0.0;
            for (Grades grade : grades) {
                grade.setCourseObject(course);
                totalGPA += grade.calculateGPA();
            }
            
            double avgGPA = grades.size() > 0 ? totalGPA / grades.size() : 0.0;
            
            gpaValues.add(avgGPA);
            courseLabels.add(courseId);
        }
        
        // Sort the data by GPA descending for better visualization
        java.util.List<int[]> sortedData = new java.util.ArrayList<>();
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
        // Group grades by semester and calculate statistics
        Map<Integer, List<Grades>> gradesBySemester = new HashMap<>();
        HashMap<String, Grades> gradeDB = database.getGradeDB();
        
        for (Grades grade : gradeDB.values()) {
            int semester = grade.getSemester();
            gradesBySemester.computeIfAbsent(semester, k -> new ArrayList<>()).add(grade);
        }
        
        // Prepare data maintaining chronological order
        java.util.List<Double> gpaValues = new ArrayList<>();
        java.util.List<String> semesterLabels = new ArrayList<>();
        
        // Sort semesters in ascending order (chronological)
        List<Integer> sortedSemesters = new ArrayList<>(gradesBySemester.keySet());
        java.util.Collections.sort(sortedSemesters);
        
        for (Integer semester : sortedSemesters) {
            List<Grades> grades = gradesBySemester.get(semester);
            
            // Calculate average GPA for this semester
            double totalGPA = 0.0;
            for (Grades grade : grades) {
                Course course = database.getCourse(grade.getCourseID());
                if (course != null) {
                    grade.setCourseObject(course);
                    totalGPA += grade.calculateGPA();
                }
            }
            
            double avgGPA = grades.size() > 0 ? totalGPA / grades.size() : 0.0;
            
            gpaValues.add(avgGPA);
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
            if (value > maxValue) maxValue = value;
        }
        if (maxValue == 0) maxValue = 1; // Avoid division by zero
        
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
        } else if (labels.length <= 15) {
            bottomMargin = 150;
        } else if (labels.length <= 25) {
            bottomMargin = 180;
        } else {
            bottomMargin = Math.min(250, 100 + labels.length * 8); // Cap at reasonable height
        }
        
        // Adjust left margin based on the length of the longest label
        FontMetrics fm = g2d.getFontMetrics();
        int maxLabelWidth = 0;
        for (String label : labels) {
            int labelWidth = fm.stringWidth(label);
            if (labelWidth > maxLabelWidth) maxLabelWidth = labelWidth;
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
        } else if (values.length <= 15) {
            // Medium datasets
            barWidth = 25;
            spacing = 10;
        } else if (values.length <= 30) {
            // Larger datasets
            barWidth = 15;
            spacing = 6;
        } else {
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
            } else {
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
            
            // Special handling for GPA Distribution chart to keep labels horizontal
            if (chartType.equals("GPA Distribution") || 
                (labels.length == 5 && labels[0].equals("A & A-") && labels[1].equals("B+, B, B-"))) {
                // For GPA Distribution, always display labels horizontally
                g2d.drawString(label, labelX, labelY);
            } else {
                // Rotate labels for large datasets or long labels to save space and avoid graph overlap
                if (labels.length > 6 || labelWidth > 30) { // Also consider label length for rotation
                    // Use a steeper angle and position the label to avoid graph area
                    g2d.rotate(-Math.PI/2.2, x + barWidth/2, labelY); // Steeper rotation (about 81 degrees) - almost vertical
                    g2d.drawString(label, x + barWidth/2 - labelWidth/2, labelY + 15); // Move down more to avoid overlap
                    g2d.rotate(Math.PI/2.2, x + barWidth/2, labelY); // Rotate back
                } else if (labelWidth > barWidth) {
                    // For medium-sized datasets with wide labels, use 60-degree rotation
                    g2d.rotate(-Math.PI/3, x + barWidth/2, labelY);
                    g2d.drawString(label, x + barWidth/2 - labelWidth/2, labelY + 12); // Move down more to avoid overlap
                    g2d.rotate(Math.PI/3, x + barWidth/2, labelY);
                } else {
                    // For shorter labels, draw horizontally
                    g2d.drawString(label, labelX, labelY);
                }
            }
            
            // Draw value on top of bar if there's enough space
            if (barHeight > 8 && barWidth > 6) {
                String valueStr = String.valueOf(values[i] / 100.0);
                FontMetrics valueFm = g2d.getFontMetrics();
                int valueWidth = valueFm.stringWidth(valueStr);
                if (valueWidth <= barWidth) {
                    g2d.drawString(valueStr, x + (barWidth - valueWidth) / 2, Math.max(y - 2, topMargin + 5));
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
        
        String[] grades = {"A & A-", "B+, B, B-", "C+, C", "D", "F"};
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
}