package org.example.sorting;

import org.example.fileManagement.readers.IntegerReader;
import org.example.fileManagement.readers.Reader;
import org.example.fileManagement.readers.StringReader;
import org.example.fileManagement.writers.Writer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Solver {

    private String outputFile;

    private List<String> inputFilesList;  // For ordered access

    private Set<String> inputFilesSet;  // For check for presence in list

    private boolean stringMode;

    private boolean ascOrder;

    public Solver(String[] arguments) {

        ascOrder = true;
        inputFilesList = new ArrayList<>();
        inputFilesSet = new HashSet<>();
        setup(arguments);
        Sorter.setAscOrder(ascOrder);
    }

    public void sort() throws IOException, ExecutionException, InterruptedException {

        var tasks = new ArrayList<FutureTask<String>>();
        var i = 0;

        for(; i < inputFilesList.size() - 1; i += 2) {

            var reader1 = getReader(inputFilesList.get(i));
            var reader2 = getReader(inputFilesList.get(i+1));
            var output = UUID.randomUUID() + ".txt";
            var sorter = new Sorter(reader1, reader2, output);

            FutureTask<String> task = new FutureTask<>(sorter);
            new Thread(task).start();
            tasks.add(task);
        }

        if(i < inputFilesList.size()) {  // For odd number of input files
            FutureTask<String> mockTask = new FutureTask<>(new MockSorter(inputFilesList.get(i)));
            new Thread(mockTask).start();
            tasks.add(mockTask);
        }

        sort(tasks);

        cleaningTemporaryFiles(tasks);
    }

    private void sort(ArrayList<FutureTask<String>> tasks) throws ExecutionException, InterruptedException, IOException {

        if(tasks.size() == 1) {
            writeResult(tasks.get(0).get());
            cleaningTemporaryFiles(tasks);
            return;
        }

        var newTasks = new ArrayList<FutureTask<String>>();
        var i = 0;

        for(; i < tasks.size() - 1; i += 2) {

            var reader1 = getReader(tasks.get(i).get());
            var reader2 = getReader(tasks.get(i+1).get());
            var output = UUID.randomUUID() + ".txt";
            var sorter = new Sorter(reader1, reader2, output);

            FutureTask<String> task = new FutureTask<>(sorter);
            new Thread(task).start();
            newTasks.add(task);
        }

        if(i < tasks.size()) {  // For odd number of input files
            FutureTask<String> mockTask = new FutureTask<>(new MockSorter(tasks.get(i).get()));
            new Thread(mockTask).start();
            newTasks.add(mockTask);
        }

        sort(newTasks);

        cleaningTemporaryFiles(newTasks);
    }

    private Reader getReader(String file) throws FileNotFoundException {
        return (stringMode) ? new StringReader(file)
                : new IntegerReader(file);
    }

    private void cleaningTemporaryFiles(ArrayList<FutureTask<String>> tasks) {
        tasks.stream().map(task -> {  // Deleting temporary files, which were created sorters
            try {
                var file = task.get();

                return (inputFilesSet.contains(file)) ? null : Path.of(file);
            } catch (InterruptedException | ExecutionException e) {
                exitAfterErrorMessage("Internal program error");
            }
            return null;
        }).filter(Objects::nonNull).forEach(path -> {
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                exitAfterErrorMessage("Error when working with files");
            }
        });
    }

    private void setup(String[] arguments) {

        for(var i = 0; i < arguments.length; i++) {

            if(arguments[i].startsWith("-")) {  // Processing of flags

                switch (arguments[i].toLowerCase()) {
                    case "-i":
                        stringMode = false;
                        break;
                    case "-s":
                        stringMode = true;
                        break;
                    case "-d":
                        ascOrder = false;
                        break;
                    case "-a":
                        ascOrder = true;
                        break;
                    default:
                        System.out.printf("Unknown flag: ( %s ) - ignored\n", arguments[i]);
                }
                if(i < arguments.length - 1 && !arguments[i + 1].startsWith("-")) {  // Adding of output file
                    outputFile = getFilePath(arguments[i+1]);
                    i++;
                }
            }
            else {  // Adding of input files
                if(i == 0)
                    exitAfterErrorMessage(("Required any from follow flags: -i, -s"));

                try {
                    checkAndAddInputFile(arguments[i]);
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if(outputFile == null)
            exitAfterErrorMessage("Required one output file");
        if(inputFilesList.size() == 0)
            exitAfterErrorMessage("Required input files");
    }

    private String getFilePath(String argument) {
        return "files/" + argument;
    }

    private void checkAndAddInputFile(String inputFile) throws FileNotFoundException {

        var path = getFilePath(inputFile);
        var reader = getReader(path);

        if(!reader.hasNext()) {
            System.out.println("File of data can't be empty: " + inputFile);
            return;
        }

        reader.close();

        inputFilesList.add(path);
        inputFilesSet.add(path);
    }

    private void writeResult(String resultFile) throws IOException {

        var reader = (stringMode) ? new StringReader(resultFile)
                : new IntegerReader(resultFile);
        var writer = new Writer(outputFile);
        Comparable current = reader.getValue();

        while(current != null) {
            writer.write(current);
            current = reader.next();
        }

        writer.close();
    }
    private static void exitAfterErrorMessage(String message) {
        System.out.println(message);
        System.exit(0);
    }
}
