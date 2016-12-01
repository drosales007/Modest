package modest;

/**
 * 
 * Class representing a 2 dimensional coordinate
 * 
 */
public class Point {
    
    int x;
    int y;
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setX(int newX){
        x = newX;
    }
    
    public void setY(int newY){
        y = newY;
    }
}
