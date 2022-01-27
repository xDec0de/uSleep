package es.xdec0de.usleep.utils.files;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class FileUtils {
	
	protected static File copyInputStreamToFile(InputStream inputStream) {
		File file = new File("messages.yml");
	    try (FileOutputStream outputStream = new FileOutputStream(file)) {
	    	int read;
	        byte[] bytes = new byte[1024];
	        while ((read = inputStream.read(bytes)) != -1) {
	        	outputStream.write(bytes, 0, read);
	        }
	        return file;
	    } catch (IOException e) {
	    	e.printStackTrace();
			return null;
		}
	}
}
