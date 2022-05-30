import java.util.*;
import java.io.*;

public class Console {
	public static void main(String[] args) {
		System.out.println("RSA Secure Transfer");
		Scanner s = new Scanner(System.in);
		Global.init();
		Global.addUser();
		int user = 0;
		for (;;) {
			switch (s.next()) {
			// new 创建新用户，依序编号
			case "new":
				Global.addUser();
				break;
			// user n 切换到用户n
			case "user":
				user = s.nextInt();
				System.out.println("已切换至用户" + user);
			// list 显示文件列表
			case "list":
				System.out.println("用户" + user + "：查看文件列表");
				System.out.println("下载权限/上传者签名/文件名");
				FileInfo[] info;
				try {
					info = Global.user[user].getFileList();
					for (FileInfo i : info) {
						System.out.println((i.encrypt == Global.NONE ? "公开/" : ("仅用户" + i.encrypt + "可下载/"))
								+ (i.sign == Global.NONE ? "无签名/" : ("用户" + i.sign + "签名/"))
								+ i.file.getName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// push filename encrypt sign 以当前用户身份，上传文件filename，encrypt不为-1时用用户encrypt的加密公钥加密，sign不为0时用自己的签名私钥签名
			case "push":
				String name = s.next();
				int toId = s.nextInt();
				int sign = s.nextInt();
				try {
					Global.user[user].sendFile(new File(name), toId, sign != 0);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// pull filename 以当前用户身份，尝试下载、校验、解密文件filename，若签名校验错误会报错，解密错误则无法获取文件
			case "pull":
				name = s.next();
				try {
					Global.user[user].receiveFile(new File(name));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("无效命令");
			}
			
		}
	}
}
