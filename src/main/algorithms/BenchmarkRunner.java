package cli;

import algorithms.SelectionSort;
import metrics.PerformanceTracker;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * CLI interface for benchmarking Selection Sort performance
 */
public class BenchmarkRunner {
    private static final Random RANDOM = new Random(42); // Fixed seed for reproducibility
    
    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        
        switch (args[0]) {
            case "benchmark":
                runBenchmarks();
                break;
            case "test":
                runCorrectnessTests();
                break;
            case "single":
                if (args.length < 2) {
                    System.out.println("Please specify array size");
                    return;
                }
                runSingleTest(Integer.parseInt(args[1]));
                break;
            default:
                printUsage();
        }
    }
    
    private static void printUsage() {
        System.out.println("Selection Sort Benchmark Runner");
        System.out.println("Usage:");
        System.out.println("  benchmark - Run comprehensive benchmarks");
        System.out.println("  test      - Run correctness tests");
        System.out.println("  single <size> - Run single test with given array size");
    }
    
    private static void runBenchmarks() {
        System.out.println("Running Selection Sort Benchmarks...");
        System.out.println("Size,Comparisons,Swaps,ArrayAccesses,TimeNanos");
        
        int[] sizes = {100, 1000, 10000, 100000};
        String[] distributions = {"random", "sorted", "reverse", "nearly-sorted"};
        
        try (FileWriter writer = new FileWriter("benchmark_results.csv")) {
            writer.write("Size,Distribution,Comparisons,Swaps,ArrayAccesses,TimeNanos\n");
            
            for (int size : sizes) {
                if (size > 10000) {
                    System.out.println("Skipping size " + size + " for detailed analysis (too slow)");
                    continue;
                }
                
                for (String distribution : distributions) {
                    int[] arr = generateArray(size, distribution);
                    PerformanceTracker tracker = new PerformanceTracker();
                    
                    SelectionSort.sort(arr, tracker);
                    
                    String line = String.format("%d,%s,%s\n", size, distribution, tracker.toCSV());
                    System.out.printf("%d,%s,%s\n", size, distribution, tracker.toCSV());
                    writer.write(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing benchmark results: " + e.getMessage());
        }
    }
    
    private static void runCorrectnessTests() {
        System.out.println("Running Correctness Tests...");
        
        testEmptyArray();
        testSingleElement();
        testSortedArray();
        testReverseSortedArray();
        testRandomArray();
        testDuplicates();
        
        System.out.println("All correctness tests passed!");
    }
    
    private static void runSingleTest(int size) {
        System.out.println("Running single test with size: " + size);
        
        int[] arr = generateArray(size, "random");
        PerformanceTracker tracker = new PerformanceTracker();
        
        System.out.println("Original array (first 10 elements): " + 
            SelectionSort.arrayToString(Arrays.copyOf(arr, Math.min(10, arr.length))));
        
        SelectionSort.sort(arr, tracker);
        
        System.out.println("Sorted array (first 10 elements): " + 
            SelectionSort.arrayToString(Arrays.copyOf(arr, Math.min(10, arr.length))));
        System.out.println("Performance: " + tracker);
        
        // Verify sorting is correct
        if (isSorted(arr)) {
            System.out.println("✓ Array is correctly sorted");
        } else {
            System.out.println("✗ Array sorting failed!");
        }
    }
    
    private static int[] generateArray(int size, String distribution) {
        int[] arr = new int[size];
        
        switch (distribution) {
            case "sorted":
                for (int i = 0; i < size; i++) arr[i] = i;
                break;
            case "reverse":
                for (int i = 0; i < size; i++) arr[i] = size - i - 1;
                break;
            case "nearly-sorted":
                for (int i = 0; i < size; i++) arr[i] = i;
                // Randomly swap 10% of elements
                for (int i = 0; i < size / 10; i++) {
                    int idx1 = RANDOM.nextInt(size);
                    int idx2 = RANDOM.nextInt(size);
                    int temp = arr[idx1];
                    arr[idx1] = arr[idx2];
                    arr[idx2] = temp;
                }
                break;
            case "random":
            default:
                for (int i = 0; i < size; i++) arr[i] = RANDOM.nextInt(size * 10);
                break;
        }
        
        return arr;
    }
    
    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    // Test methods
    private static void testEmptyArray() {
        int[] arr = {};
        SelectionSort.sort(arr);
        System.out.println("✓ Empty array test passed");
    }
    
    private static void testSingleElement() {
        int[] arr = {42};
        SelectionSort.sort(arr);
        assert arr[0] == 42 : "Single element test failed";
        System.out.println("✓ Single element test passed");
    }
    
    private static void testSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        SelectionSort.sort(arr);
        assert isSorted(arr) : "Sorted array test failed";
        System.out.println("✓ Sorted array test passed");
    }
    
    private static void testReverseSortedArray() {
        int[] arr = {5, 4, 3, 2, 1};
        SelectionSort.sort(arr);
        assert isSorted(arr) : "Reverse sorted test failed";
        System.out.println("✓ Reverse sorted test passed");
    }
    
    private static void testRandomArray() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
        SelectionSort.sort(arr);
        assert isSorted(arr) : "Random array test failed";
        System.out.println("✓ Random array test passed");
    }
    
    private static void testDuplicates() {
        int[] arr = {5, 2, 5, 1, 2, 1};
        SelectionSort.sort(arr);
        assert isSorted(arr) : "Duplicates test failed";
        System.out.println("✓ Duplicates test passed");
    }
}
