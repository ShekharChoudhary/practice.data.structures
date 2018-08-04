package sorting.algos;
/**
 * Time complexity of Bubble sort :-
 * Best case : O(n)
 * Average case : O(n2)
 * Worst case : O(n2)
 * @author shekhar
 *
 */
public class BubbleSort {

	public static void main(String[] args) {
		int[] inputArray = { 9, 8, 7, 6, 1,2,3,4,5 };
		BubbleSort sort = new BubbleSort();
		int[] outputArray = sort.doBubbleSort(inputArray);
		for (int i : outputArray) {
			System.out.print(i + ",");
		}
	}

	private int[] doBubbleSort(int[] inputArray) {

		int length = inputArray.length;
		int temp = 0;
		for (int i = 1; i < length; i++) {
			boolean flag = true;
			for (int j = 0; j < length - (i); j++) {
				if (inputArray[j] > inputArray[j + 1]) {
					temp = inputArray[j];
					inputArray[j] = inputArray[j + 1];
					inputArray[j + 1] = temp;
					flag = false;
				}
			}
			// This is to remove redundant iteration. If the array gets sorted in average case then break the loop.
			if(flag) {
				break;
			}
		}
		return inputArray;
	}
}
