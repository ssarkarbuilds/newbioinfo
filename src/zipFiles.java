import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class zipFiles
{
    List<String> fileList;
    

    zipFiles(){
	fileList = new ArrayList<String>();
    }

    public static void doZip(int jid)
    {
        String OUTPUT_ZIP_FILE = "C://Users//Subham//NEWWORKSPACE//newbioinfo//WebContent//OUTPUT//JOB"+jid+"//JOB"+jid+".zip";
        String SOURCE_FOLDER = "C://Users//Subham//NEWWORKSPACE//newbioinfo//WebContent//OUTPUT//JOB"+jid;
    	zipFiles appZip = new zipFiles();
    	appZip.generateFileList(new File(SOURCE_FOLDER),SOURCE_FOLDER);
    	appZip.zipIt(OUTPUT_ZIP_FILE,SOURCE_FOLDER,jid);
    }

    /**
     * Zip it
     * @param zipFile output ZIP file location
     */
    public void zipIt(String zipFile,String SOURCE_FOLDER,int jid){
    	
    	

     byte[] buffer = new byte[1024];

     try{

    	FileOutputStream fos = new FileOutputStream(zipFile);
    	ZipOutputStream zos = new ZipOutputStream(fos);

    	//System.out.println("Output to Zip : " + zipFile);

    	for(String file : fileList){

    	//	System.out.println("File Added : " + file);
    		ZipEntry ze= new ZipEntry(file);
        	zos.putNextEntry(ze);

        	FileInputStream in =
                       new FileInputStream(SOURCE_FOLDER + File.separator + file);

        	int len;
        	while ((len = in.read(buffer)) > 0) {
        		zos.write(buffer, 0, len);
        	}

        	in.close();
    	}

    	zos.closeEntry();
    	//remember close it
    	zos.close();

    	System.out.println("Files Zipped for JOB "+jid);
    }catch(IOException ex){
       ex.printStackTrace();
    }
   }

    /**
     * Traverse a directory and get all files,
     * and add the file into fileList
     * @param node file or directory
     */
    public void generateFileList(File node,String SOURCE_FOLDER){

    	//add file only
	if(node.isFile()){
		
		fileList.add(generateZipEntry((node.getAbsoluteFile().toString()),SOURCE_FOLDER));
		
		
	}

	if(node.isDirectory()){
		String[] subNote = node.list();
		for(String filename : subNote){
			
			generateFileList(new File(node, filename),SOURCE_FOLDER);
		}
	}
	

    }

    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    private String generateZipEntry(String file,String SOURCE_FOLDER){
    	return file.substring(SOURCE_FOLDER.length()-5, file.length());
    }
}