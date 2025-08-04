package org.veeva.DP1_Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    /**
     * Reads lines from a text file and returns them as a list of strings.
     * @param filePath Path to the input file.
     * @return List of lines (trimmed).
     */
    public static List<String> readExpectedTitlesFromFile(String filePath) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Writes the given list of strings to a file, one string per line.
     * @param data     List of strings to write.
     * @param filePath Path to the output file.
     */
    public static void writeActualTitlesToFile(List<String> data, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
