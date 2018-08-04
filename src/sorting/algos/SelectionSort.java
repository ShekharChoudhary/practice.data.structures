package sorting.algos;

public class SelectionSort {

	public static void main(String[] args) {
		int [] inputArray = {2,3,6,7,12,9,1,13,-1,0};
		SelectionSort sort = new SelectionSort();
		int [] sortedArray = sort.doSelectionSort(inputArray);
		for (int i : sortedArray) {
			System.out.print(i+", ");
		}
	}

	private int[] doSelectionSort(int[] inputArray) {

		int arrLength = inputArray.length;
		int temp =0;
		for(int i =0;i< arrLength-1;i++) {
			int minNumber =inputArray[i];
			
			for(int j =i+1;j<arrLength;j++) {
			if(minNumber>inputArray[j]) {
				temp = inputArray[i];
				inputArray[i]= inputArray[j];
				inputArray[j]= temp;
				minNumber =inputArray[i];
			}
			}
		}
		
		return inputArray;
	}
}
