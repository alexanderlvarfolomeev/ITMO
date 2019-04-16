package search;

public class BinarySearchMissing {

    public static void main(String[] args) {
        int searchedElement;
        int[] elements;
        //Pre: args[0] is correct integer number ∧ args[0] - searched element of array
        searchedElement = Integer.parseInt(args[0]);
        //Post: searchedElement = key

        //Pre: args.length > 1
        elements = new int[args.length - 1];
        //Post: elements.length = args.length - 1

        //Pre: ∀i∈[1; args.length) args[i] is correct integer number ∧ ∀i∈[2; args.length) args[i - 1] ≥ args[i]
        for (int i = 0; i < elements.length; i++) {
            elements[i] = Integer.parseInt(args[i + 1]);
        }
        //Post: elements - sorted entered array

        //Pre: Inv is saved
        int searchedIndex = recursiveBinarySearch(elements, searchedElement, -1, elements.length);
        //int searchedIndex = iterativeBinarySearch(elements.length);

        //Post: searchedIndex - searched index

        //Pre: searchedIndex ∈ [0; elements.length]
        if (searchedIndex < elements.length && elements[searchedIndex] == searchedElement) {
            //Pre: searchedIndex∈I ∧ A[searchedIndex] = key
            System.out.print(searchedIndex);
            //Post: searchedIndex was printed
        } else {
            //Pre: ¬∃i∈I ∧ A[i] = key ⇒ searchedIndex: A[searchedIndex-1] > key ∧ A[searchedIndex] < key
            System.out.print(-(searchedIndex + 1));
            //Post: (- searchedIndex - 1) was printed
        }
        //Post: searched index was printed
    }

    // NB. elements[searchedIndex] ≤ key
    // NB. elements[searchedIndex - 1] > key

    //Inv:
    // searchedIndex∈(leftBound; rightBound]
    // leftBound < rightBound
    private static int recursiveBinarySearch(int[] elements, int key, int leftBound, int rightBound) {
        //Pre: Inv
        if (leftBound + 1 == rightBound) {
            //leftBound + 1 = rightBound ∧ Inv ⇒ rightBound = searchedIndex
            return rightBound;
        }
        //Post: leftBound + 1 < rightBound

        //Pre: leftBound + 2 ≤ rightBound
        int middle = (leftBound + rightBound) / 2;
        //Post: leftBound < middle < rightBound

        //Pre: Inv
        if (elements[middle] <= key) {
            //Pre: elements - sorted array ∧ elements[middle] ≤ key ⇒ ∀i∈[middle; rightBound] elements[i] ≤ key
            return recursiveBinarySearch(elements, key, leftBound, middle);
            //Post1: ∀i∈[middle; rightBound] elements[i] ≤ key ∧ Inv ⇒ searchedIndex∈(leftBound; middle]
            //Post2: middle < rightBound ⇒ (new)leftBound < (new)rightBound
        } else {
            //Pre: elements - sorted array ∧ elements[middle] > key ⇒ ∀i∈(leftBound; middle] elements[i] > key
            return recursiveBinarySearch(elements, key, middle, rightBound);
            //Post1: ∀i∈(leftBound; middle] elements[i] > key ∧ Inv ⇒ searchedIndex∈(middle; rightBound]
            //Post3: middle < rightBound ⇒ (new)leftBound < (new)rightBound
        }
        //Post: Post1 ∧ Post2 ⇒ Inv was saved
    }

    // NB. elements[searchedIndex] ≤ key
    // NB. elements[searchedIndex - 1] > key
    private static int iterativeBinarySearch(int[] elements, int key, int rightBound) {
        //Pre: ∀i∈[0; -1] elements[i] > key
        int leftBound = -1;
        //Post: ∀i∈[0; leftBound] elements[i] > key

        //Inv:
        // searchedIndex∈(leftBound; rightBound]
        // leftBound < rightBound
        while (leftBound + 1 != rightBound) {
            ////Pre: leftBound + 2 ≤ rightBound
            int middle = (leftBound + rightBound) / 2;
            //Post: leftBound < middle < rightBound

            //Pre: Inv
            if (elements[middle] <= key) {
                //Pre: elements - sorted array ∧ elements[middle] ≤ key ⇒ ∀i∈[middle; rightBound] elements[i] ≤ key
                rightBound = middle;
                //Post1: ∀i∈[middle; rightBound] elements[i] ≤ key ∧ Inv ⇒ searchedIndex∈(leftBound; middle]
                //Post2: middle < rightBound ⇒ (new)leftBound < (new)rightBound
            } else {
                //Pre: elements - sorted array ∧ elements[middle] > key ⇒ ∀i∈(leftBound; middle] elements[i] > key
                leftBound = middle;
                //Post1: ∀i∈(leftBound; middle] elements[i] > key ∧ Inv ⇒ searchedIndex∈(middle; rightBound]
                //Post3: middle < rightBound ⇒ (new)leftBound < (new)rightBound
            }
            //Post: Post1 ∧ Post2 ∧ Post3 ⇒ Inv was saved
        }

        //leftBound + 1 = rightBound ∧ Inv ⇒ rightBound = searchedIndex
        return rightBound;
    }
}