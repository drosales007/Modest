package modest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagingQueue {
    
    List<String> q;
    boolean empty = true;
    
    public MessagingQueue(){
        this.q = new ArrayList<String>();
    }
    
    public void enqueue(String message){
        q.add(message);
        empty = false;
        //orderQueue();
    }
    
    public String dequeue(){
        String msg = q.get(0);
        q.remove(0);
        if (q.size()==0){
            empty = true;
        }
        return msg;
    }
    
    public boolean isEmpty(){
        return empty;
    }
    
    public int size(){
        return q.size();
    }
    
    public String get(int idx){
        return q.get(idx);
    }
    
    public void set(int x, String msg){
        q.set(x, msg);
    }
    
    public void add(String msg){
        q.add(msg);
    }
    
    public void remove(int idx){
        q.remove(idx);
    }
    
    public void clear(){
        q.clear();
    }
    
    public static int getClock(int idx, String msg){
        // Parses out the clock from a msg
	final ArrayList<Integer> clk = new ArrayList<Integer>();
	final Pattern integerPattern = Pattern.compile("(\\-?\\d+)");
    	final Matcher matched = integerPattern.matcher(msg);
    	while (matched.find()) {
     		clk.add(Integer.valueOf(matched.group()));
    	}
    	return clk.get(2);
    }
    
    public static int getProc(int idx, String msg){
        // Parses out the process from a msg
	final ArrayList<Integer> clk = new ArrayList<Integer>();
	final Pattern integerPattern = Pattern.compile("(\\-?\\d+)");
    	final Matcher matched = integerPattern.matcher(msg);
    	while (matched.find()) {
     		clk.add(Integer.valueOf(matched.group()));
    	}
    	return clk.get(0);
    }
    
    public static String[] getTime(int idx, String msg){
        // Parses out the time from a msg
        String[] message = msg.split(" ");
        return message[message.length -1].split(":");
    }
    
    public void orderQueue() {
        if (q.size()>1) {
            try {
                outerloop:
                // Loop through each item in the queue
                for(int x=0; x<q.size(); x++) {
                    String msg1 = q.get(x);
                    String[] t1 = getTime(x, msg1);
                    
                    midloop:
                    // Compare to each other item in the queue
                    for(int y=x+1; y<q.size(); y++) {
                        String msg2 = q.get(x);
                        String[] t2 = getTime(y, msg2);
                        
                        innerloop:
                        // Loops through each time delimiter
                        for(int z=0; z<3; z++) {
                            // If any of the times for t2 is less than t1, swap
                            if (Integer.parseInt(t2[z]) <
                                    Integer.parseInt(t1[z])) {
                                q.set(x, msg2);
                                q.set(y, msg1);
                                
                                if (x>0){
                                    x -= 1;
                                } else {
                                    x = -1;
                                }
                                break midloop;
                            }
                            
                            // If any of the times in t2 is more than t1, ignore
                            if (Integer.parseInt(t2[z]) >
                                    Integer.parseInt(t1[z])) {
                                break;
                            }
                            
                            // Check for equality in times
                            if (Integer.parseInt(t2[z]) ==
                                    Integer.parseInt(t1[z])) {
                                
                                // If they are equal, default to pid
                                if (z==2){
                                    if (Character.getNumericValue(
                                            msg1.charAt(0)) >
                                            Character.getNumericValue(
                                                    msg2.charAt(0))){
                                        q.set(x, msg2);
                                        q.set(y, msg1);
                                
                                        if (x>0){
                                            x -= 1;
                                        } else {
                                            x = -1;
                                        }
                                        break midloop;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
    }
}
