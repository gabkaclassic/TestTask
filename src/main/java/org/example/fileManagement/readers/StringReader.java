package org.example.fileManagement.readers;

import java.io.FileNotFoundException;

public class StringReader extends Reader<String> {
    public StringReader(String inputFile) throws FileNotFoundException {
        super(inputFile);
    }
    @Override
    public String next() {

    do {
        buffer = (reader.hasNextLine()) ? reader.nextLine() : null;

        if(buffer != null && buffer.contains(" "))
            System.out.println("Strings can't contains whitespaces: " + buffer);

    } while(buffer != null && buffer.contains(" "));

        return buffer;
    }

    @Override
    public boolean hasNext() {
        return reader.hasNextLine();
    }
}
