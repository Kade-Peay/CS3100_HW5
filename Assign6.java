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
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        
        int[] pageFaultsFifo = new int[maxMemoryFrames]; // (output) an array used to record the number of page faults that occur each simulation of some number of frames.
        int[] pageFaultsLru = new int[maxMemoryFrames];
        int[] pageFaultsMru = new int[maxMemoryFrames];

        int fifoMinPf = 0;
        int lruMinPf = 0;
        int mruMinPf = 0;

        long start = System.currentTimeMillis();
        
        for(int simulation = 0; simulation < SEQUENCE_LENGTH; simulation++) {
            int[] sequence = new int[SEQUENCE_LENGTH];
            for(int page = 0; page < sequence.length; page++){
                sequence[page] = rand.nextInt(maxPageReference);
            }

            for(int frame = 0; frame < maxMemoryFrames; frame++) {
                TaskFIFO fifo = new TaskFIFO(sequence, frame, maxPageReference, pageFaultsFifo);
                TaskLRU lru = new TaskLRU(sequence, frame, maxPageReference, pageFaultsLru);
                TaskMRU mru = new TaskMRU(sequence, frame, maxPageReference, pageFaultsMru);

                executor.execute(() -> fifo.run());
                executor.execute(() -> lru.run());
                executor.execute(() -> mru.run());
            }    
    
            for(int i = 0; i < maxMemoryFrames; i++) {
                int f = pageFaultsFifo[i]; // fifo
                int l = pageFaultsLru[i]; // lru
                int m = pageFaultsMru[i]; // mru
    
                if(f<l) {
                    if(m<f) {
                        mruMinPf++;
                    } else {
                        fifoMinPf++;
                    }
                } else {
                    if(l<m) {
                        lruMinPf++;
                    } else {
                        mruMinPf++;
                    }
                }
            }
        }

        // Print total time to do all simulations
        long end = System.currentTimeMillis();
        System.out.printf("\nSimulation took %d ms\n\n", (end - start)); 
        
        
        System.out.printf("FIFO min PF: %d\n", fifoMinPf);
        System.out.printf("LRU min PF: %d\n", lruMinPf);
        System.out.printf("MRU min PF: %d\n", mruMinPf);
        

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