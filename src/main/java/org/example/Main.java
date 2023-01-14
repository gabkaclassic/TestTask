package org.example;

import org.example.sorting.Solver;
import org.example.testing.GeneratorTestFiles;

import java.io.*;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) {

        startSorting(args);
    }

    private static void startSorting(String[] args) {

        var solver  = new Solver(args);
        try {
            solver.sort();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Internal program error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error when working with files: " + e.getMessage());
        }
    }

    private static void generatingTests(int numberFiles, boolean integerMode, boolean ascOrder) {

        if(integerMode)
            GeneratorTestFiles.generateIntegerFiles(numberFiles, ascOrder);
        else
            GeneratorTestFiles.generateStringFiles(numberFiles, ascOrder);
    }
}