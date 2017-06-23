

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login_Check_Servlet
 */
@WebServlet("/Login_Check_Servlet")
public class Login_Check_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Login_Check_Servlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//res.getWriter().append("Served at: ").append(req.getContextPath());
		String email=req.getParameter("username");
		String pass=req.getParameter("password");
		BufferedReader br = null;
		FileReader fr = null;
		String line=null;
		String arr[]; boolean flag=false;
		try {
			br = new BufferedReader(new FileReader("C:/Users/Subham/NEWWORKSPACE/newbioinfo/WebContent/User_details/users.txt"));
			while ((line = br.readLine()) != null) {
				//System.out.println(line);
				arr=line.split(" ");
				if(arr[0].equalsIgnoreCase(email)&& arr[1].equalsIgnoreCase(pass))
					flag=true;
			}
			if(flag)
			{
			//	req.setAttribute("bingo", "Just checking");
				RequestDispatcher rd=req.getRequestDispatcher("job_servlet");
				rd.forward(req,res);
			}
			else
				res.getWriter().append("Not a valid user ");
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

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
