import java.io.*;
import java.lang.*;

public class SumAbcFile {
    public static void main(String[] args) {
        int res = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line = "";
            do {
                String[] splitLine = line.toLowerCase().split("[^a-j+-]");
                for (String number : splitLine) {
                    for (char ch = 'a'; ch < 'k'; ch++) {
                        number = number.replace(ch, (char) (ch - 'a' + '0'));
                    }
                    if (!number.isEmpty()) res += Integer.valueOf(number);
                }
                line = reader.readLine();
            } while (line != null);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args[1])))) {
                writer.write("" + res);
            }
        } catch (FileNotFoundException e) {
            System.out.print("Sorry, but file that you wanted was not found");
        } catch (NumberFormatException e) {
            System.out.print("Please, enter numbers only");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.print("Please, enter names of files");
        } catch (IOException e) {
            System.out.print("File was closed, but program still need it");
        }
    }
}
