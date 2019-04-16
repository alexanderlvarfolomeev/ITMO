import java.lang.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.*;

public class WordStatLineIndex {
    public static void main(String[] args) {

        String inputFile = null, outputFile = null;

        try {
            inputFile = args[0];
            outputFile = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("Usage: \"java SumHexFile <inputFileName> <output.out>\"");
        }

        try (FastScanner reader = new FastScanner(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8))) {
            TreeMap<String, ArrayList<Map.Entry<Integer, Integer>>> wordsAndPositions = new TreeMap<>();
            int numberOfLine = 0;
            String line;

            while ((line = reader.nextLine()) != null) {

                int position = 0;
                numberOfLine++;
                FastScanner lineReader = new FastScanner(line);
                String word;

                while ((word = lineReader.nextWord()) != null) {
                    position++;
                    if (!wordsAndPositions.containsKey(word)) {
                        wordsAndPositions.put(word, new ArrayList<>());
                    }
                    wordsAndPositions.get(word).add(new AbstractMap.SimpleEntry<>(numberOfLine, position));
                }
            }

            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(new File(outputFile)), StandardCharsets.UTF_8))) {
                wordsAndPositions.forEach((word, positions) -> {
                    try {
                        writer.write(word + " " + positions.size());
                        for (Map.Entry<Integer, Integer> position : positions) {
                            writer.write(" " + position.getKey() + ':' + position.getValue());
                        }
                        writer.write("\r\n");
                    } catch (IOException e) {
                        System.out.print("File has been closed, but program still needs it. And it's strange.");
                    }
                });
            } catch (NoSuchFileException e) {
                e.printStackTrace();
                System.out.print("There is not enough memory to create the output file.");
            }

        } catch (
                FileNotFoundException e) {
            System.out.print("Please, create the file " + inputFile);
        } catch (
                IOException e) {
            System.out.print("Not all bytes read in input stream.");
        }
    }
}