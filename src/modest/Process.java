package modest;

import java.util.*;

public class Process implements MsgHandler {

    public int myId;
    public int n; // number of neighbors including myself
    MsgHandler app = null; // upper layer
    MsgHandler comm = null; // lower layer
    public boolean appFinished = false;
    public Properties prop = new Properties();
    public List<Integer> neighbors = new ArrayList<Integer>();

    public Process(MsgHandler initComm)  {
	comm = initComm;
	myId = comm.getMyId();	       
	neighbors = comm.getNeighbors();
	n = neighbors.size() + 1;
	prop = comm.getProp();
    }

    @Override
    public void init(MsgHandler app){
	this.app = app;
	comm.init(this);    	
    }

    @Override
    public synchronized void handleMsg(Msg m, int src, String tag){
        System.out.println("Handle Message");
        //No-op
    }

    @Override
    public synchronized void executeMsg(Msg m) {	
	handleMsg(m, m.src, m.tag);
	notifyAll();
	if (app != null){
            app.executeMsg(m);
        }		
    }

    @Override
    public void sendMsg(int destId, Object ... objects) {
	comm.sendMsg(destId, objects);
    }

    public void sendMsg(List<Integer> destIds, Object... objects) {
        for (int i : destIds) {
            if (i != myId) {
		sendMsg(i, objects);
            }
        }
    }

    @Override
    public synchronized int getMyId() {
        return myId;
    }

    @Override
    public Properties getProp() {
        return prop;
    }

    public synchronized void myWait() {
	try {
            wait();
	} catch (InterruptedException e) {
            System.err.println(e);
	}
    }

    public static void println(String s) {
	System.out.println(s);
    }

    @Override
    public List<Integer> getNeighbors() {
        return comm.getNeighbors();
    }

    @Override
    public void close() {
	comm.close();
    }

    @Override
    public void turnPassive() { 
	comm.turnPassive();
    }
}
