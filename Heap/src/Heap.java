import java.util.ArrayList;

public class Heap {

    public static class Step {
        int[] array;
        int highlight1 = -1;
        int highlight2 = -1;

        public Step(int[] array, int h1, int h2) {
            this.array = array.clone();
            this.highlight1 = h1;
            this.highlight2 = h2;
        }
    }

    public static ArrayList<Step> heapSortMaxSteps(ArrayList<Integer> arr) {
        ArrayList<Step> steps = new ArrayList<>();
        int n = arr.size();

        for (int i = n / 2 - 1; i >= 0; i--)
            heapifyMaxSteps(arr, n, i, steps);

        for (int i = n - 1; i >= 0; i--) {
            int temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);

            steps.add(new Step(arr.stream().mapToInt(x -> x).toArray(), 0, i));
            heapifyMaxSteps(arr, i, 0, steps);
        }
        return steps;
    }

    private static void heapifyMaxSteps(ArrayList<Integer> arr, int size, int parent, ArrayList<Step> steps) {
        int largest = parent;
        int left = 2 * parent + 1;
        int right = 2 * parent + 2;

        if (left < size && arr.get(left) > arr.get(largest))
            largest = left;
        if (right < size && arr.get(right) > arr.get(largest))
            largest = right;

        if (largest != parent) {
            int swap = arr.get(parent);
            arr.set(parent, arr.get(largest));
            arr.set(largest, swap);

            steps.add(new Step(arr.stream().mapToInt(x -> x).toArray(), parent, largest));
            heapifyMaxSteps(arr, size, largest, steps);
        }
    }

    public static ArrayList<Step> heapSortMinSteps(ArrayList<Integer> arr) {
        ArrayList<Step> steps = new ArrayList<>();
        int n = arr.size();

        for (int i = n / 2 - 1; i >= 0; i--)
            heapifyMinSteps(arr, n, i, steps);

        for (int i = n - 1; i >= 0; i--) {
            int temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);

            steps.add(new Step(arr.stream().mapToInt(x -> x).toArray(), 0, i));
            heapifyMinSteps(arr, i, 0, steps);
        }
        return steps;
    }

    private static void heapifyMinSteps(ArrayList<Integer> arr, int size, int parent, ArrayList<Step> steps) {
        int smallest = parent;
        int left = 2 * parent + 1;
        int right = 2 * parent + 2;

        if (left < size && arr.get(left) < arr.get(smallest))
            smallest = left;
        if (right < size && arr.get(right) < arr.get(smallest))
            smallest = right;

        if (smallest != parent) {
            int swap = arr.get(parent);
            arr.set(parent, arr.get(smallest));
            arr.set(smallest, swap);

            steps.add(new Step(arr.stream().mapToInt(x -> x).toArray(), parent, smallest));
            heapifyMinSteps(arr, size, smallest, steps);
        }
    }
}
