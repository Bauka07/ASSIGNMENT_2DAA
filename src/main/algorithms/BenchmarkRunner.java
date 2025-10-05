package cli;

import algorithms.SelectionSort;
import metrics.PerformanceTracker;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Command-line interface for benchmarking Selection Sort algorithm.
 * Provides options for testing with various input sizes and distributions.
 * 
 * @author Bauyrzhan
 * @version 1.0
 */
public class BenchmarkRunner {
    
    private static final Random random = new Random(42); // Fixed seed for reproducibility
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║     Selection Sort Benchmark Tool                        ║");
        System.out.println("║     Algorithm Analysis Assignment - Bauyrzhan            ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        while (true) {
            displayMenu();
            System.out.print("Enter your choice: ");
            
            int choice = getIntInput(scanner);
            
            switch (choice) {
                case 1:
                    runSingleTest(scanner);
                    break;
                case 2:
                    runComprehensiveBenchmark();
                    break;
                case 3:
                    runScalabilityTest();
                    break;
                case 4:
                    runInputDistributionTest(scanner);
                    break;
                case 5:
                    runComparisonTest();
                    break;
                case 6:
                    testEdgeCases();
                    break;
                case 7:
                    System.out.println("\nExiting benchmark tool. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("\nInvalid choice. Please try again.\n");
            }
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║ MENU                                                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════════╣");
        System.out.println("║ 1. Run Single Test (custom size & type)                  ║");
        System.out.println("║ 2. Run Comprehensive Benchmark (all sizes & types)       ║");
        System.out.println("║ 3. Run Scalability Test (100 to 100,000 elements)        ║");
        System.out.println("║ 4. Run Input Distribution Test                           ║");
        System.out.println("║ 5. Compare Standard vs Aggressive Termination            ║");
        System.out.println("║ 6. Test Edge Cases                                       ║");
        System.out.println("║ 7. Exit                                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }
    
    private static void runSingleTest(Scanner scanner) {
        System.out.println("\n--- Single Test Configuration ---");
        System.out.print("Enter array size: ");
        int size = getIntInput(scanner);
        
        if (size < 0) {
            System.out.println("Invalid size. Must be non-negative.");
            return;
        }
        
        System.out.println("\nSelect input type:");
        System.out.println("1. Random");
        System.out.println("2. Sorted (ascending)");
        System.out.println("3. Reverse sorted (descending)");
        System.out.println("4. Nearly sorted (90% sorted)");
        System.out.println("5. All duplicates");
        System.out.print("Choice: ");
        
        int typeChoice = getIntInput(scanner);
        
        int[] arr = generateArray(size, typeChoice);
        
        if (arr == null) {
            System.out.println("Invalid input type.");
            return;
        }
        
        // Show sample of array if small enough
        if (size <= 20) {
            System.out.print("\nOriginal array: ");
            SelectionSort.printArray(arr);
        }
        
        PerformanceTracker tracker = new PerformanceTracker();
        SelectionSort sorter = new SelectionSort(tracker);
        
        System.out.println("\nSorting...");
        sorter.sort(arr);
        
        // Show sorted array if small enough
        if (size <= 20) {
            System.out.print("Sorted array: ");
            SelectionSort.printArray(arr);
        }
        
        // Verify correctness
        if (SelectionSort.isSorted(arr)) {
            System.out.println("✓ Array is correctly sorted");
        } else {
            System.out.println("✗ ERROR: Array is not sorted correctly!");
        }
        
        System.out.println();
        tracker.printMetrics();
    }
    
    private static void runComprehensiveBenchmark() {
        System.out.println("\n--- Comprehensive Benchmark ---");
        System.out.println("Testing with sizes: 100, 1000, 10000, 100000");
        System.out.println("Input types: Random, Sorted, Reversed, Nearly Sorted\n");
        
        int[] sizes = {100, 1000, 10000, 100000};
        String[] types = {"Random", "Sorted", "Reversed", "Nearly Sorted"};
        int[] typeCodes = {1, 2, 3, 4};
        
        PerformanceTracker tracker = new PerformanceTracker();
        
        for (int size : sizes) {
            for (int i = 0; i < types.length; i++) {
                System.out.printf("Testing: n=%d, type=%s...", size, types[i]);
                
                int[] arr = generateArray(size, typeCodes[i]);
                SelectionSort sorter = new SelectionSort(tracker);
                
                sorter.sort(arr);
                
                if (!SelectionSort.isSorted(arr)) {
                    System.out.println(" ERROR - Not sorted!");
                    continue;
                }
                
                tracker.saveBenchmarkResult(size, types[i]);
                System.out.printf(" ✓ (%.3f ms, %d comparisons, %d swaps)%n",
                    tracker.getExecutionTimeMillis(),
                    tracker.getComparisons(),
                    tracker.getSwaps()
                );
                
                tracker.reset();
            }
        }
        
        tracker.printResultsSummary();
        
        // Export to CSV
        try {
            String filename = "benchmark_results_" + System.currentTimeMillis() + ".csv";
            tracker.exportToCSV(filename);
            System.out.println("\n✓ Results exported to: " + filename);
        } catch (IOException e) {
            System.out.println("\n✗ Error exporting results: " + e.getMessage());
        }
    }
    
    private static void runScalabilityTest() {
        System.out.println("\n--- Scalability Test ---");
        System.out.println("Testing with random arrays from n=100 to n=100,000\n");
        
        int[] sizes = {100, 500, 1000, 2500, 5000, 10000, 25000, 50000, 100000};
        PerformanceTracker tracker = new PerformanceTracker();
        
        System.out.printf("%-12s %-15s %-15s %-15s%n", "Size", "Time (ms)", "Comparisons", "Swaps");
        System.out.println("-".repeat(60));
        
        for (int size : sizes) {
            int[] arr = generateArray(size, 1); // Random
            SelectionSort sorter = new SelectionSort(tracker);
            
            sorter.sort(arr);
            
            System.out.printf("%-12d %-15.6f %-15d %-15d%n",
                size,
                tracker.getExecutionTimeMillis(),
                tracker.getComparisons(),
                tracker.getSwaps()
            );
            
            tracker.saveBenchmarkResult(size, "Random");
            tracker.reset();
        }
        
        // Export results
        try {
            String filename = "scalability_test_" + System.currentTimeMillis() + ".csv";
            tracker.exportToCSV(filename);
            System.out.println("\n✓ Results exported to: " + filename);
        } catch (IOException e) {
            System.out.println("\n✗ Error exporting results: " + e.getMessage());
        }
    }
    
    private static void runInputDistributionTest(Scanner scanner) {
        System.out.println("\n--- Input Distribution Test ---");
        System.out.print("Enter array size: ");
        int size = getIntInput(scanner);
        
        if (size < 0) {
            System.out.println("Invalid size.");
            return;
        }
        
        String[] types = {"Random", "Sorted", "Reversed", "Nearly Sorted", "All Duplicates"};
        PerformanceTracker tracker = new PerformanceTracker();
        
        System.out.println();
        System.out.printf("%-20s %-15s %-15s %-15s %-15s%n",
            "Distribution", "Time (ms)", "Comparisons", "Swaps", "Early Term");
        System.out.println("-".repeat(80));
        
        for (int i = 0; i < types.length; i++) {
            int[] arr = generateArray(size, i + 1);
            SelectionSort sorter = new SelectionSort(tracker);
            
            sorter.sort(arr);
            
            System.out.printf("%-20s %-15.6f %-15d %-15d %-15s%n",
                types[i],
                tracker.getExecutionTimeMillis(),
                tracker.getComparisons(),
                tracker.getSwaps(),
                tracker.isEarlyTermination() ? "Yes" : "No"
            );
            
            tracker.reset();
        }
    }
    
    private static void runComparisonTest() {
        System.out.println("\n--- Standard vs Aggressive Termination Comparison ---");
        
        int[] sizes = {100, 1000, 10000};
        
        for (int size : sizes) {
            System.out.println("\n--- Size: " + size + " ---");
            
            // Test on nearly sorted array (best case for early termination)
            int[] arr1 = generateNearlySortedArray(size, 0.95);
            int[] arr2 = Arrays.copyOf(arr1, arr1.length);
            
            PerformanceTracker tracker1 = new PerformanceTracker();
            PerformanceTracker tracker2 = new PerformanceTracker();
            
            SelectionSort sorter1 = new SelectionSort(tracker1);
            SelectionSort sorter2 = new SelectionSort(tracker2);
            
            System.out.println("Standard implementation:");
            sorter1.sort(arr1);
            tracker1.printMetrics();
            
            System.out.println("\nAggressive termination implementation:");
            sorter2.sortWithAggressiveTermination(arr2);
            tracker2.printMetrics();
            
            double improvement = ((tracker1.getExecutionTimeMillis() - tracker2.getExecutionTimeMillis()) 
                                / tracker1.getExecutionTimeMillis()) * 100;
            System.out.printf("\nPerformance improvement: %.2f%%%n", improvement);
        }
    }
    
    private static void testEdgeCases() {
        System.out.println("\n--- Edge Cases Testing ---\n");
        
        PerformanceTracker tracker = new PerformanceTracker();
        SelectionSort sorter = new SelectionSort(tracker);
        
        // Empty array
        System.out.println("1. Empty array:");
        int[] empty = {};
        sorter.sort(empty);
        System.out.println("   Result: " + (SelectionSort.isSorted(empty) ? "✓ PASS" : "✗ FAIL"));
        
        // Single element
        System.out.println("\n2. Single element:");
        int[] single = {42};
        sorter.sort(single);
        System.out.println("   Result: " + (SelectionSort.isSorted(single) ? "✓ PASS" : "✗ FAIL"));
        
        // Two elements - sorted
        System.out.println("\n3. Two elements (sorted):");
        int[] twoSorted = {1, 2};
        sorter.sort(twoSorted);
        System.out.println("   Result: " + (SelectionSort.isSorted(twoSorted) ? "✓ PASS" : "✗ FAIL"));
        
        // Two elements - reversed
        System.out.println("\n4. Two elements (reversed):");
        int[] twoReversed = {2, 1};
        sorter.sort(twoReversed);
        System.out.println("   Result: " + (SelectionSort.isSorted(twoReversed) ? "✓ PASS" : "✗ FAIL"));
        
        // All duplicates
        System.out.println("\n5. All duplicates:");
        int[] duplicates = {5, 5, 5, 5, 5};
        sorter.sort(duplicates);
        System.out.println("   Result: " + (SelectionSort.isSorted(duplicates) ? "✓ PASS" : "✗ FAIL"));
        
        // Already sorted
        System.out.println("\n6. Already sorted array:");
        int[] sorted = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        sorter.sort(sorted);
        System.out.println("   Result: " + (SelectionSort.isSorted(sorted) ? "✓ PASS" : "✗ FAIL"));
        System.out.println("   Early termination: " + (tracker.isEarlyTermination() ? "Yes" : "No"));
        
        // Reverse sorted
        System.out.println("\n7. Reverse sorted array:");
        int[] reversed = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        tracker.reset();
        sorter.sort(reversed);
        System.out.println("   Result: " + (SelectionSort.isSorted(reversed) ? "✓ PASS" : "✗ FAIL"));
        
        // Array with negative numbers
        System.out.println("\n8. Array with negative numbers:");
        int[] negative = {-5, 3, -1, 7, -10, 0, 2};
        sorter.sort(negative);
        System.out.println("   Result: " + (SelectionSort.isSorted(negative) ? "✓ PASS" : "✗ FAIL"));
        
        // Array with duplicates
        System.out.println("\n9. Array with duplicates:");
        int[] withDupes = {3, 7, 3, 1, 7, 9, 1};
        sorter.sort(withDupes);
        System.out.println("   Result: " + (SelectionSort.isSorted(withDupes) ? "✓ PASS" : "✗ FAIL"));
        
        System.out.println("\n✓ All edge case tests completed!");
    }
    
    /**
     * Generates an array based on the specified type.
     * 
     * @param size Size of the array
     * @param type Type of array (1=random, 2=sorted, 3=reversed, 4=nearly sorted, 5=duplicates)
     * @return Generated array
     */
    private static int[] generateArray(int size, int type) {
        switch (type) {
            case 1:
                return generateRandomArray(size);
            case 2:
                return generateSortedArray(size);
            case 3:
                return generateReversedArray(size);
            case 4:
                return generateNearlySortedArray(size, 0.90);
            case 5:
                return generateDuplicatesArray(size);
            default:
                return null;
        }
    }
    
    /**
     * Generates a random array.
     */
    private static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }
    
    /**
     * Generates a sorted array.
     */
    private static int[] generateSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        return arr;
    }
    
    /**
     * Generates a reverse sorted array.
     */
    private static int[] generateReversedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i - 1;
        }
        return arr;
    }
    
    /**
     * Generates a nearly sorted array.
     * 
     * @param size Size of the array
     * @param sortedPercentage Percentage of elements that should be in sorted order (0.0 to 1.0)
     */
    private static int[] generateNearlySortedArray(int size, double sortedPercentage) {
        int[] arr = generateSortedArray(size);
        
        // Swap a small percentage of elements
        int swapsToMake = (int) ((1.0 - sortedPercentage) * size / 2);
        
        for (int i = 0; i < swapsToMake; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = arr[idx1];
            arr[idx1] = arr[idx2];
            arr[idx2] = temp;
        }
        
        return arr;
    }
    
    /**
     * Generates an array with all duplicate values.
     */
    private static int[] generateDuplicatesArray(int size) {
        int[] arr = new int[size];
        int value = random.nextInt(100);
        Arrays.fill(arr, value);
        return arr;
    }
    
    /**
     * Safely gets integer input from scanner.
     */
    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}
