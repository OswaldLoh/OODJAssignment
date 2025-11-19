package com.mycompany.oodjassignment.functions;

public interface CSVParser<Class> {
    Class fromCSV(String line);
    String toCSV();
    String getFileHeader();
    String getFilename();
}
