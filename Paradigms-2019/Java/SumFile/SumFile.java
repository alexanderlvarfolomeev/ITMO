import java.io.*;
import java.lang.*;

public class SumFile {
    public static void main(String[] args) {
        int res = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line = "";
            do {
                String[] splitLine = line.split("[^0-9+-]");
                for (String number : splitLine) {
                    if (!number.equals("")) {
                        res += Integer.valueOf(number);
                    }
                }
                line = reader.readLine();
            } while (line != null);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])))) {
                writer.write(String.valueOf(res));
            }
        } catch (FileNotFoundException e) {
            System.out.print("Sorry, but file that you wanted was not found");
            System.exit(0);
        } catch (NumberFormatException e) {
            System.out.print("Please, enter numbers only");
            System.exit(0);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("Please, enter names of files");
            System.exit(0);
        } catch (IOException e) {
            System.out.print("File was closed, but program still need it");
            System.exit(0);
        }
    }
}