
public class Global {
	public static User[] user = new User[100];
	public static int userCount = 0;
	public static final int NONE = 255;
	
	public static int addUser() {
		user[userCount] = new User(userCount);
		userCount++;
		return userCount;
	}
}
