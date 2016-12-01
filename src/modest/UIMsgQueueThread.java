package modest;

import java.awt.Color;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * Thread to handle GUI updates
 * 
 */
public class UIMsgQueueThread extends SwingWorker<Integer, String> {

    private JTextPane textPane;
    private JScrollPane scrollPane;
    private Style style;

    public UIMsgQueueThread(JTextPane pane, JScrollPane scrollbar) {
        this.textPane = pane;
        this.style = textPane.addStyle("S1", null);
        this.scrollPane = scrollbar;
    }

    @Override
    protected Integer doInBackground() throws InterruptedException {
        System.out.println("Starting UI Listener Thead.");
        boolean finished = false;
        
        // Check for updates until the simulation is terminated
        while (!finished) {
            Thread.sleep(10);
            // Requests an update
            publish();
        }
        return 1;
    }

    @Override
    protected void process(final List<String> chunks) {
        // Updates the textPane area
        String txt = textPane.getText();
        String new_msg = "";
        StyledDocument doc = textPane.getStyledDocument();
        
        // If the queue has messages, grab the next in line
        if (!Modest.mqueue.isEmpty()) {
            new_msg = Modest.mqueue.dequeue() + "\n";
            txt = txt + new_msg;
            handleMsg(new_msg);
            try {
                // Adds message to the logging pane in the correct format
                doc.insertString(doc.getLength(), new_msg, style);
            } catch (Exception exc) {
                System.err.print(exc);
            }
        }
    }

    @Override
    protected void done() {
    }
    
    void handleMsg(String msg){
        // Handle each type of message differently in a new thread
        if (msg.startsWith("ack")){
            StyleConstants.setForeground(style, Color.blue);
            try{
                UIArrowThread a = new UIArrowThread(
                        Modest.jlp,
                        "ack",
                        Integer.parseInt(msg.split(" ")[3]),
                        Integer.parseInt(msg.split(" ")[5]));
                Modest.threadPool.execute(a);
            } catch (Exception exc){
                System.err.println(exc);
            }
        } else if (msg.startsWith("request")){
            StyleConstants.setForeground(style, Color.red);
            try{
                UIArrowThread a = new UIArrowThread(
                        Modest.jlp,
                        "request",
                        Integer.parseInt(msg.split(" ")[3]),
                        Integer.parseInt(msg.split(" ")[5]));
                Modest.threadPool.execute(a);
            } catch (Exception exc){
                System.err.println(exc);
            }
        } else if (msg.startsWith("release")){
            StyleConstants.setForeground(style, Color.green);
            try{
                UIArrowThread a = new UIArrowThread(
                        Modest.jlp,
                        "release",
                        Integer.parseInt(msg.split(" ")[3]),
                        Integer.parseInt(msg.split(" ")[5]));
                Modest.threadPool.execute(a);
            } catch (Exception exc){
                System.err.println(exc);
            }
        } else if (msg.startsWith("DROPPED")){
            StyleConstants.setForeground(style, Color.magenta);
        } else if (msg.startsWith("KILLED")){
            StyleConstants.setForeground(style, Color.magenta);
        }else {
            StyleConstants.setForeground(style, Color.black);
            try{
                UIArrowThread a = new UIArrowThread(
                        Modest.jlp,
                        "generic",
                        Integer.parseInt(msg.split(" ")[3]),
                        Integer.parseInt(msg.split(" ")[5]));
                Modest.threadPool.execute(a);
            } catch (Exception exc){
                //System.err.println(exc);
            }
        }
    }
}
