package modest;

/**
 *
 * Implementation of the base ListenerThread.class to be used with the GUI
 */
public class UIListenerThread extends Thread {
    int channel;
    UILinker comm = null;
    public UIListenerThread(int channel, UILinker comm) {
        this.channel = channel;
        this.comm = comm;
    }
    
    @Override
    public void run() {
        while (!comm.appFinished) {
            // System.out.println("Listening on " + channel);
            Msg m = comm.receiveMsg(channel);
            comm.executeMsg(m);
        }
    }
}
