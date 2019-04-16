import java.io.*;
import java.lang.*;
import java.nio.charset.StandardCharsets;

public class SumHexFile {
    public static void main(String[] args) {
        int numberOfLine = 1;
        int numberOfPosition = 0;
        String inputFile = null, outputFile = null;
        try {
            inputFile = args[0];
            outputFile = args[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("Please, run program as \"java SumHexFile <input.in> <output.out>\"");
        }
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputFile),
                        StandardCharsets.UTF_8))) {
            int result = 0;
            String line = reader.readLine();
            while (line != null) {
                String[] splitLine = line.toLowerCase().split("[\\p{Space}\\p{Z}]");
                for (String number : splitLine)
                    if (!number.isEmpty()) {
                        numberOfPosition++;
                        int exponent;
                        if (number.startsWith("0x")) {
                            number = number.substring(2);
                            exponent = 16;
                        } else {
                            exponent = 10;
                        }
                        result += Long.valueOf(number.substring(number.indexOf('x') + 1), exponent);
                    }
                line = reader.readLine();
                numberOfLine++;
                numberOfPosition = 0;
            }
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(outputFile),
                            StandardCharsets.UTF_8))) {
                writer.write(Integer.toString(result));
            }

        } catch (FileNotFoundException e) {
            System.out.print("Please, create the file " + outputFile);
        } catch (NumberFormatException e) {
            System.out.print("Invalid number in line " + numberOfLine + " at position " + numberOfPosition);
        } catch (IOException e) {
            System.out.print("File was closed, but program still needs it. And it's strange.");
        }
    }
}