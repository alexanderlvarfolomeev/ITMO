import java.util.*;
import java.lang.*;

public class ReverseMax {
    public static void main(String[] args) {
        ArrayList<Integer> listOfLineSizes = new ArrayList<>();
        ArrayList<Integer> listOfMaxInLine = new ArrayList<>();
        ArrayList<Integer> listOfMaxInColumn = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        int maximum;
        do {
            String comString = scan.nextLine();
            Scanner scanOfString = new Scanner(comString);
            maximum = Integer.MIN_VALUE;
            int inputNumber;
            int i = -1;
            while (scanOfString.hasNextInt()) {
                i++;
                inputNumber = scanOfString.nextInt();
                if (listOfMaxInColumn.size() < i + 1) {
                    listOfMaxInColumn.add(inputNumber);
                } else {
                    listOfMaxInColumn.set(i, Math.max(listOfMaxInColumn.get(i), inputNumber));
                }
                maximum = Math.max(maximum, inputNumber);
            }
            listOfMaxInLine.add(maximum);
            listOfLineSizes.add(i + 1);
        } while (scan.hasNextLine());

        for (int i = 0; i < listOfLineSizes.size(); i++) {
            for (int j = 0; j < listOfLineSizes.get(i); j++) {
                maximum = Math.max(listOfMaxInLine.get(i), listOfMaxInColumn.get(j));
                System.out.print(maximum + " ");
            }
            System.out.print("\n");
        }
    }
}
