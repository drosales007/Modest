package modest;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 
 * Extension of JLabel that will create a server object for display.
 * 
 */
public class ServerLabel extends JLabel{
    
    int id;
    int width;
    int height;
    int x;
    int y;
    int centerX;
    int centerY;
    
    public ServerLabel(ImageIcon icon, int id, int width, int height,
                       Point coords){
        super(icon);
        String openHTML = "<html><font color='white',size=18><b>";
        String closeHTML = "</b></font></html>";
        this.setIconTextGap(-20);
        this.setText(openHTML + id + closeHTML);
        this.id = id;
        this.width = width;
        this.height = height;
        this.x = coords.x - width/2;
        this.y = coords.y - height/2;
        this.centerX = coords.x;
        this.centerY = coords.y;
        this.setBounds(x, y, width, height);
    }
}
