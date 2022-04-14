
public class Assign6 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        


        long end = System.currentTimeMillis();
        System.out.printf("Simulation took %d ms\n", (end - start));
    }

    public static void testLRU() {
        int[] sequence1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] sequence2 = {1, 2, 1, 3, 2, 1, 2, 3, 4};
        int[] pageFaults = new int[4];  // 4 because maxMemoryFrames is 3
    
        // Replacement should be: 1, 2, 3, 4, 5, 6, 7, 8
        // Page Faults should be 9
        // (new TaskLRU(sequence1, 1, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[1]);
    
        // Replacement should be: 2, 1, 3, 1, 2
        // Page Faults should be 7
        // (new TaskLRU(sequence2, 2, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[2]);
    
        // Replacement should be: 1
        // Page Faults should be 4
        // (new TaskLRU(sequence2, 3, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[3]);
    }

    public static void testMRU() {
        int[] sequence1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] sequence2 = {1, 2, 1, 3, 2, 1, 2, 3, 4};
        int[] pageFaults = new int[4];  // 4 because maxMemoryFrames is 3
    
        // Replacement should be: 1, 2, 3, 4, 5, 6, 7, 8
        // Page Faults should be 9
        // (new TaskMRU(sequence1, 1, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[1]);
    
        // Replacement should be: 1, 2, 1, 3
        // Page Faults should be 6
        // (new TaskMRU(sequence2, 2, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[2]);
    
        // Replacement should be: 3
        // Page Faults should be 4
        // (new TaskMRU(sequence2, 3, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[3]);
    }
    
}