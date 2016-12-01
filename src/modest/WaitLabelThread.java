package modest;

import static modest.Modest.msgLifespan;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingWorker;

/**
 *
 * Thread for creating a busy animation in the center of the animation panel
 * 
 */
public class WaitLabelThread extends SwingWorker {
    
    JLayeredPane jlp;
    int multiplier;
    
    public WaitLabelThread(JLayeredPane jlp, int multiplier){
        this.jlp = jlp;
        this.multiplier = multiplier;
    }
    
    @Override
    protected Integer doInBackground() throws InterruptedException {
        ImageIcon icon = new ImageIcon(getClass().getResource("wait.gif"));
        JLabel lbl = new JLabel(icon);
        lbl.setBounds(250, 250, 400, 400);
        jlp.add(lbl,0,0);
        jlp.repaint();
        // Remains displayed for a specified amount of time
        try{
            Thread.sleep(msgLifespan * multiplier);
        } catch (Exception exc){
            System.err.println(exc);
        }
        jlp.remove(lbl);
        jlp.repaint();
        return 1;
    }

    @Override
    protected void done() {
    }
    
}
