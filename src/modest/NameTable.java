package modest;

import java.util.*;
import java.net.*;

public class NameTable {
	class NameEntry {
		public String procName;
		public InetSocketAddress addr;
		public NameEntry(String pName, String host, int port){
			procName = pName;
			addr = new InetSocketAddress(host, port);
		}
	}
	ArrayList<NameEntry> table = new ArrayList<NameEntry>(); 
	public synchronized InetSocketAddress search(String procName) {
		for (NameEntry entry: table)
			if (procName.equals(entry.procName)) return entry.addr;
				return null;
	}
	// returns 0 if old value replaced, otherwise 1
	public synchronized int insert(String procName, String hostName, int port) {
                int retValue = 1;
                int existing = -1;
                for (int i=0; i<table.size(); i++)
			if (procName.equals(table.get(i).procName)) { 
				existing = i;
				retValue = 0;
                        }
                if (retValue == 0) table.set(existing, new NameEntry(procName,hostName, port));
		else table.add(new NameEntry(procName,hostName, port));
                  
		/*
		NameEntry existing = null;
		for (NameEntry entry: table)
			if (procName.equals(entry.procName)) { 
				retValue = 0;
				existing = entry;
				break;
			}
                if (retValue == 0) table.remove(existing);
		table.add(new NameEntry(procName,hostName, port));
 		*/
		notifyAll();
		return retValue;
	}
	public synchronized InetSocketAddress blockingFind(String procName) {
		InetSocketAddress addr = search(procName);
		while (addr == null) {
			Util.myWait(this);
			addr = search(procName);
		}
		return addr;
	}
	public synchronized void clear() {
		table.clear();
	}
}
