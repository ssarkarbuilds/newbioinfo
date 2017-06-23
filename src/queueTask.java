import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TimerTask;

public class queueTask extends TimerTask {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		
		
	//	System.out.println("reached queue task!");
		String jobDetails; int jid; String DATE;
		
		
		FileReader fr = null;
		try {
			fr = new FileReader("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\User_details\\job_status.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		String line=null;	int busycount=1;
		try {
			while ((line = br.readLine()) != null) {
				 
				 
				 
				if(line.contains("busy"))
					 busycount++;   
					 
			 }
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//main task starts here.
		
		if(queue.al.size()!=0 && busycount<4){
			
			jobDetails=queue.pop();
			
			jid=Integer.parseInt(jobDetails.split("_")[0]);
			DATE=jobDetails.split("_")[1];
			
			try {
				pyrun.pythoncall(jid, DATE);
				Input_Process_Servlet.releasejob(jid);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
			
		}
		
		
	}

	
	
}
