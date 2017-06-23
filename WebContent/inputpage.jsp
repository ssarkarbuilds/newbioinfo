<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Search Page</title>
</head>
<body>

<% String jobid=request.getParameter("job");
	String busycount=request.getParameter("c");
%>
<form action="Input_Process_Servlet" method="GET">
Enter the protein ID :
<input type="text" name="id" placeholder="Enter Protein ID here">
<input type="hidden" name="jobid" value='<%=jobid%>'>
<input type="hidden" name="busycount" value='<%=busycount%>'>
<input type="submit" value="SUBMIT" >
<br>

</form>
<form action="upload" method="POST" enctype="multipart/form-data">
Upload the Fasta file :
<input type="file" name="img" >
<input type="hidden" name="jobid" value='<%=jobid%>'>
<input type="hidden" name="busycount" value='<%=busycount%>'>
<input type="submit" value="UPLOAD" >
</form>
</body>
</html>