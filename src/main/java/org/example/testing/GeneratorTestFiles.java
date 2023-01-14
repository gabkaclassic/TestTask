package org.example.testing;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class GeneratorTestFiles {

    private static final Random random = new Random();

    public static void generateIntegerFiles(int numberFiles, boolean ascOrder) {

        for (int i = 0; i < numberFiles; i++) {

            if(ascOrder)
                generateIntegerFileAsc("in" + i + ".txt");
            else
                generateIntegerFileDesc("in" + i + ".txt");
        }
    }

    public static void generateStringFiles(int numberFiles, boolean ascOrder) {

        for (int i = 0; i < numberFiles; i++) {

            if(ascOrder)
                generateStringFileAsc("in" + i + ".txt");
            else
                generateStringFileDesc("in" + i + ".txt");
        }
    }

    private static void generateIntegerFileAsc(String filename) {

        int upperBound = Integer.MAX_VALUE - random.nextInt(1000);
        int current = Integer.MIN_VALUE + random.nextInt(1000);

        try(var writer = new PrintWriter(new FileWriter(filename, true), true)) {

            while(current < upperBound)
                writer.println(current += random.nextInt(100));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateIntegerFileDesc(String filename) {

        int lowerBound = Integer.MIN_VALUE + random.nextInt(1000);
        int current = Integer.MAX_VALUE - random.nextInt(1000);

        try(var writer = new PrintWriter(new FileWriter(filename, true), true)) {

            while(current > lowerBound)
                writer.println(current -= random.nextInt(100));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateStringFileAsc(String filename) {

        int upperBound = Integer.MAX_VALUE - random.nextInt(100);
        int current = random.nextInt(100);

        try(var writer = new PrintWriter(new FileWriter(filename, true), true)) {

            while(current < upperBound) {
                writer.write(current += random.nextInt(10));
                writer.println();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void generateStringFileDesc(String filename) {

        int lowerBound = random.nextInt(100);
        int current = Integer.MAX_VALUE - random.nextInt(100);

        try(var writer = new PrintWriter(new FileWriter(filename, true), true)) {

            while(current > lowerBound) {
                writer.write(current -= random.nextInt(10));
                writer.println();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
