package sorting.algos;

public class MergeSort {

	public static void main(String[] args) {

		int num[] = { 12, 34, 5, 3, 7, 56, 89, 32 };
		MergeSort sort = new MergeSort();
		sort.sortNumbers(num);
		sort.printSortedValue(num);
	}

	private void sortNumbers(int[] num) {

		if (num.length < 2) {
			return;
		}
		int leftLength = num.length / 2;
		int rightLength = num.length - leftLength;
		int[] leftArr = new int[leftLength];
		int[] rightArr = new int[rightLength];
		for (int i = 0; i < leftLength; i++) {
			leftArr[i] = num[i];
		}
		for (int j = 0; j < rightLength; j++) {
			rightArr[j] = num[rightLength + j];
		}
		sortNumbers(leftArr);
		sortNumbers(rightArr);
		merge(leftArr, rightArr, num);
	}

	private void merge(int[] leftArr, int[] rightArr, int[] num) {

		int leftLength = leftArr.length;
		int rightLength = rightArr.length;
		int i = 0;
		int j = 0;
		int k = 0;
		while (i < leftLength && j < rightLength) {
			if (leftArr[i] <= rightArr[j]) {
				num[k] = leftArr[i];
				i++;

			} else {
				num[k] = rightArr[j];
				j++;

			}
			k++;
		}
		while (i < leftLength) {
			num[k] = leftArr[i];
			i++;
			k++;
		}
		while (j < rightLength) {
			num[k] = rightArr[j];
			j++;
			k++;
		}
	}

	private void printSortedValue(int[] sortedNum) {

		for (int i : sortedNum) {
			System.out.print(i + "  ");
		}
	}

}
