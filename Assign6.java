import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Assign6 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        
        Random rand = new Random();
        final int SEQUENCE_LENGTH = 1000;
        final int nThreads = Runtime.getRuntime().availableProcessors();        
        int maxMemoryFrames = 100; // (input) the number of frames of memory available
        int maxPageReference = 250; // (input) the maximum page reference possible in the sequence        
        int[] pageFaults = new int[maxMemoryFrames]; // (output) an array used to record the number of page faults that occur each simulation of some number of frames.
        
        
        /* 
        Each call to the 'run' method of a task results in storing the number of page faults for the task using something like: 
        'pageFaults[maxMemoryFrames] = pageFaults' (where 'pageFaults' is the number of page faults your code detects)
        */
        
        for(int i = 0; i < maxMemoryFrames; i++) { // for memory frames ranging from 1 to 100
            int[] sequence = new int[SEQUENCE_LENGTH]; // (input) a randomly generated sequence of page refrences
            for(int j = 0; j < SEQUENCE_LENGTH; j++){ // Generate a page reference sequence of length 1000
                sequence[j] = rand.nextInt(maxPageReference); // page references from 1 to 250
            }
            TaskFIFO fifo = new TaskFIFO(sequence, maxMemoryFrames, maxPageReference, pageFaults, i); // create a FIFO simulation task to simulate FIFO page replacement
            TaskLRU lru = new TaskLRU(sequence, maxMemoryFrames, maxPageReference, pageFaults, i); // Create a LRU simulation task to simulate LRU page replacement
            TaskMRU mru = new TaskMRU(sequence, maxMemoryFrames, maxPageReference, pageFaults, i); // Create a MRU simulation task to simulate MRU page replacement
            
            // TODO: use a thread pool ('Executors.newFixedThreadPool') to execute individual simulation tasks.
            // Create as many workers as there are processors available on the system.
            ExecutorService executorService = Executors.newFixedThreadPool(nThreads); // Add these tasks to the thread pool for execution
            fifo.run();
        }

        for (int page : pageFaults) {
            System.out.println(page);
        }


        // Print total time to do all simulations
        long end = System.currentTimeMillis();
        System.out.printf("Simulation took %d ms\n", (end - start)); 
    }

    // public static void testLRU() {
    //     int[] sequence1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    //     int[] sequence2 = {1, 2, 1, 3, 2, 1, 2, 3, 4};
    //     int[] pageFaults = new int[4];  // 4 because maxMemoryFrames is 3
    
    //     // Replacement should be: 1, 2, 3, 4, 5, 6, 7, 8
    //     // Page Faults should be 9
    //     // (new TaskLRU(sequence1, 1, MAX_PAGE_REFERENCE, pageFaults)).run();
    //     System.out.printf("Page Faults: %d\n", pageFaults[1]);
    
    //     // Replacement should be: 2, 1, 3, 1, 2
    //     // Page Faults should be 7
    //     // (new TaskLRU(sequence2, 2, MAX_PAGE_REFERENCE, pageFaults)).run();
    //     System.out.printf("Page Faults: %d\n", pageFaults[2]);
    
    //     // Replacement should be: 1
    //     // Page Faults should be 4
    //     // (new TaskLRU(sequence2, 3, MAX_PAGE_REFERENCE, pageFaults)).run();
    //     System.out.printf("Page Faults: %d\n", pageFaults[3]);
    // }

    // public static void testMRU() {
    //     int[] sequence1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    //     int[] sequence2 = {1, 2, 1, 3, 2, 1, 2, 3, 4};
    //     int[] pageFaults = new int[4];  // 4 because maxMemoryFrames is 3
    
    //     // Replacement should be: 1, 2, 3, 4, 5, 6, 7, 8
    //     // Page Faults should be 9
    //     // (new TaskMRU(sequence1, 1, MAX_PAGE_REFERENCE, pageFaults)).run();
    //     System.out.printf("Page Faults: %d\n", pageFaults[1]);
    
    //     // Replacement should be: 1, 2, 1, 3
    //     // Page Faults should be 6
    //     // (new TaskMRU(sequence2, 2, MAX_PAGE_REFERENCE, pageFaults)).run();
    //     System.out.printf("Page Faults: %d\n", pageFaults[2]);
    
    //     // Replacement should be: 3
    //     // Page Faults should be 4
    //     // (new TaskMRU(sequence2, 3, MAX_PAGE_REFERENCE, pageFaults)).run();
    //     System.out.printf("Page Faults: %d\n", pageFaults[3]);
    // }
    
}