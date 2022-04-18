import java.util.*;

public class TaskFIFO implements Runnable {
    int[] sequence;
    int maxMemoryFrames;
    int maxPageReference;
    int[] pageFaults;
    int iteration;

    public TaskFIFO(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults, int iteration){
        this.sequence = sequence; 
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
        this.iteration = iteration;
    }

    @Override
    public void run() {
        int page_faults = 0;
        HashSet<Integer> s = new HashSet<>(this.maxMemoryFrames);
        Queue<Integer> indexes = new LinkedList<>();
        for (int page : this.sequence) {
            if(s.size() <= this.maxMemoryFrames) {
                if(!s.contains(this.sequence[page])){
                    s.add(this.sequence[page]);
                    page_faults++;
                    indexes.add(this.sequence[page]);
                }
            } else {
                if(!s.contains(sequence[page])){
                    int val = indexes.peek();
                    indexes.poll();
                    s.remove(val);
                    s.add(this.sequence[page]);
                    indexes.add(this.sequence[page]);
                    page_faults++;
                }
            }
        }

        this.pageFaults[this.iteration] = page_faults;
        // return page_faults; // at this simulation, save the amount of page faults
    }
}