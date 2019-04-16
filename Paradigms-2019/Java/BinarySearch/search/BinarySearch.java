package search;

public class BinarySearch {
    private static int searchedElement;
    private static int[] elements;

    public static void main(String[] args) {
        // args[0] - x
        // A - array
        // I = [0; args.length - 1] ⋂ ℕ = [0; A.length] ⋂ ℕ | I - set of indexes
        // ∀i∈I: args[i + 1]∈A
        // ∀i∈I\{0}: A[i - 1] ≥ A[i]

        //Pre: args[0] = x in String format
        searchedElement = Integer.parseInt(args[0]);
        //Post: searchedElement = x as integer

        //Pre: args.length = A.length + 1
        elements = new int[args.length - 1];
        //Post: element.length = A.length

        //Pre: ∀i∈I: A[i] = args[i + 1]
        for (int i = 0; i < elements.length; i++) {
            //Pre: A[i] = args[i + 1]
            elements[i] = Integer.parseInt(args[i + 1]);
            //Post: elements[i] = A[i]
        }
        //Post: ∀i∈I: A[i] = args[i + 1] ∧ args[i + 1] = elements[i] ⇒ ∀i∈I: A[i] = elements[i] ⇒ elements = A

        //Pre: 1: NB: k∉I ⇒ k = min(ℕ\I) = A.length;
        //Pre: 2: NB: k∈I ⇒ A[max(I)] ≤ x
        //Pre: 1 ∧ 2 ⇒ ∀k A[A.length] ≤ x
        //Pre: let be A[-1] > x; if leftBound ≥ -1 then rightBound > -1 ⇒ k ≥ 0 ∀A
        //Pre: Inv is saved

        System.out.print(recursiveBinarySearch(-1, elements.length));
        //System.out.print(iterativeBinarySearch(elements.length));

        //Post: searched index was printed
    }

    // NB:
    // 1: (∃k: A[k] ≤ x) ∧ (∀i∈I\{0}: A[i - 1] ≥ A[i]) ⇒ ∀i ≥ k  A[i] ≤ x
    // 2: (∃k: A[k] > x) ∧ (∀i∈I\{0}: A[i - 1] ≥ A[i]) ⇒ ∀i ≤ k  A[i] > x
    // 3: 1 ∧ 2 ⇒ ∃k: ∀i ≥ k  A[i] ≤ x; ∀i < k  A[i] > x
    // k ≥ 0 because (I ≥ 0) ∧ (∃k < 0: ∀i ≥ k  A[i] ≤ x) ⇒ ∀i ≥ 0  A[i] ≤ x ⇒ k = 0
    // ∃k∈I ⇒ ∃ A[k] ≤ x ⇒ k - searched index
    // k∉I ⇒ k > max(I)
    // k > max(I) ⇒ ∀i∈I A[i] > x ⇒ searched index doesn't exist
    private static boolean conditionIsCorrect(int index) {
        return elements[index] <= searchedElement;
    }

    //Inv:
    // A[leftBound] > x ∧ A[rightBound] ≤ x
    // leftBound < rightBound
    private static int recursiveBinarySearch(int leftBound, int rightBound) {
        if (leftBound + 1 == rightBound) {
            //Pre: leftBound + 1 = rightBound ⇒ ¬∃ i∈(leftBound; rightBound)
            return rightBound;
            //Post: Inv ∧ ¬∃ i∈(leftBound; rightBound) ⇒ rightBound - searched index
        }

        //Pre: leftBound + 1 < rightBound ⇒ leftBound + 2 ≤ rightBound
        int middle = (leftBound + rightBound) / 2;
        //Post: leftBound + 2 ≤ rightBound ⇒ leftBound < middle < rightBound

        //Pre: k ∈ I ⋂ [leftBound; rightBound]
        if (conditionIsCorrect(middle)) {
            //Pre: A[middle] ≤ x ∧ NB.3 ⇒ k ≤ middle
            return recursiveBinarySearch(leftBound, middle);
            //Post: leftBound < middle < rightBound ⇒ [leftBound; middle] ⊊ [leftBound; rightBound]
            //Post: middle > leftBound ⇒ (new)leftBound < (new)rightBound
            //Post: Inv was saved
        } else {
            //Pre: A[middle] > x ∧ NB.3 ⇒ k > middle
            return recursiveBinarySearch(middle, rightBound);
            //Post: leftBound < middle < rightBound ⇒ [middle; rightBound] ⊊ [leftBound; rightBound]
            //Post: middle < rightBound ⇒ (new)leftBound < (new)rightBound
            //Post: Inv was saved
        }
        //Post: I ⋂ [leftBound; rightBound] is finite and decreasing ⇒ ∃ step of recursion: leftBound + 1 = rightBound
    }

    private static int iterativeBinarySearch(int rightBound) {
        //Pre: A[-1] > x
        int leftBound = -1;
        //Post: A[leftBound] > x

        //Inv:
        // a[leftBound] > x ∧ a[rightBound] <= x
        // leftBound < rightBound
        while (leftBound + 1 != rightBound) {
            //Pre: leftBound + 1 < rightBound ⇒ leftBound + 2 ≤ rightBound
            int middle = (leftBound + rightBound) / 2;
            //Post: leftBound + 2 ≤ rightBound ⇒ leftBound < middle < rightBound

            //Pre: k ∈ I ⋂ [leftBound; rightBound]
            if (conditionIsCorrect(middle)) {
                //Pre: A[middle] ≤ x ∧ NB.3 ⇒ k ≤ middle
                rightBound = middle;
                //Post: leftBound < middle < rightBound ⇒ [leftBound; middle] ⊊ [leftBound; rightBound]
                //Post: middle > leftBound ⇒ (new)leftBound < (new)rightBound
                //Post: Inv was saved
            } else {
                //Pre: A[middle] > x ∧ NB.3 ⇒ k > middle
                leftBound = middle;
                //Post: leftBound < middle < rightBound ⇒ [middle; rightBound] ⊊ [leftBound; rightBound]
                //Post: middle < rightBound ⇒ (new)leftBound < (new)rightBound
                //Post: Inv was saved
            }
            //Post: I ⋂ [leftBound; rightBound] is finite and decreasing ⇒
            // ⇒ ∃ iteration of loop: leftBound + 1 = rightBound
        }
        //Pre: leftBound + 1 = rightBound ⇒ ¬∃ i∈(leftBound; rightBound)
        return rightBound;
        //Post: Inv ∧ ¬∃ i∈(leftBound; rightBound) ⇒ rightBound - searched index
    }
}