package sorting.algos;
/**
 * Time complexity of Insertion sort :-
 * Best case : O(n)
 * Average case : O(n2)
 * Worst case : O(n2)
 * @author shekhar
 *
 */
public class InsertionSort {

	public static void main(String[] args) {
		int [] inputArray = {9,8,7,6,5,4,3,2,1};
		InsertionSort sort = new InsertionSort();
		int [] outputArray = sort.doInsertionSort(inputArray);
		for (int i : outputArray) {
			System.out.print(i+", ");
		}
	}

	private int[] doInsertionSort(int[] inputArray) {
		int length = inputArray.length;
		
		for(int i =0;i<length;i++) {
			int value = inputArray[i];
			int position = i;
			while(position >0 && value < inputArray[position-1]) {
				int temp = inputArray[position];
				inputArray[position] = inputArray[position-1];
				inputArray[position-1] = temp;
				-- position; 
			}
			
		}
		
		return inputArray;
	}
}
