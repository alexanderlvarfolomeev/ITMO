import java.lang.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordStatWords {
    public static void main(String[] args) {
        TreeMap<String, Integer> mapOfWords = new TreeMap<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(args[0]),
                        StandardCharsets.UTF_8))) {
            String line = "";
            do {
                String[] splitLine = line.toLowerCase().split("[^\\p{L}\\p{Pd}']");
                for (String word : splitLine) {
                    if (!word.equals("")) {
                        if (mapOfWords.containsKey(word)) {
                            mapOfWords.put(word, mapOfWords.get(word) + 1);
                        } else {
                            mapOfWords.put(word, 1);
                        }
                    }
                }
                line = reader.readLine();
            } while (line != null);
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(new File(args[1])),
                            StandardCharsets.UTF_8))) {
                while (!mapOfWords.isEmpty()) {
                    writer.write(mapOfWords.firstKey() + " "
                            + mapOfWords.get(mapOfWords.firstKey()) + "\r\n");
                    mapOfWords.remove(mapOfWords.firstKey());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.print("Sorry, but file that you wanted was not found");
        } catch (NumberFormatException e) {
            System.out.print("Please, enter valid numbers only");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("Please, enter names of files");
        } catch (IOException e) {
            System.out.print("File was closed, but program still need it");
        }
    }
}