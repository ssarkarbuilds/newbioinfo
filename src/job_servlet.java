

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class job_servlet
 */
@WebServlet("/job_servlet")
public class job_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	FileWriter fw;
	FileReader fr;
	BufferedReader br;
	BufferedWriter bw;
	
	
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public job_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("well hello there!!").append(request.getParameter("username"));
		int i=0,i1 = 1;
		
		
		
		fr=new FileReader("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\User_details\\job_status.txt");
		br = new BufferedReader(fr);
		
		 String line = null;
		 String oldtext="";
		 int busycount=1;
		 while ((line = br.readLine()) != null) {
        	 
			 
			 if(line.contains("busy"))
				 busycount++;
        	 if(line.contains("available") && i1>=0){
        		 i=i1;
        		 i1=i1*-1;
        		 
        	 }
        	 if(i1>=0)
        		 i1++;
        	 else
        		 i1--;
        	 oldtext += line + "\r\n";   
        		 
         }
		 
        
		 


		 while(true){

		String path ="C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\OUTPUT\\JOB"+i ;

		if (Files.isDirectory(Paths.get(path)) && i<=15) {
				//System.out.println("exists ");
        	 		i++;
         			}
		else
			break;
		 }
		 
		 
    //     System.out.println("oldtext"+oldtext);
         String regex=i+" available"; String replacement=i+" busy";
      //   System.out.println(replacement);
         String newtext=oldtext.replaceAll(regex, replacement);
         FileWriter fw= new FileWriter("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\User_details\\job_status.txt",false);
 		BufferedWriter bw = new BufferedWriter(fw);
 	//	System.out.println("new "+newtext);
        fw.write(newtext);
        
         
		
		System.out.println("Job selected = "+i);
		new File("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\OUTPUT\\"+"JOB"+i+"\\NWK").mkdirs();
		new File("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\OUTPUT\\"+"JOB"+i+"\\NWK_TO_CLUST\\CLUST").mkdirs();
		new File("C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\OUTPUT\\"+"JOB"+i+"\\NWK_TO_CLUST\\Result_Analysis").mkdir();
	
		
		RequestDispatcher rd=request.getRequestDispatcher("inputpage.jsp?job="+i+"&c="+busycount);
		rd.forward(request,response);
		bw.close();
		br.close();
		}
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
