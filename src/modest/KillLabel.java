package modest;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * Extension of JLabel for creating a kill image
 * 
 */
public class KillLabel extends JLabel{
    
    public KillLabel(ImageIcon icon, Point coords){
        super(icon);
        this.setBounds(coords.x, coords.y, 64, 64);
    }
}