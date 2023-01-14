package org.example.sorting;

import org.example.fileManagement.readers.Reader;
import org.example.fileManagement.writers.Writer;

import java.io.IOException;
import java.util.concurrent.Callable;

public class Sorter implements Callable<String> {

    private Reader reader1;

    private Reader reader2;

    private Writer writer;

    private String output;

    private static boolean ascOrder;

    public Sorter(Reader reader1, Reader reader2, String output) throws IOException {
        this.reader1 = reader1;
        this.reader2 = reader2;
        writer = new Writer(output);
        this.output = output;
    }

    @Override
    public String call() throws IllegalStateException {

        var value1 = reader1.getValue();
        var value2 = reader2.getValue();

        do {

            if(lessThanWithOrder(value1, value2)) {
                writer.write(value1);
                value1 = skip(reader1, value1);
            } else {
                writer.write(value2);
                value2 = skip(reader2, value2);
            }

        } while(value1 != null && value2 != null);

        while(value1 != null) {
            writer.write(value1);
            value1 = skip(reader1, value1);
        }
        while(value2 != null) {
            writer.write(value2);
            value2 = skip(reader2, value2);
        }

        reader1.close();
        reader2.close();

        return output;
    }

    private Comparable skip(Reader reader, Comparable value) {  // Skip incorrect data from file
        var temp = reader.next();
        while(temp != null && lessThanWithOrder(temp, value))
            temp = reader.next();
        return temp;
    }

    static boolean lessThanWithOrder(Comparable value1, Comparable value2) {

        return (ascOrder) ? value1.compareTo(value2) <= 0 : value1.compareTo(value2) >= 0;
    }

    public static void setAscOrder(boolean ascOrder) {
        Sorter.ascOrder = ascOrder;
    }
}
