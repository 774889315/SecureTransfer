import java.io.*;

public class Test {
	public static void main(String[] args) throws Exception {
		File tmp = new File("D:\\tmp");
		Global.addUser();
		Global.addUser();
		Global.addUser();
		Global.user[0].sendFile(new File("D:\\mj_20220526_111631.bmp"), 1, false, true, new FileOutputStream(tmp));
		Global.user[1].receiveFile(new FileInputStream(tmp), new File("D:\\1.bmp"));
		Global.user[2].receiveFile(new FileInputStream(tmp), new File("D:\\2.bmp"));
	}
}
