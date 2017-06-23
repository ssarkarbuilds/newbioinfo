

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Input_Process_Servlet
 */
@WebServlet("/Input_Process_Servlet")
public class Input_Process_Servlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Input_Process_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String DATE=gettime.getdate();
		
		 String pids; String pid[];pids=request.getParameter("id"); int jid=Integer.parseInt(request.getParameter("jobid"));
		  
		 int busycount= Integer.parseInt(request.getParameter("busycount"));
		 int f=0;
		 
		 
		 
		    pid=pids.split(",");
		    
		   
		    for(int i=0;i<pid.length;i++)
		          {
		        if(pid[i].length()!=6 || (pid[i].charAt(0)<65 || pid[i].charAt(0)>90) || pid.length<2 )
		                 {
		        	
		            f=1;   //Invalid ID
		                 }
		             }
		                 
		                 if(f==0)
		                  {
		            
		                  String ur=genUrl(pid);
		                  //System.out.print(ur);
		                  URL url = new URL(ur);
		                  
		                      FileWriter writer= new FileWriter("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\INPUT\\JOB"+jid+"_"+DATE+".fasta",true);
		                      BufferedWriter bw = new BufferedWriter(writer);
		                      URLConnection con = url.openConnection();
		                      InputStream is =con.getInputStream();
		                      BufferedReader br=new BufferedReader(new InputStreamReader(is));
		                    try {
		                    
		                    String line = null;
		                    
		                    // read each line and write to System.out
		                    while ((line = br.readLine()) != null) {
		                        
		                       // System.out.print("Value of line: "+line);
		                        bw.write(line);
		                                    bw.newLine();
		                                   
		                                  
		                    }
		                    
		                    
		            		
		            		
		                    }
		                    
		                    catch(Exception e){
		                                    System.out.println(e);
		                                  }
		                                finally
		                                        {
		                                            bw.close();
		                                        }
		        }
		    
		        else{
		            
		        	response.getWriter().append("One or more IDs were invalid. Try again.");
		         }	
		                
		                 
		        if(busycount<4){   
		            	  pyrun.pythoncall(jid,DATE);  //calling the clustering algo when server is not busy
		            	  releasejob(jid);      //releasing job after work done.
		        }
		        
		        else{
		        	// if server is busy , add request to a queue.
		        	response.getWriter().append("Your request has been noted down. Thank you.");
		        	queue.push(jid, DATE);
		        	
		        }
		        
		        	
	}
	
	



	public static void releasejob(int i) throws IOException {
		// TODO Auto-generated method stub

		
		
		
		FileReader fr = new FileReader("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\User_details\\job_status.txt");
		BufferedReader br = new BufferedReader(fr);
		
		 String line = null;
		 String oldtext="";
		 while ((line = br.readLine()) != null) {
        	 
        	 oldtext += line + "\r\n";   
        		 
         }
		 
        
         String regex=i+" busy"; String replacement=i+" available";
         String newtext=oldtext.replaceAll(regex, replacement);
         FileWriter fw= new FileWriter("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\User_details\\job_status.txt",false);
 		BufferedWriter bw = new BufferedWriter(fw);
        fw.write(newtext);
        br.close();
        bw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	public String genUrl(String pid[])
    {
      String a="http://www.uniprot.org/uniprot/?query=id:"+pid[0];
        for(int i=1;i<pid.length;i++)
         {
           // actual trial link http://www.uniprot.org/uniprot/?query=id:P04637+OR+id:P63244+OR+id:P08246&columns=sequence&format=fasta&sort=score
              a=a+"+OR+id:"+pid[i];
            }
       
         a=a+"+&columns=sequence&format=fasta&sort=score";
        return a;
      }

}
