package algorithms;

import metrics.PerformanceTracker;

/**
 * Implementation of Selection Sort with early termination optimizations
 * 
 * Features:
 * - Early termination when array is already sorted
 * - Minimum tracking optimization
 * - Reduced swap operations
 * - Comprehensive performance tracking
 * 
 * Time Complexity:
 * - Best case: Θ(n) with early termination
 * - Worst case: Θ(n²)
 * - Average case: Θ(n²)
 * 
 * Space Complexity: Θ(1) - in-place sorting
 */
public class SelectionSort {
    
    /**
     * Sorts the array using optimized selection sort with early termination
     * 
     * @param arr the array to be sorted
     * @param tracker performance tracker for metrics collection
     * @throws IllegalArgumentException if array is null
     */
    public static void sort(int[] arr, PerformanceTracker tracker) {
        if (arr == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
        
        if (tracker == null) {
            throw new IllegalArgumentException("Performance tracker cannot be null");
        }
        
        tracker.startTimer();
        
        int n = arr.length;
        
        // Early return for empty or single-element arrays
        if (n <= 1) {
            tracker.stopTimer();
            return;
        }
        
        // Early termination check: if already sorted
        if (isAlreadySorted(arr, tracker)) {
            tracker.stopTimer();
            return;
        }
        
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            boolean sortedRemaining = true;
            
            // Find minimum in remaining unsorted portion
            for (int j = i + 1; j < n; j++) {
                tracker.incrementComparisons();
                tracker.incrementArrayAccesses(2); // arr[j] and arr[minIndex]
                
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                    sortedRemaining = false; // Found inversion, not sorted
                } else if (arr[j] < arr[j - 1]) {
                    sortedRemaining = false; // Check if remaining portion is sorted
                }
            }
            
            // Early termination if remaining portion is sorted
            if (sortedRemaining && i > 0) {
                tracker.stopTimer();
                return;
            }
            
            // Swap only if necessary
            if (minIndex != i) {
                swap(arr, i, minIndex, tracker);
            }
            
            // Additional check: if array became sorted after this swap
            if (i >= n / 2 && isPartiallySorted(arr, i + 1, tracker)) {
                tracker.stopTimer();
                return;
            }
        }
        
        tracker.stopTimer();
    }
    
    /**
     * Sorts the array without performance tracking (convenience method)
     */
    public static void sort(int[] arr) {
        PerformanceTracker tracker = new PerformanceTracker();
        sort(arr, tracker);
    }
    
    /**
     * Checks if the array is already sorted
     */
    private static boolean isAlreadySorted(int[] arr, PerformanceTracker tracker) {
        for (int i = 0; i < arr.length - 1; i++) {
            tracker.incrementComparisons();
            tracker.incrementArrayAccesses(2);
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if array is sorted from start index to end
     */
    private static boolean isPartiallySorted(int[] arr, int start, PerformanceTracker tracker) {
        for (int i = start; i < arr.length - 1; i++) {
            tracker.incrementComparisons();
            tracker.incrementArrayAccesses(2);
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Swaps two elements in the array
     */
    private static void swap(int[] arr, int i, int j, PerformanceTracker tracker) {
        tracker.incrementArrayAccesses(4); // 2 reads + 2 writes
        tracker.incrementSwaps();
        
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    /**
     * Returns a string representation of the array
     */
    public static String arrayToString(int[] arr) {
        if (arr == null) return "null";
        if (arr.length == 0) return "[]";
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
