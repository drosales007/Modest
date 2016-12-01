package modest;

import java.util.List;
import javax.swing.SwingWorker;

/**
 * 
 * Creates a worker thread to start a server. This swing worker returns to the 
 * thread pool and will remain around for additional use when it completes.
 * 
 **/
public class UIServerThread extends SwingWorker<Integer, String>{
    
    private final String id;
    private final String num_procs;
    private final String algorithm;
    private final String base_name;
    private final String custom;
    
    UILockTester uiLock;
    
    public UIServerThread(String[] args){
        this.base_name = args[0];
        this.id = args[1];
        this.num_procs = args[2];
        this.algorithm = args[3];
        this.custom = args[4];
    }

    @Override
    protected Integer doInBackground(){
        System.out.println("Starting Server " + id);
        String[] args = {base_name, id, num_procs, algorithm, custom};
        uiLock = new UILockTester();
        uiLock.main(args);
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
        //uiLock.lock.close();
    }
}
