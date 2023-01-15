package org.example.fileManagement.readers;


import java.io.FileNotFoundException;

public class IntegerReader extends Reader<Integer> {

    public IntegerReader(String inputFile) throws FileNotFoundException {
        super(inputFile);
    }

    @Override
    public Integer next() {
        return buffer = (reader.hasNextInt()) ? reader.nextInt() : null;
    }

    @Override
    public boolean hasNext() {

        return reader.hasNextInt();
    }
}

