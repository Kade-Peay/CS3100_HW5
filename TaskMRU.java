import java.util.*;

public class TaskMRU implements Runnable {
    int[] sequence;
    int maxMemoryFrames;
    int maxPageReference;
    int[] pageFaults;

    public TaskMRU(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults){
        this.sequence = sequence; 
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {
        HashSet<Integer> s = new HashSet<>(this.maxMemoryFrames);
        HashMap<Integer, Integer> indexes = new HashMap<>();
        int page_faults = 0;
        for (int page=0; page < this.sequence.length; page++)
        {
            if (s.size() < this.maxMemoryFrames)
            {
                if (!s.contains(this.sequence[page]))
                {
                    s.add(this.sequence[page]);
                    page_faults++;
                }
                indexes.put(this.sequence[page], page);
            }
            else
            {
                if (!s.contains(this.sequence[page]))
                {
                    int mru = Integer.MAX_VALUE; int val=Integer.MIN_VALUE;               
                    Iterator<Integer> itr = s.iterator();
                     
                    while (itr.hasNext()) {
                        int temp = itr.next();
                        if (indexes.get(temp) > mru)
                        {
                            mru = indexes.get(temp);
                            val = temp;
                        }
                    }
                    s.remove(val);
                    indexes.remove(val);
                    s.add(this.sequence[page]);
                    page_faults++;
                }
                indexes.put(this.sequence[page], page);
            }
        }      
        pageFaults[maxMemoryFrames] = page_faults;
    }
}