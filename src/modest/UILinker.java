package modest;

import java.util.*; import java.io.*;

/**
 *
 * Extends the functionality of Linker.class
 * This becomes the programs hook to implement network delays and failures
 * 
 */
public class UILinker implements MsgHandler {

    public int myId;
    public int n; // number of neighbors including myself
    Connector connector = null;
    MsgHandler app = null;// upper layer
    MsgHandler comm = null;// lower layer
    public boolean appFinished = false;
    public List<Integer> neighbors = new ArrayList<Integer>();	
    public Properties prop = new Properties();
    
    public int delay = 0;
    public int csDelay = 1;
    boolean req = false;
    boolean rel = false;
    public boolean dropMsg = false;

    public UILinker(String args[]) throws Exception { 
        String basename = args[0];
        myId = Integer.parseInt(args[1]);
        
        // If there are topology files, the neighbors will be set from those
        // else we assign the neighbors manually
        //
        // TODO: Neighbors may need to be set differently for each algorithm.
        //       Auto generate a topology for the respective algorithms.
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
        Modest.Connectors.put(myId, connector);
    }

    @Override
    public void init(MsgHandler app){
	this.app = app;	
	for (int pid : neighbors) {
            new UIListenerThread(pid, this).start();
        }
    }

    @Override
    public void sendMsg(int destId, Object ... objects) {
        String msg;
        
        // Drop the message if requested, replicates network failure
        if (dropMsg){
            dropMsg = false;
            msg = "DROPPED " + objects[0] + ":  source=" + myId 
                  + "   target=" + destId + "          [" + objects[1] + "]";
            Modest.mqueue.enqueue(msg);
            System.out.println("Message from " + myId + " to " + destId
                               + " was lost");
        } else {
            try {
                if (delay > 0){
                    // Sleep for the delayed time to replicate network latency
                    Thread.sleep(delay * 1000);
                    System.out.println("Message delayed " + delay + " seconds "
                                       + "for server " + myId);
                }
            } catch (Exception exc){
                System.err.println(exc);
            }
            
            // Add the message to the queue
            msg = objects[0] + " sent from " + myId + " to " + destId
                  + "          [" + objects[1] + "]";
            int j = neighbors.indexOf(destId);
            try {
                LinkedList<Object> objectList = Util.getLinkedList(objects);
                ObjectOutputStream os = connector.dataOut[j];
                os.writeObject(Integer.valueOf(objectList.size()));
                for (Object object : objects){
                    os.writeObject(object);
                }
                if (Modest.messagingEnabled){
                    Modest.mqueue.enqueue(msg);
                }
                os.flush();
            } catch (java.net.SocketException exc){
                // The destination server might have died or been killed so we no-op
            } catch (Exception exc) {
                System.err.println(exc);
                exc.printStackTrace();
                close();
            }
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
        } catch (java.io.EOFException exc) {
            return null;
        } catch (java.net.SocketException exc) {
            return null;
        } catch (Exception exc) {
            System.out.println(exc);
            exc.printStackTrace();
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
        try {
            Thread.sleep(delay * 50);
        } catch (Exception exc){
            System.err.println(exc);
        }
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
