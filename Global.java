import java.io.File;

public class Global {
	public static User[] user = new User[100];
	public static int userCount = 0;
	public static final int NONE = 255;
	public static final String PUBLIC_PATH = "D:\\rsa2\\public\\";
	public static final String PRIVATE_PATH = "D:\\rsa2\\private\\";
	
	public static void init() {
		File dir = new File(PUBLIC_PATH);
		if (!dir.exists()) dir.mkdirs();
		dir = new File(PRIVATE_PATH);
		if (!dir.exists()) dir.mkdirs();
	}
	
	public static int addUser() {
		user[userCount] = new User(userCount);
		File dir = new File(PRIVATE_PATH + "\\" + userCount);
		if (!dir.exists()) dir.mkdirs();
		userCount++;
		System.out.println("当前已有" + userCount + "个用户");
		return userCount;
	}
}
