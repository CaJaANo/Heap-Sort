import java.util.ArrayList;
import java.util.List;

public class Heap {
    public static class Step {
        public int[] array;
        public int i = -1, j = -1;
        public Step(int[] a, int i, int j) { this.array = a.clone(); this.i = i; this.j = j; }
    }

    public static ArrayList<Step> heapSortMaxSteps(ArrayList<Integer> input) {
        ArrayList<Step> steps = new ArrayList<>();
        ArrayList<Integer> a = new ArrayList<>(input.subList(0, Math.min(input.size(), 63)));
        int n = a.size();

        for (int i = n / 2 - 1; i >= 0; i--) heapifyMax(a, n, i, steps);
        for (int end = n - 1; end > 0; end--) {
            swap(a, 0, end);
            steps.add(new Step(a.stream().mapToInt(x -> x).toArray(), 0, end));
            heapifyMax(a, end, 0, steps);
        }
        return steps;
    }

    private static void heapifyMax(ArrayList<Integer> a, int size, int root, ArrayList<Step> steps) {
        int largest = root, left = 2 * root + 1, right = 2 * root + 2;
        if (left < size && a.get(left) > a.get(largest)) largest = left;
        if (right < size && a.get(right) > a.get(largest)) largest = right;
        if (largest != root) {
            swap(a, root, largest);
            steps.add(new Step(a.stream().mapToInt(x -> x).toArray(), root, largest));
            heapifyMax(a, size, largest, steps);
        }
    }

    public static ArrayList<Step> heapSortMinSteps(ArrayList<Integer> input) {
        ArrayList<Step> steps = new ArrayList<>();
        ArrayList<Integer> a = new ArrayList<>(input.subList(0, Math.min(input.size(), 63)));
        int n = a.size();

        for (int i = n / 2 - 1; i >= 0; i--) heapifyMin(a, n, i, steps);
        for (int end = n - 1; end > 0; end--) {
            swap(a, 0, end);
            steps.add(new Step(a.stream().mapToInt(x -> x).toArray(), 0, end));
            heapifyMin(a, end, 0, steps);
        }
        return steps;
    }

    private static void heapifyMin(ArrayList<Integer> a, int size, int root, ArrayList<Step> steps) {
        int smallest = root, left = 2 * root + 1, right = 2 * root + 2;
        if (left < size && a.get(left) < a.get(smallest)) smallest = left;
        if (right < size && a.get(right) < a.get(smallest)) smallest = right;
        if (smallest != root) {
            swap(a, root, smallest);
            steps.add(new Step(a.stream().mapToInt(x -> x).toArray(), root, smallest));
            heapifyMin(a, size, smallest, steps);
        }
    }

    private static void swap(List<Integer> a, int i, int j) {
        int t = a.get(i); a.set(i, a.get(j)); a.set(j, t);
    }
}