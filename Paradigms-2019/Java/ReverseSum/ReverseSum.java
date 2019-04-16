import java.io.IOException;
import java.util.*;
import java.lang.*;

public class ReverseSum {
    public static void main(String[] args) {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        ArrayList<Integer> sumInLine = new ArrayList<>();
        ArrayList<Integer> sumInColumn = new ArrayList<>();
        int numberOfLine = 0;

        try (FastScanner scanner = new FastScanner(System.in)) {
            while (scanner.hasNextLine()) {
                matrix.add(scanner.splitLineToInteger());
                int sum = 0;
                int numberOfColumn = 0;
                for (int number : matrix.get(numberOfLine)) {
                    if (sumInColumn.size() <= numberOfColumn) {
                        sumInColumn.add(number);
                    } else {
                        sumInColumn.set(numberOfColumn, sumInColumn.get(numberOfColumn) + number);
                    }

                    sum += number;
                    numberOfColumn++;
                }

                sumInLine.add(sum);
                numberOfLine++;
            }
        } catch (IOException e) {
            System.out.print("Scanner was closed.");
        }
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                int number = sumInLine.get(i) + sumInColumn.get(j) - matrix.get(i).get(j);

                System.out.print(number + " ");
            }
            System.out.print("\n");
        }
    }
}
