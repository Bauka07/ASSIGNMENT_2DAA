package algorithms;

import metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for SelectionSort with early termination
 */
class SelectionSortTest {
    private PerformanceTracker tracker;
    
    @BeforeEach
    void setUp() {
        tracker = new PerformanceTracker();
    }
    
    @Test
    @DisplayName("Should handle empty array")
    void testEmptyArray() {
        int[] arr = {};
        SelectionSort.sort(arr, tracker);
        assertEquals(0, arr.length);
        assertEquals(0, tracker.getComparisons());
    }
    
    @Test
    @DisplayName("Should handle single element array")
    void testSingleElement() {
        int[] arr = {42};
        SelectionSort.sort(arr, tracker);
        assertArrayEquals(new int[]{42}, arr);
    }
    
    @Test
    @DisplayName("Should sort already sorted array with early termination")
    void testSortedArray() {
        int[] arr = {1, 2, 3, 4, 5};
        SelectionSort.sort(arr, tracker);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
        // With early termination, comparisons should be minimal
        assertTrue(tracker.getComparisons() <= arr.length);
    }
    
    @Test
    @DisplayName("Should sort reverse sorted array")
    void testReverseSortedArray() {
        int[] arr = {5, 4, 3, 2, 1};
        SelectionSort.sort(arr, tracker);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }
    
    @Test
    @DisplayName("Should sort random array")
    void testRandomArray() {
        int[] arr = {3, 1, 4, 1, 5, 9, 2, 6};
        SelectionSort.sort(arr, tracker);
        assertArrayEquals(new int[]{1, 1, 2, 3, 4, 5, 6, 9}, arr);
    }
    
    @Test
    @DisplayName("Should handle array with duplicates")
    void testDuplicates() {
        int[] arr = {5, 2, 5, 1, 2, 1};
        SelectionSort.sort(arr, tracker);
        assertArrayEquals(new int[]{1, 1, 2, 2, 5, 5}, arr);
    }
    
    @Test
    @DisplayName("Should handle nearly sorted array with optimizations")
    void testNearlySortedArray() {
        int[] arr = {1, 2, 4, 3, 5}; // Nearly sorted with one inversion
        SelectionSort.sort(arr, tracker);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }
    
    @Test
    @DisplayName("Should throw exception for null array")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            SelectionSort.sort(null, tracker);
        });
    }
    
    @Test
    @DisplayName("Should throw exception for null tracker")
    void testNullTracker() {
        int[] arr = {1, 2, 3};
        assertThrows(IllegalArgumentException.class, () -> {
            SelectionSort.sort(arr, null);
        });
    }
    
    @Test
    @DisplayName("Should track performance metrics correctly")
    void testPerformanceTracking() {
        int[] arr = {3, 1, 4, 2};
        SelectionSort.sort(arr, tracker);
        
        assertTrue(tracker.getComparisons() > 0, "Should track comparisons");
        assertTrue(tracker.getSwaps() > 0, "Should track swaps");
        assertTrue(tracker.getArrayAccesses() > 0, "Should track array accesses");
        assertTrue(tracker.getTimeNanos() > 0, "Should track time");
    }
    
    @Test
    @DisplayName("Should work with convenience method without tracker")
    void testConvenienceMethod() {
        int[] arr = {3, 1, 2};
        assertDoesNotThrow(() -> SelectionSort.sort(arr));
        assertArrayEquals(new int[]{1, 2, 3}, arr);
    }
    
    @Test
    @DisplayName("Should handle large array")
    void testLargeArray() {
        int[] arr = new int[1000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr.length - i - 1; // Reverse sorted
        }
        
        SelectionSort.sort(arr, tracker);
        
        for (int i = 0; i < arr.length - 1; i++) {
            assertTrue(arr[i] <= arr[i + 1], "Array should be sorted");
        }
    }
}
