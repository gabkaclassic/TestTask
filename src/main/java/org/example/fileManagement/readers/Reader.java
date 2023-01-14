package org.example.fileManagement.readers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public abstract class Reader<T extends Comparable<T>> {

    protected Scanner reader;

    protected T buffer;

    public Reader(String inputFile) throws FileNotFoundException {
        reader = new Scanner(new File(inputFile));
        next();
    }

    public void close() {

        reader.close();
    }
    public abstract T next();

    public abstract boolean hasNext();

    public T getValue() {
        return buffer;
    }
}
