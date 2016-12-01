package modest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LamportQueue {
    
    List<int[]> q;
    boolean empty = true;
    
    public LamportQueue(){
        this.q = new ArrayList<int[]>();
    }
    
    public void enqueue(int[] clock){
        q.add(clock);
        empty = false;
        //orderQueue();
    }
    
    public void dequeue(int id){
        Iterator<int[]> itr = q.iterator();
        while(itr.hasNext()){
            int[] itm = itr.next();
            if (itm[1]==id){
                itr.remove();
            }
        }
        if (q.size()==0){
            empty = true;
        }
    }
    
    public boolean isEmpty(){
        return empty;
    }
    
    public int size(){
        return q.size();
    }
    
    public int[] get(int idx){
        return q.get(idx);
    }
    
    public void set(int x, int[] clock){
        q.set(x, clock);
    }
    
    public void add(int[] clock){
        q.add(clock);
    }
    
    public void remove(int idx){
        q.remove(idx);
    }
    
}
