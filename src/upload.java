import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

 
/**
 * A Java servlet that handles file upload from client.
 * @author www.codejava.net
 */
@WebServlet("/upload")
@MultipartConfig
public class upload extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    
    private static final int THRESHOLD_SIZE     = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    FileOutputStream outputStream=null;
 
    /**
     * handles file upload via HTTP POST method
     */
    @Override
	protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // checks if the request actually contains upload file
    	
    	
    	int jid=Integer.parseInt(request.getParameter("jobid"));
    	int busycount=Integer.parseInt(request.getParameter("busycount"));
    	String DATE=gettime.getdate();
    	
    	
        if (!ServletFileUpload.isMultipartContent(request)) {
            PrintWriter writer = response.getWriter();
            writer.println("Request does not contain upload data");
            writer.flush();
            return;
        }
         
        // configures upload settings
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(THRESHOLD_SIZE);
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
         
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_REQUEST_SIZE);
         
        // constructs the directory path to store upload file
        String uploadPath = "C:\\Users\\Subham\\NEWWORKSPACE\\newbioinfo\\WebContent\\INPUT\\JOB"+jid+"_"+DATE+".fasta";
        
         
        
        Part filePart = request.getPart("img"); // Retrieves <input type="file" name="file">
        
        InputStream fileContent = (InputStream) filePart.getInputStream();
        
        try {
    		

    		// write the inputStream to a FileOutputStream
        	outputStream = new FileOutputStream(new File(uploadPath));

    		int read = 0;
    		byte[] bytes = new byte[1024];

    		while ((read = fileContent.read(bytes)) != -1) {
    			outputStream.write(bytes, 0, read);
    		}

    	//	System.out.println("Done!");

    	} catch (IOException e) {
    		e.printStackTrace();
    	} 
        finally{
        			
        }
        
         
      
      //  getServletContext().getRequestDispatcher("/inputpage.jsp").forward(request, response);
        
        if(busycount<4){   
      	  pyrun.pythoncall(jid,DATE);  //calling the clustering algo when server is not busy
      	  Input_Process_Servlet.releasejob(jid);      //releasing job after work done.
  }
        else{
        	response.getWriter().append("Your request has been noted down. Thank you.");
        	queue.push(jid, DATE);
        }
       
    }
}