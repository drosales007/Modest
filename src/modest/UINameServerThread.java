package modest;

import java.util.List;
import javax.swing.SwingWorker;

/**
 *
 * Thread to go off and handle the Nameserver
 * 
 */
public class UINameServerThread extends SwingWorker<Integer, String>{
    
    NameServer nsT;
    
    public UINameServerThread(){
    }
    
    @Override
    protected Integer doInBackground(){
        String[] args = new String[0];
        nsT = new NameServer();
        nsT.main(args);
        return 1;
    }

    @Override
    protected void process(final List<String> chunks){
        // Updates the pane area
        for (String str : chunks) {
             System.out.println(str);
         }
    }
    
    @Override
    protected void done() {
        nsT.t.interrupt();
    }
}
