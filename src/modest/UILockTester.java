package modest;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 *
 * Extended implementation of the base LockTester.class to add multiple
 * simulation modes, algorithm importing, and CS delays
 * 
 */
public class UILockTester {

    Lock lock = null;
    int myId;
    
    public UILockTester(){
    }
    
    public void main(String[] args) {
        Class classLoaded = null;
        MsgHandler comm = null;
        String fp = "";
	try {
            comm = new UILinker (args);
            myId = comm.getMyId();
            Modest.srvLinkers.put(myId, (UILinker) comm);
            // Try to compile and or import the algorithm if this custom alg
            if (args[4].equals("True")){
                
                // Do some work so we can determine what type of file this is
                String file = "";
                String[] path_split = args[3].split("\\\\");
                String[] dir_split = Arrays.copyOfRange(path_split, 0,
                                                        path_split.length-1);
                String dir = String.join("\\", dir_split);
                
                file = path_split[path_split.length-1];
                String[] file_split = file.split("\\.");
                
                // If it is a java class file we can load it. If not, we try to
                // compile it. Only java class files are supported but this lays
                // the groundwork for java files
                if (file_split[file_split.length-1].equals("class")){
                    file = file_split[0];
                } else if (file_split[file_split.length-1].equals("java")){
                    fp = dir + "\\" + file_split[0] + ".class";
                    File f = new File(fp);
                    if(f.exists() && !f.isDirectory()) {
                        file = file_split[0];
                    } else {
                        // Try to compile
                        String command = "javac " + args[3];
                        System.out.println("Compiling " + f.toString() + " "
                                           + command);
                        java.lang.Process proc =
                            Runtime.getRuntime().exec(command);
                        int rc = proc.waitFor();
                        System.out.println("Return code: " + rc);
                        file = file_split[0];
                    }
                } else {
                    // throw an error
                }
                System.out.println("Loading " + file);
                File dir_file = new File(dir);
                URL dir_url =  dir_file.toURL();
                URLClassLoader urlClassLoader = URLClassLoader.newInstance(
                    new URL[] {dir_url});
                classLoaded = urlClassLoader.loadClass(file);
            } else {
                fp = "modest." + args[3];
                classLoaded = Class.forName(fp);
            }
            Constructor mainCons = classLoaded.getConstructor(MsgHandler.class);
            lock = (Lock) mainCons.newInstance(comm);
            lock.init(null);
            // Decide which mode we will run in and do it
            if (Modest.automated){
                runAutomation();
            } else {
                runManual();
            }
	} catch (java.net.SocketException exc){
            // The destination server might have died or been killed so we no-op
        } catch (Exception exc) {
            System.err.println(exc);
            exc.printStackTrace();
            lock.close();
	} finally {
            // lock.close()
        }
        String msg = "Process " + comm.getMyId() + " finished.";
        Modest.mqueue.enqueue(msg);
	System.out.println(msg);
    }
    
    public static String millisToTime(long millis){
        // Convert milliseconds to hh:mm:ss
        return String.format("%02d:%02d:%02d", 
            TimeUnit.MILLISECONDS.toHours(millis) % 24,
            TimeUnit.MILLISECONDS.toMinutes(millis) -  
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - 
                TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(millis)));   
    }
    
    public static String getSuffix(int i) {
        // Gets a suffix for numbers
        String suffix;
        switch (i){
                    case 0: 
                        suffix = "st";
                        break;
                    case 1: 
                        suffix = "nd";
                        break;
                    case 2: 
                        suffix = "rd";
                        break;
                    default: 
                        suffix = "th";
                        break;
                }
        return suffix;
    }
    
    void runAutomation(){
        // Runs an automated simulation for the requested number of iterations
        String msg;
        String suffix;
        int iters = Modest.iterations;
        for (int i = 0; i < iters; i++) {
            suffix = getSuffix(i);
            // Wait some time before we request CS again
            Util.mySleep(1500);
            lock.requestCS();
            // Add event to the queue
            long t = System.currentTimeMillis();
            msg = "Server " + myId + " entered the CS for the "
                  + (i + 1) + suffix + " time at " + millisToTime(t);
            Modest.csLabel.addServer(myId);
            Modest.mqueue.enqueue(msg);
            System.out.println(msg);
            int csDelay = Modest.srvLinkers.get(myId).csDelay;
            // Stay in the CS for the required time
            Util.mySleep(csDelay * 1000);
            System.out.println("Server " + myId + " in CS for "
                               + csDelay + " seconds");
            t = System.currentTimeMillis();
            Modest.csLabel.removeServer(myId);
            // Add event to queue and leave
            msg = "Server " + myId + " left the CS at " 
                  + millisToTime(t);
            Modest.mqueue.enqueue(msg);
            lock.releaseCS();
            System.out.println(msg);
        }
    }
    
    void runManual(){
        int count = 0;
        String msg;
        String suffix;
        
        // Run forever
        while(true){
            
            // If a request is made, send the request
            if (Modest.srvLinkers.get(myId).req){
                Modest.srvLinkers.get(myId).req = false;
                lock.requestCS();
                long t = System.currentTimeMillis();
                suffix = getSuffix(count);
                msg = "Server " + myId + " entered the CS for the "
                      + (count + 1) + suffix + " time at " + millisToTime(t);
                Modest.csLabel.addServer(myId);
                Modest.mqueue.enqueue(msg);
                System.out.println(msg);
                
                // Track how many times this process has entered the CS
                count++;
            }
            
            // If a release was made, release the CS
            if (Modest.srvLinkers.get(myId).rel){
                Modest.srvLinkers.get(myId).rel = false;
                long t = System.currentTimeMillis();
                Modest.csLabel.removeServer(myId);
                msg = "Server " + myId + " left the CS at " 
                      + millisToTime(t);
                Modest.mqueue.enqueue(msg);
                lock.releaseCS();
            }
            
            // Small wait
            try{
                Thread.sleep(10);
            } catch (Exception exc){
                System.err.println(exc);
            }
        }
    }
}
