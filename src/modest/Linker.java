package modest;

import java.util.*; import java.io.*;

public class Linker implements MsgHandler {

    public int myId;	
    public int n; // number of neighbors including myself
    Connector connector = null;
    MsgHandler app = null;// upper layer
    MsgHandler comm = null;// lower layer
    public boolean appFinished = false;
    public List<Integer> neighbors = new ArrayList<Integer>();	
    public Properties prop = new Properties();

    public Linker(String args[]) throws Exception { 
        String basename = args[0];
        myId = Integer.parseInt(args[1]);
        
        // If there are topology files, the neighbors will be set from those
        // else we assign the neighbors manually
        //
        // TODO: Neighbors may need to be set differently for each algorithm.
        // Auto generate a topology for the respective algorithms.
        if (!Topology.readNeighbors(myId, neighbors)) {
            Topology.setComplete(myId, neighbors, Integer.parseInt(args[2]));
        }
	n = neighbors.size() + 1;
        String fp = "C:\\Users\\IBM_ADMIN\\My Documents\\NetBeansProjects"
                    + "\\Modest\\build\\classes"
                    + "\\modest\\LinkerProp.xml";
	prop.loadFromXML(new FileInputStream(fp));
	connector = new Connector();
	connector.Connect(basename, myId, neighbors);
    }

    @Override
    public void init(MsgHandler app){
	this.app = app;
    }

    @Override
    public void sendMsg(int destId, Object ... objects) {	
        String msg = "Sender: " + myId + "   Receiver: " + destId + "   ";
	int j = neighbors.indexOf(destId);
	try {
            LinkedList<Object> objectList = Util.getLinkedList(objects);
            ObjectOutputStream os = connector.dataOut[j];
            os.writeObject(Integer.valueOf(objectList.size()));
            for (Object object : objectList) {
		os.writeObject(object);
                msg = msg + "   " + object;
            }
            Modest.mqueue.enqueue(msg);
            os.flush();
	} catch (IOException e) {
            System.out.println(e);
            close();
        }
    }

    public Msg receiveMsg(int fromId) {
	int i = neighbors.indexOf(fromId);
	try {
            ObjectInputStream oi = connector.dataIn[i];
            int numItems = ((Integer) oi.readObject()).intValue();
            LinkedList<Object> recvdMessage = new LinkedList<Object>();
            for (int j = 0; j < numItems; j++) {
		recvdMessage.add(oi.readObject());
            }
            String tag = (String) recvdMessage.removeFirst();
            return new Msg(fromId, myId, tag, recvdMessage);
	} catch (Exception e) {
            System.out.println(e);
            close(); return null;		
	}
    }
    
    public void setNeighbors() {
        for (int x=0; x<myId; x++){
            if (x!=myId){
                neighbors.add(x);
            }
        }
    }

    @Override
    public synchronized void handleMsg(Msg m, int src, String tag) {
        // No-op
    }

    @Override
    public synchronized void executeMsg(Msg m) {
        if (m!=null){
            handleMsg(m, m.src, m.tag);
            notifyAll();
            if (app != null) {
            app.executeMsg(m);
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

    @Override
    public List<Integer> getNeighbors() {
        return neighbors;
    }

    @Override
    public void close() {
        appFinished = true;
        connector.closeSockets();
    }

    @Override
    public void turnPassive() {
        // No-op
    }
}
