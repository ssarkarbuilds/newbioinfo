import java.text.SimpleDateFormat;
import java.util.Date;

public class gettime {

	public static String getdate() {
		// TODO Auto-generated method stub
		String timeStamp = new SimpleDateFormat("ddMMyyHHmmss").format(new Date());
		//System.out.println(timeStamp);
		return timeStamp;
	}

}
