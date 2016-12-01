package modest;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * Extension of JLabel that will create a critical section object for display.
 * 
 */
public class CSLabel extends JLabel{
    
    int width;
    int height;
    int x;
    int y;
    int centerX;
    int centerY;
    
    // Lets us know if there are any servers in the critical section
    boolean isEmpty = true;
    
    // Lets us know which servers are in the critical section. If the index is 
    // true, the server associated with the index is in the cs
    boolean[] srvStatus;
    
    // String that is displayed in the critical section
    String srvs_in = "";
    
    // HTML tags to format critical section display
    String goodHTML = "<html><font color='rgb(153,153,153)',size=18>"
                      + "<b><p align='right'>";
    String badHTML = "<html><font color='red',size=18><b><p align='right'>";
    String closeHTML = "</p></b></font></html>";
    
    public CSLabel(ImageIcon icon, int width, int height, Point coords,
                   int num_srv){
        super(icon);
        this.width = width;
        this.height = height;
        this.x = coords.x - width/2;
        this.y = coords.y - height/2;
        this.centerX = coords.x;
        this.centerY = coords.y;
        this.srvStatus = new boolean[num_srv];
        this.setHorizontalTextPosition(JLabel.CENTER);
    }
    
    public synchronized void addServer(int srv_id){
        // Adds a server to the critical section and displays list of servers
        System.out.println("Adding server " + srv_id + " to the "
                           + "critical section");
        String csText = "";
        
        // Determine how the server should be displayed in the CS
        if (isEmpty){
            srvStatus[srv_id] = true;
            isEmpty = false;
            srvs_in = "" + srv_id;
            csText = goodHTML + srvs_in + closeHTML;
        } else {
            srvStatus[srv_id] = true;
            updateServersIn();
            if (srvs_in.length() > 1){
                csText = badHTML + srvs_in + closeHTML;
            } else {
                csText = goodHTML + srvs_in + closeHTML;
            }
        }
        
        // Update the CS
        this.setText(csText);
    }
    
    public synchronized void removeServer(int srv_id){
        // Removes a server from the critical section and displays new list
        srvStatus[srv_id] = false;
        setIsEmpty();
        srvs_in = srvs_in.replace("" + srv_id, "");
        String csText = "";
        
        // Determine how the server should be displayed in the CS
        if (srvs_in.length() > 1){
            csText = badHTML + srvs_in + closeHTML;
        } else {
            csText = goodHTML + srvs_in + closeHTML;
        }
        
        // Update CS
        this.setText(csText);
    }
    
    public void setIsEmpty(){
        // Sets whether or not the critical section is empty
        boolean empty = true;
        for (boolean srv : srvStatus){
            if (srv){
                empty = false;
            }
        }
        isEmpty = empty;
    }
    
    public void updateServersIn(){
        // Updates property with all servers currently in the CS
        srvs_in = "";
        for (int idx=0; idx<srvStatus.length; idx++){
            if (srvStatus[idx]){
                srvs_in = srvs_in + idx;
            }
        }
    }
}
