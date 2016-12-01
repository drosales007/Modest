package modest;

public class LamportBakery extends Process implements Lock {
    
    int clock;
    int[] ticketNums;
    int acks = 1;
    boolean[] choosing;
    int ticketNum = 0;
    
    public LamportBakery(MsgHandler initComm){
        super(initComm);
        choosing = new boolean[n];
        ticketNums = new int[n];
        for (int x=0; x<n; x++){
            choosing[x] = false;
            ticketNums[x] = 0;
        }
    }
    
    @Override
    public void requestCS(){
        sendMsg(neighbors, "request", ticketNums[myId]);
        // Get my number
        chooseNumber();
        sendMsg(neighbors, "chosen", ticketNums[myId]);
        // Wait if other processes are also choosing numbers
        for (int x=0; x<n; x++){
            while(choosing[x]){
                myWait();
            }
        }
        // Wait for my turn in the CS
        while (!okayCS()) {
            myWait();
        }
    }
    
    @Override
    public void releaseCS(){
        ticketNums[myId] = 0;
        acks = 1;
        sendMsg(neighbors, "release", 0);
    }
    
    @Override
    public void handleMsg(Msg m, int src, String tag){
        _handleMsg(m, src, tag);
    }

    public synchronized void _handleMsg(Msg m, int src, String tag) {
        if (tag.equals("request")) {
            choosing[src] = true;
            sendMsg(src, "ack", ticketNums[myId]);
        } else if (tag.equals("release")) {
            ticketNums[src] = 0;
        } else if (tag.equals("ack")){
            ticketNums[src] = m.getMessageInt();
            acks++;
        } else if (tag.equals("chosen")){
            ticketNums[src] = m.getMessageInt();
            choosing[src] = false;
        }
    }
    
    public void chooseNumber(){
        //choosing[myId] = true;
        ticketNums[myId] = ticketNum + 1;
        for (int x=0; x<n; x++){
            if (ticketNums[x] > ticketNums[myId]){
                ticketNums[myId] = ticketNums[x] + 1;
            }
        }
        //choosing[myId] = false;
        ticketNum = ticketNums[myId];
    }
    
    public boolean okayCS(){
        // Check to see if I have the lowest ticket number
        for (int x=0; x<n; x++){
            if (x!=myId){
                if (isGreater(myId, x) && ticketNums[x]!=0){
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean isGreater(int src, int dest){
        // Check ticket number then process if a tie
        if (ticketNums[src] > ticketNums[dest]){
            return true;
        } else if (ticketNums[src] == ticketNums[dest]){
            if (src>dest){
                return false;
            }
        }
        return true;
    }
}
