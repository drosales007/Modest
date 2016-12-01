package modest;

import java.io.*;
import java.util.*;

public class Topology {
    
    private static String fp = "C:\\Users\\IBM_ADMIN\\My Documents"
                               + "\\NetBeansProjects\\FastMutex"
                               + "\\build\\classes\\fastmutex"
                               + "\\topology";
	
    public static boolean readNeighbors(int myId, List<Integer> neighbors) {
        // Returns true if read neighbors successfully
        System.out.println("Reading topology");
        try {
            Scanner sc = new Scanner(new FileReader(fp + myId));
            while (sc.hasNext()) {
                int neighbor = sc.nextInt();
                neighbors.add(neighbor);
            }
            System.out.println(neighbors.toString());
                return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        } 
    }

    public static void setComplete(int myId,
                                   List<Integer> neighbors,
                                   int numProc) {
	for (int i = 0; i < numProc; ++i) {
            if (i != myId) neighbors.add(i);
	}
    }
    
    public static void main(String [] args) {
        LinkedList<Integer> l = new LinkedList<Integer>();
	Topology.readNeighbors(Integer.parseInt(args[0]),l);
    }
}
