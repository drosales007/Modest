package modest;

import static java.lang.Math.atan;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import java.util.List;
import javax.swing.JLayeredPane;
import javax.swing.SwingWorker;
import javax.swing.ImageIcon;
import static java.lang.Math.abs;
import static java.lang.Math.abs;

/**
 * 
 * Creates a worker thread to display an arrow from one server to another.
 * The arrow is in the form of a JLabel with extended functionality to
 * destroy itself after a specified amount of time. This swing worker
 * returns to the thread pool and will remain around for additional use when
 * it completes.
 * 
 **/
public class UIArrowThread extends SwingWorker<Integer, String>{
    
    JLayeredPane jlp;
    String type;
    int source;
    int target;
    Point start;
    Point end;
    Point[] points;
    int quadrant;
    
    ArrowLabel arrow;
    
    public UIArrowThread(JLayeredPane jlp, String msg_type, int source,
                         int target){
        this.jlp = jlp;
        this.type = msg_type;
        this.source = source;
        this.target = target;
        this.points = getPoints();
        this.start = points[0];
        this.end = points[1];
    }
    
    @Override
    protected Integer doInBackground(){
        // Get the correct arrow
        String img = "arrow_" + type + ".png";
        if (sqrt(pow(abs(points[0].x-points[1].x),2) +
                 pow(abs(points[0].y-points[1].y),2)) < 370){
            img = "arrow_" + type + "_short.png";
        } else {
            img = "arrow_" + type + ".png";
        }
        // Rotate the arrow and display it
        int theta = getRotation(points);
        ImageIcon ic = new ImageIcon(getClass().getResource(img));
        RotatedIcon icon = new RotatedIcon(ic, theta);
        ArrowLabel arrow = new ArrowLabel(icon, jlp);
        arrow.setBounds(points[0].x - 200, points[0].y - 200, 400, 400);
        jlp.add(arrow);
        
        // Wait to reach lifespan then remove arrow
        arrow.main();
        jlp.remove(arrow);
        jlp.repaint();
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
    
    private Point[] getPoints(){
        // Method returning the center points of the source and target servers
        Point s = new Point(Modest.srvLabels.get(source).centerX,
                            Modest.srvLabels.get(source).centerY);
        Point t = new Point(Modest.srvLabels.get(target).centerX,
                            Modest.srvLabels.get(target).centerY);
        return new Point[] {s, t};
    }
    
    public int getRotation(Point[] points){
        // Finds the angle to rotate an arrow based on server positions
        int theta = 0;
        double distX = abs(points[0].x - points[1].x);
        double distY = abs(points[0].y - points[1].y);
        
        if (points[0].x < points[1].x && points[0].y < points[1].y){
            // Points Southeast
            theta = (int) toDegrees(atan(distY/distX));
            quadrant = 1;
        } else if (points[0].x < points[1].x && points[0].y > points[1].y){
            // Points Northeast
            theta = 360 - ((int) toDegrees(atan(distY/distX)));
            quadrant = 2;
        } else if (points[0].x > points[1].x && points[0].y < points[1].y){
            // Points Southwest
            theta = 180 - ((int) toDegrees(atan(distY/distX)));
            quadrant = 3;
        } else if (points[0].x > points[1].x && points[0].y > points[1].y){
            // Points Northwest
            theta = 180 + ((int) toDegrees(atan(distY/distX)));
            quadrant = 4;
        } else if (points[0].x == points[1].x && points[0].y < points[1].y){
            // Points South
            theta = 90;
            quadrant = 5;
        } else if (points[0].x == points[1].x && points[0].y > points[1].y){
            // Points North
            theta = 270;
            quadrant = 6;
        } else if (points[0].x < points[1].x && points[0].y == points[1].y){
            // Points East
            theta = 0;
            quadrant = 7;
        } else if (points[0].x > points[1].x && points[0].y == points[1].y){
            // Points West
            theta = 180;
            quadrant = 8;
        }
        
        /**System.out.println("x1=" + points[0].x + " y1=" + points[0].y + " x2="
                           + points[1].x + " y2=" + points[1].y + " distX=" 
                           + distX + " distY=" + distY + " theta=" + theta
                           + " quadrant=" + quadrant);**/
        return theta;
    }
}