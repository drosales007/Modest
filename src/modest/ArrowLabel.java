package modest;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 * 
 * Extension of JLabel that will create an arrow pointing from one server to
 * another. This will destroy itself after a set amount of time.
 * 
 */
public class ArrowLabel extends JLabel{
    
    int lifespan = Modest.msgLifespan * 1000;
    JLayeredPane jlp;
    
    public ArrowLabel(RotatedIcon icon, JLayeredPane jlp){
        super(icon);
    }
    
    public synchronized void main(){
        try{
            this.wait(lifespan);
        } catch (Exception exc){
            System.err.println(exc);
        }
    }
}