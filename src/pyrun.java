import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class pyrun {
		
	
	public static void pythoncall(int jid,String DATE) throws IOException {
		// TODO Auto-generated method stub
		 String ret;
         String command = "cmd.exe /c cd "+"C:/Users/Subham/NEWWORKSPACE/newbioinfo/WebContent"+" && python 3gClustWEB.py JOB"+jid+"_"+DATE+".fasta";
        // System.out.println("executing python now");
 		Process p = Runtime.getRuntime().exec(command);
 		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
 		while((ret=in.readLine())!=null)
 			System.out.println(ret);  
 		
 		zipFiles.doZip(jid);
 		javaMail.sendMail(jid);
 		
	
	}
	
}
