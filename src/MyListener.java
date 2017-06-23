import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;  

  

@WebListener
public class MyListener implements ServletContextListener{  
  
	
private ScheduledExecutorService scheduler;
public void contextInitialized(ServletContextEvent arg0) {  
try{  
	System.out.println("Project Deployed");
	System.out.println("Size of queue= "+queue.al.size());
	scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(new cleanTask(), 0, 1, TimeUnit.HOURS);	//performs cleaning of old files
    scheduler.scheduleAtFixedRate(new queueTask(), 0, 10, TimeUnit.SECONDS);  //handles jobs from queue
          
      

}catch(Exception e){e.printStackTrace();}  
}  
  
public void contextDestroyed(ServletContextEvent arg0) {  
System.out.println("project undeployed");  
scheduler.shutdownNow();
          
}  
}  