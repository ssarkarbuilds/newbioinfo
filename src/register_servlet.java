
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class register_servlet
 */
@WebServlet("/register_servlet")
public class register_servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public register_servlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		String name=req.getParameter("username");
		String pass=req.getParameter("password");
		BufferedWriter bw = null;
		FileWriter fw = null;
		
		
		try {
			
			bw = new BufferedWriter(new FileWriter("C:/Users/Subham/NEWWORKSPACE/newbioinfo/WebContent/User_details/users.txt",true));
			bw.newLine();
			bw.append(name+" "+pass);
			
				res.getWriter().append("<html>");
				res.getWriter().append("New user registered.");
				res.getWriter().append("<a href='loginpage.html'>Click here to go to login page</a></html>");
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		finally{
			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
