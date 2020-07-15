
import java.util.*;

class calling  extends Thread
{
	
	List<String> receivers = null;
    exchange ex;
	
   public calling(List<String> list , exchange ex)
	{  
	   this.receivers = list;
	   this.ex= ex;
		
	}
   private void send(String send, String rcv, long timestamp) {
		// TODO Auto-generated method stub
		ex.sendintro(send, rcv,timestamp);
		try
		 {
		   Thread.sleep((long)(Math.random() * 1000));
		 } 
		 catch (InterruptedException e)
		 {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		ex.sendreply(send, rcv, timestamp);		
	}
	 public void run()
	  {	 
		
		 for(String rec:receivers)
		 { 
			
			 try
			 {
			   Thread.sleep((long)(Math.random() * 1000));
			 } 
			 catch (InterruptedException e)
			 {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }	
			 
			 send(this.getName(),rec,System.currentTimeMillis());		 
		 }
			 
			  try 
			   {
				Thread.sleep(1000);
				 ex.shutdownprocess(this.getName());
			   }
			  catch (InterruptedException e)
			   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			   }			 
	  }
   
}
		
	