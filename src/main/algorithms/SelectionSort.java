package algorithms;

import metrics.PerformanceTracker;

/**
 * Selection Sort implementation with early termination optimization.
 * 
 * Selection Sort works by repeatedly finding the minimum element from the unsorted
 * portion and placing it at the beginning. This implementation includes:
 * - Early termination when array is already sorted
 * - Performance metrics tracking
 * - Optimized minimum element search
 * 
 * Time Complexity:
 * - Best Case: O(n) with early termination (already sorted)
 * - Average Case: O(n²)
 * - Worst Case: O(n²)
 * 
 * Space Complexity: O(1) - in-place sorting
 * 
 * @author Bauyrzhan
 * @version 1.0
 */
public class SelectionSort {
    
    private PerformanceTracker tracker;
    
    /**
     * Default constructor
     */
    public SelectionSort() {
        this.tracker = new PerformanceTracker();
    }
    
    /**
     * Constructor with custom performance tracker
     * @param tracker Custom performance tracker
     */
    public SelectionSort(PerformanceTracker tracker) {
        this.tracker = tracker;
    }
    
    /**
     * Sorts an array using Selection Sort with early termination optimization.
     * 
     * @param arr Array to be sorted
     * @throws IllegalArgumentException if array is null
     */
    public void sort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        
        tracker.reset();
        tracker.startTimer();
        
        int n = arr.length;
        
        // Early termination for arrays of size 0 or 1
        if (n <= 1) {
            tracker.stopTimer();
            return;
        }
        
        boolean swapped;
        
        // Iterate through the array
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            swapped = false;
            
            // Find the minimum element in unsorted portion
            for (int j = i + 1; j < n; j++) {
                tracker.incrementComparisons();
                tracker.incrementArrayAccesses(2); // arr[j] and arr[minIndex]
                
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            
            // Early termination optimization: if minIndex hasn't changed,
            // check if the rest of the array is sorted
            if (minIndex != i) {
                // Swap the found minimum element with the first element
                swap(arr, i, minIndex);
                tracker.incrementSwaps();
                swapped = true;
            }
            
            // Early termination: if no swap occurred and we can verify
            // the remaining array is sorted, we can terminate early
            if (!swapped && isSortedFrom(arr, i)) {
                tracker.setEarlyTermination(true);
                break;
            }
        }
        
        tracker.stopTimer();
    }
    
    /**
     * Checks if array is sorted from given index onwards.
     * This is used for early termination optimization.
     * 
     * @param arr Array to check
     * @param startIndex Starting index
     * @return true if sorted from startIndex, false otherwise
     */
    private boolean isSortedFrom(int[] arr, int startIndex) {
        for (int i = startIndex; i < arr.length - 1; i++) {
            tracker.incrementComparisons();
            tracker.incrementArrayAccesses(2);
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Swaps two elements in an array.
     * 
     * @param arr Array containing elements
     * @param i First index
     * @param j Second index
     */
    private void swap(int[] arr, int i, int j) {
        tracker.incrementArrayAccesses(4); // 2 reads, 2 writes
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * Alternative implementation with aggressive early termination.
     * Terminates as soon as a pass makes no swaps.
     * 
     * @param arr Array to be sorted
     */
    public void sortWithAggressiveTermination(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        
        tracker.reset();
        tracker.startTimer();
        
        int n = arr.length;
        
        if (n <= 1) {
            tracker.stopTimer();
            return;
        }
        
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            
            // Find minimum in unsorted portion
            for (int j = i + 1; j < n; j++) {
                tracker.incrementComparisons();
                tracker.incrementArrayAccesses(2);
                
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            
            // If minimum is already at correct position, array might be sorted
            if (minIndex == i) {
                // Check if remaining array is sorted
                boolean isSorted = true;
                for (int k = i; k < n - 1; k++) {
                    tracker.incrementComparisons();
                    tracker.incrementArrayAccesses(2);
                    if (arr[k] > arr[k + 1]) {
                        isSorted = false;
                        break;
                    }
                }
                if (isSorted) {
                    tracker.setEarlyTermination(true);
                    break;
                }
            } else {
                // Perform swap
                swap(arr, i, minIndex);
                tracker.incrementSwaps();
            }
        }
        
        tracker.stopTimer();
    }
    
    /**
     * Gets the performance tracker for this sort instance.
     * 
     * @return PerformanceTracker object with metrics
     */
    public PerformanceTracker getTracker() {
        return tracker;
    }
    
    /**
     * Utility method to print array.
     * 
     * @param arr Array to print
     */
    public static void printArray(int[] arr) {
        if (arr == null || arr.length == 0) {
            System.out.println("[]");
            return;
        }
        
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    /**
     * Validates if an array is sorted in ascending order.
     * 
     * @param arr Array to validate
     * @return true if sorted, false otherwise
     */
    public static boolean isSorted(int[] arr) {
        if (arr == null || arr.length <= 1) {
            return true;
        }
        
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
}
