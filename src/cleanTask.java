

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.TimerTask;

public class cleanTask extends TimerTask
{

	
	
	public void run() {
		// TODO Auto-generated method stub
		//for(int)
		System.out.println("Cleaning....");
		File folder = new File("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\INPUT");
		  FileFilter filter = new FileFilter() {
	            @Override
	            public boolean accept(File pathname) {
	            	
	            	if(pathname.getName().contains("JOB") && pathname.getName().endsWith(".fasta"))
	            		return pathname.isFile();
	            	else
	            		return false;
	            }	
	         };
		 
		File[] listOfFiles = folder.listFiles(filter);
		
		for(int i=0;i<listOfFiles.length;i++){
			
			
				String s=listOfFiles[i].getName();
				
				
				//	System.out.println("Bingo "+(s.split("_")[1]).split(".fasta")[0]);
				
					try {
						comparetime((s.split("_")[1]).split(".fasta")[0],s,s.split("_")[0]);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
			
			
		//	System.out.println(listOfFiles[i].getName());
			
		}
		
		
	}
	static void comparetime(String filetime,String fileName,String jobName) throws IOException{
		
		
		int date=Integer.parseInt(filetime.substring(0, 2));
		int month=Integer.parseInt(filetime.substring(2, 4));
	//	int year=Integer.parseInt(filetime.substring(4, 6));
	//	System.out.println("date "+date+" month "+month+" Year "+year);         
		
		LocalDate today = LocalDate.now();
	//	System.out.println("todAY "+today);
		
		LocalDate folderDate = LocalDate.of(2017, month, date);
		 
		Period p = Period.between(folderDate , today);
		//System.out.println("TIme of day "+p.getDays());
		
		if(p.getDays()>=1){
			Delete.deleteDirectory("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\INPUT\\"+fileName);
			Delete.deleteDirectory("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\OUTPUT\\"+jobName);
			System.out.println("Cleaned "+jobName);
		}
		 
		
		
	}
		
	

}
