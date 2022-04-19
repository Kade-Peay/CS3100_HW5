import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Assign6 {
    public static void main(String[] args) {
        
        Random rand = new Random();
        final int SEQUENCE_LENGTH = 1000;
        final int nThreads = Runtime.getRuntime().availableProcessors();        
        int maxMemoryFrames = 100; // (input) the number of frames of memory available
        int maxPageReference = 250; // (input) the maximum page reference possible in the sequence        
        int[] pageFaults = new int[maxMemoryFrames]; // (output) an array used to record the number of page faults that occur each simulation of some number of frames.
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        
                
        long start = System.currentTimeMillis();
        
        for(int simulation = 0; simulation < SEQUENCE_LENGTH; simulation++) {
            int[] sequence = new int[SEQUENCE_LENGTH];
            for(int page = 0; page < sequence.length; page++){
                sequence[page] = rand.nextInt(maxPageReference);
            }

            for(int frame = 0; frame < maxMemoryFrames; frame++) {
                TaskFIFO fifo = new TaskFIFO(sequence, frame, maxPageReference, pageFaults);
                TaskLRU lru = new TaskLRU(sequence, frame, maxPageReference, pageFaults);
                TaskMRU mru = new TaskMRU(sequence, frame, maxPageReference, pageFaults);

                executor.execute(() -> fifo.run());
                executor.execute(() -> lru.run());
                executor.execute(() -> mru.run());
            }    
        }
        
        // TODO: summarize results
    
        
        // Print total time to do all simulations
        long end = System.currentTimeMillis();
        System.out.printf("Simulation took %d ms\n", (end - start)); 
        
        // insure that the threads shut down so the program doesn't hang
        executor.shutdownNow();
    }

    public static void testLRU() {
        int[] sequence1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] sequence2 = {1, 2, 1, 3, 2, 1, 2, 3, 4};
        int MAX_PAGE_REFERENCE = 250;
        int[] pageFaults = new int[4];  // 4 because maxMemoryFrames is 3
    
        // Replacement should be: 1, 2, 3, 4, 5, 6, 7, 8
        // Page Faults should be 9
        (new TaskLRU(sequence1, 1, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[1]);
    
        // Replacement should be: 2, 1, 3, 1, 2
        // Page Faults should be 7
        (new TaskLRU(sequence2, 2, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[2]);
    
        // Replacement should be: 1
        // Page Faults should be 4
        (new TaskLRU(sequence2, 3, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[3]);
    }

    public static void testMRU() {
        int[] sequence1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] sequence2 = {1, 2, 1, 3, 2, 1, 2, 3, 4};
        int MAX_PAGE_REFERENCE = 250;
        int[] pageFaults = new int[4];  // 4 because maxMemoryFrames is 3
    
        // Replacement should be: 1, 2, 3, 4, 5, 6, 7, 8
        // Page Faults should be 9
        (new TaskMRU(sequence1, 1, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[1]);
    
        // Replacement should be: 1, 2, 1, 3
        // Page Faults should be 6
        (new TaskMRU(sequence2, 2, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[2]);
    
        // Replacement should be: 3
        // Page Faults should be 4
        (new TaskMRU(sequence2, 3, MAX_PAGE_REFERENCE, pageFaults)).run();
        System.out.printf("Page Faults: %d\n", pageFaults[3]);
    }
    
}