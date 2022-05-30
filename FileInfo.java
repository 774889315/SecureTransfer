import java.io.*;

public class FileInfo {
	File file;
	int encrypt;
	int sign;
	
	FileInfo(File file) throws Exception {
		this.file = file;
		FileInputStream f = new FileInputStream(file);
		encrypt = f.read();
		sign = f.read();
		f.close();
	}
}
