package metrics;

/**
 * Tracks performance metrics for sorting algorithms
 */
public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long startTime;
    private long endTime;
    
    public PerformanceTracker() {
        reset();
    }
    
    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        startTime = 0;
        endTime = 0;
    }
    
    public void startTimer() {
        startTime = System.nanoTime();
    }
    
    public void stopTimer() {
        endTime = System.nanoTime();
    }
    
    public void incrementComparisons() {
        comparisons++;
    }
    
    public void incrementComparisons(int count) {
        comparisons += count;
    }
    
    public void incrementSwaps() {
        swaps++;
    }
    
    public void incrementArrayAccesses() {
        arrayAccesses++;
    }
    
    public void incrementArrayAccesses(int count) {
        arrayAccesses += count;
    }
    
    // Getters
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getTimeNanos() { return endTime - startTime; }
    public long getTimeMillis() { return (endTime - startTime) / 1_000_000; }
    
    @Override
    public String toString() {
        return String.format(
            "PerformanceMetrics{comparisons=%d, swaps=%d, arrayAccesses=%d, time=%.3fms}",
            comparisons, swaps, arrayAccesses, getTimeMillis()
        );
    }
    
    public String toCSV() {
        return String.format("%d,%d,%d,%d", comparisons, swaps, arrayAccesses, getTimeNanos());
    }
}
