import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class Delete {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		//	String directory="C:\\Users\\Subham\\NEWWORKSPACE\\BIOINFOMATICS2\\input.fasta";
		//	deleteDirectory(directory);
	}
	public static void deleteDirectory(String directoryFilePath) throws IOException
	{
	    Path directory = Paths.get(directoryFilePath);

	    if (Files.exists(directory))
	    {
	        Files.walkFileTree(directory, new SimpleFileVisitor<Path>()
	        {
	            @Override
	            public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException
	            {
	                Files.delete(path);
	                return FileVisitResult.CONTINUE;
	            }

	            @Override
	            public FileVisitResult postVisitDirectory(Path directory, IOException ioException) throws IOException
	            {
	                Files.delete(directory);
	                return FileVisitResult.CONTINUE;
	            }
	        });
	    }
	    else
	    	System.out.println("No directory");
	}

}
