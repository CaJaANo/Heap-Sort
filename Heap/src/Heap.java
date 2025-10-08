import java.util.ArrayList;

public class Heap {
    public static void heapSort(ArrayList<Integer> arr) {
        int n = arr.size();

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i);

        for (int i = n - 1; i >= 0; i--) {
            int temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);

            heapify(arr, i, 0);
        }
    }

    public static void heapify(ArrayList<Integer> arr, int min, int max) {
        int largest = max;
        int left = 2 * max + 1;
        int right = 2 * max + 2;

        if (left < min && arr.get(left) > arr.get(largest))
            largest = left;

        if (right < min && arr.get(right) > arr.get(largest))
            largest = right;

        if (largest != max) {
            int swap = arr.get(max);
            arr.set(max, arr.get(largest));
            arr.set(largest, swap);

            heapify(arr, min, largest);
        }
    }
}
