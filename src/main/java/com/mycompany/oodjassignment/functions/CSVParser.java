package com.mycompany.oodjassignment.functions;

public interface CSVParser<Class> {
    Class fromCSV(String line);
}
