package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for SelectionSort implementation.
 * Tests correctness, edge cases, and performance characteristics.
 * 
 * @author Bauyrzhan
 * @version 1.0
 */
class SelectionSortTest {
    
    private SelectionSort sorter;
    private PerformanceTracker tracker;
    private Random random;
    
    @BeforeEach
    void setUp() {
        tracker = new PerformanceTracker();
        sorter = new SelectionSort(tracker);
        random = new Random(42); // Fixed seed for reproducibility
    }
    
    // ===== Edge Cases =====
    
    @Test
    @DisplayName("Test null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> sorter.sort(null));
    }
    
    @Test
    @DisplayName("Test empty array")
    void testEmptyArray() {
        int[] arr = {};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertEquals(0, arr.length);
    }
    
    @Test
    @DisplayName("Test single element array")
    void testSingleElement() {
        int[] arr = {42};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertEquals(42, arr[0]);
    }
    
    @Test
    @DisplayName("Test two elements - already sorted")
    void testTwoElementsSorted() {
        int[] arr = {1, 2};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{1, 2}, arr);
    }
    
    @Test
    @DisplayName("Test two elements - needs sorting")
    void testTwoElementsUnsorted() {
        int[] arr = {2, 1};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{1, 2}, arr);
    }
    
    // ===== Correctness Tests =====
    
    @Test
    @DisplayName("Test small random array")
    void testSmallRandomArray() {
        int[] arr = {5, 2, 8, 1, 9, 3};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{1, 2, 3, 5, 8, 9}, arr);
    }
    
    @Test
    @DisplayName("Test already sorted array")
    void testAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, arr);
    }
    
    @Test
    @DisplayName("Test reverse sorted array")
    void testReverseSorted() {
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, arr);
    }
    
    @Test
    @DisplayName("Test array with duplicates")
    void testDuplicates() {
        int[] arr = {5, 2, 8, 2, 9, 5, 1};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{1, 2, 2, 5, 5, 8, 9}, arr);
    }
    
    @Test
    @DisplayName("Test all elements identical")
    void testAllIdentical() {
        int[] arr = {7, 7, 7, 7, 7};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, arr);
    }
    
    @Test
    @DisplayName("Test array with negative numbers")
    void testNegativeNumbers() {
        int[] arr = {-5, 3, -1, 7, -10, 0, 2};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{-10, -5, -1, 0, 2, 3, 7}, arr);
    }
    
    @Test
    @DisplayName("Test array with mix of positive, negative, and zero")
    void testMixedNumbers() {
        int[] arr = {-3, 0, -7, 5, 0, -1, 9};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{-7, -3, -1, 0, 0, 5, 9}, arr);
    }
    
    // ===== Property-Based Tests =====
    
    @ParameterizedTest
    @ValueSource(ints = {10, 50, 100, 500})
    @DisplayName("Test random arrays of various sizes")
    void testRandomArrays(int size) {
        int[] arr = generateRandomArray(size);
        int[] expected = Arrays.copyOf(arr, arr.length);
        Arrays.sort(expected);
        
        sorter.sort(arr);
        
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(expected, arr);
    }
    
    @Test
    @DisplayName("Test multiple random arrays for consistency")
    void testMultipleRandomArrays() {
        for (int i = 0; i < 100; i++) {
            int size = random.nextInt(50) + 1;
            int[] arr = generateRandomArray(size);
            int[] expected = Arrays.copyOf(arr, arr.length);
            Arrays.sort(expected);
            
            sorter.sort(arr);
            
            assertTrue(SelectionSort.isSorted(arr), 
                "Array not sorted correctly on iteration " + i);
            assertArrayEquals(expected, arr, 
                "Array doesn't match expected sorted order on iteration " + i);
        }
    }
    
    // ===== Performance Metrics Tests =====
    
    @Test
    @DisplayName("Test metrics tracking for small array")
    void testMetricsTracking() {
        int[] arr = {5, 2, 8, 1, 9};
        sorter.sort(arr);
        
        assertTrue(tracker.getComparisons() > 0, "Should have comparisons");
        assertTrue(tracker.getSwaps() >= 0, "Should track swaps");
        assertTrue(tracker.getExecutionTimeNanos() > 0, "Should track time");
    }
    
    @Test
    @DisplayName("Test early termination on sorted array")
    void testEarlyTerminationSorted() {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        sorter.sort(arr);
        
        // Early termination should occur for sorted arrays
        assertTrue(tracker.isEarlyTermination() || tracker.getSwaps() == 0,
            "Should detect sorted array and terminate early or make no swaps");
    }
    
    @Test
    @DisplayName("Test comparisons count is O(n²) for random array")
    void testComparisonComplexity() {
        int[] sizes = {10, 20, 40};
        long[] comparisons = new long[sizes.length];
        
        for (int i = 0; i < sizes.length; i++) {
            int[] arr = generateRandomArray(sizes[i]);
            PerformanceTracker t = new PerformanceTracker();
            SelectionSort s = new SelectionSort(t);
            s.sort(arr);
            comparisons[i] = t.getComparisons();
        }
        
        // For selection sort, comparisons should be roughly n(n-1)/2
        // When we double n, comparisons should roughly quadruple
        double ratio = (double) comparisons[2] / comparisons[1];
        assertTrue(ratio > 3.0 && ratio < 5.0, 
            "Comparison growth should be quadratic. Ratio: " + ratio);
    }
    
    // ===== Optimization Tests =====
    
    @Test
    @DisplayName("Test aggressive termination implementation")
    void testAggressiveTermination() {
        int[] arr = {1, 2, 3, 4, 5, 10, 6, 7, 8, 9};
        sorter.sortWithAggressiveTermination(arr);
        
        assertTrue(SelectionSort.isSorted(arr));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, arr);
    }
    
    @Test
    @DisplayName("Compare standard vs aggressive termination on nearly sorted")
    void compareTerminationStrategies() {
        int[] arr1 = generateNearlySortedArray(100, 0.95);
        int[] arr2 = Arrays.copyOf(arr1, arr1.length);
        
        PerformanceTracker tracker1 = new PerformanceTracker();
        PerformanceTracker tracker2 = new PerformanceTracker();
        
        SelectionSort sorter1 = new SelectionSort(tracker1);
        SelectionSort sorter2 = new SelectionSort(tracker2);
        
        sorter1.sort(arr1);
        sorter2.sortWithAggressiveTermination(arr2);
        
        assertTrue(SelectionSort.isSorted(arr1));
        assertTrue(SelectionSort.isSorted(arr2));
        assertArrayEquals(arr1, arr2);
    }
    
    // ===== Stability Test (Note: Selection Sort is NOT stable) =====
    
    @Test
    @DisplayName("Document that Selection Sort is not stable")
    void testStability() {
        // This test documents that selection sort is NOT a stable sort
        // We're not testing for stability, just documenting the behavior
        int[] arr = {5, 2, 8, 2, 9};
        sorter.sort(arr);
        assertTrue(SelectionSort.isSorted(arr));
        // The two 2's may have swapped positions - this is expected
    }
    
    // ===== Helper Methods =====
    
    /**
     * Generates a random array of specified size.
     */
    private int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(size * 10);
        }
        return arr;
    }
    
    /**
     * Generates a nearly sorted array.
     * 
     * @param size Size of array
     * @param sortedPercentage Percentage of elements in correct position (0.0-1.0)
     */
    private int[] generateNearlySortedArray(int size, double sortedPercentage) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        
        // Randomly swap some elements
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
    
    // ===== Verification Tests =====
    
    @Test
    @DisplayName("Verify isSorted helper method")
    void testIsSortedHelper() {
        assertTrue(SelectionSort.isSorted(new int[]{}));
        assertTrue(SelectionSort.isSorted(new int[]{1}));
        assertTrue(SelectionSort.isSorted(new int[]{1, 2, 3}));
        assertTrue(SelectionSort.isSorted(new int[]{1, 1, 1}));
        assertFalse(SelectionSort.isSorted(new int[]{2, 1}));
        assertFalse(SelectionSort.isSorted(new int[]{1, 3, 2}));
    }
}
