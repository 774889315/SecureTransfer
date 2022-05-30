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
			// new �������û���������
			case "new":
				Global.addUser();
				break;
			// user n �л����û�n
			case "user":
				user = s.nextInt();
				System.out.println("���л����û�" + user);
			// list ��ʾ�ļ��б�
			case "list":
				System.out.println("�û�" + user + "���鿴�ļ��б�");
				System.out.println("����Ȩ��/�ϴ���ǩ��/�ļ���");
				FileInfo[] info;
				try {
					info = Global.user[user].getFileList();
					for (FileInfo i : info) {
						System.out.println((i.encrypt == Global.NONE ? "����/" : ("���û�" + i.encrypt + "������/"))
								+ (i.sign == Global.NONE ? "��ǩ��/" : ("�û�" + i.sign + "ǩ��/"))
								+ i.file.getName());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// push filename encrypt sign �Ե�ǰ�û���ݣ��ϴ��ļ�filename��encrypt��Ϊ-1ʱ���û�encrypt�ļ��ܹ�Կ���ܣ�sign��Ϊ0ʱ���Լ���ǩ��˽Կǩ��
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
			// pull filename �Ե�ǰ�û���ݣ��������ء�У�顢�����ļ�filename����ǩ��У�����ᱨ�����ܴ������޷���ȡ�ļ�
			case "pull":
				name = s.next();
				try {
					Global.user[user].receiveFile(new File(name));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("��Ч����");
			}
			
		}
	}
}
