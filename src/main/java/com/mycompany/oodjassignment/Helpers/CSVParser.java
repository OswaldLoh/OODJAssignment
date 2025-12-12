package com.mycompany.oodjassignment.Helpers;

public interface CSVParser<Class> {
    Class fromCSV(String line);     // construct a single line in the text file into an object of the class
    String toCSV();                 // convert the object back into CSV formatted string
    String getFileHeader();         // returns header row used at the start of text file
    String getFilename();           // returns filename that stores the objects of the class
}



