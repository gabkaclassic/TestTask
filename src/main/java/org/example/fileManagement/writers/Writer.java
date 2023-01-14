package org.example.fileManagement.writers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Writer<T> {
    protected PrintWriter writer;

    public Writer(String outputFile) throws IOException {
        writer = new PrintWriter(new PrintWriter(new FileWriter(outputFile, true), true));
    }

    public void write(T outputValue) {

        writer.println(outputValue);
        writer.flush();
    }

    public void close() {

        writer.close();
    }
}
