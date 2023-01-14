package org.example.sorting;

import java.util.concurrent.Callable;

public class MockSorter implements Callable<String> {

    private String output;

    public MockSorter(String output) {
        this.output = output;
    }

    @Override
    public String call() {
        return output;
    }
}
