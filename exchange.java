import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class exchange extends Thread {
	int flag,flag1=0;
	LinkedHashMap<String, List<String>> contactdata = null;
public void run() {
	calling call;
	Map<String,List<String>> map = this.contactdata;
	for(Entry<String, List<String>> templist: map.entrySet()) {
		System.out.println(templist.getKey()+":"+ templist.getValue());
	}
	
	for(Entry<String, List<String>> templist2: map.entrySet()) {
	call = new calling(templist2.getValue(),this); 
	call.setName(templist2.getKey());
	call.start();
	}
	System.out.println();
	}
	 
	public exchange(LinkedHashMap<String, List<String>> list) {
		contactdata = list;
		}
	public void sendintro(String rec,String send, long timestamp) {
		System.out.println(rec +" received intro message from "+send+" ["+ timestamp+"]");
	}
	
	public  void sendreply(String send,String rec, long timestamp) {
		System.out.println(rec+" received reply message from "+send +" ["+ timestamp+"]");
	}
	

	
	public void shutdownprocess(String name) throws InterruptedException {
		// TODO Auto-generated method stub
		 synchronized (this)
		 {
			if(flag<contactdata.size()-1)
			{
				flag++;
				   try 
				   { this.wait(); }
				   catch(Exception e) {}
			}
			else {
				this.notifyAll();
			     }}
			System.out.println("\nProcess "+ name +" has received no calls for 1 second, ending...\n");
			
			flag1++;
			if(flag1==flag+1)
			 {
				System.out.println("Master has received no replies for 1.5 seconds, ending...");
			 }
		
			}		
	
	public static void main(String[] args) throws IOException {
		System.out.println("**Calls to be made**");
try {
		BufferedReader br = new BufferedReader(new FileReader("calls.txt"));

  LinkedHashMap<String, List<String>> splitlist = new LinkedHashMap<String,List<String>>();
  
  		String str,st;
		while ((str = br.readLine()) != null) {
			
			st = str.replaceAll("[{}\\[\\]. ]", "");
			
    		ArrayList<String> contacts = new ArrayList(Arrays.asList( st.split(",")));
    		String sender = contacts.get(0);
    		contacts.remove(0);       		
    		splitlist.put(sender,contacts);  
        }
		exchange ex = new exchange(splitlist);
  	    ex.start();
	}
  	  catch (IOException e)
	   {
       System.err.println("File not found");
      }
	}
	
	
	}
