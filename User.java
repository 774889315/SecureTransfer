import java.io.*;
import java.security.MessageDigest;
import java.util.Map;

public class User {
	public final int id;
	public String publicSignatureKey;
	public String publicEncryptionKey;
	private String privateSignatureKey;
	private String privateEncryptionKey;
	
	public User(int id) {
		this.id = id;
		Map<String, String> SignatureKey = RSAUtil.createKeys(512);
		Map<String, String> EncryptionKey = RSAUtil.createKeys(512);
		publicSignatureKey = SignatureKey.get("publicKey");
		publicEncryptionKey = EncryptionKey.get("publicKey");
		privateSignatureKey = SignatureKey.get("privateKey");
		privateEncryptionKey = EncryptionKey.get("privateKey");
	}
	
	public void sendFile(File file, int toId, boolean encrypt, boolean sign, OutputStream out) throws Exception {
		System.out.println("��ʼ�����ļ�");
		long t = System.currentTimeMillis();
		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream f = new FileInputStream(file);
		byte buffer[] = new byte[4096];
		if (encrypt) out.write(toId);
		else out.write(Global.NONE);
		if (sign) out.write(id);
		else out.write(Global.NONE);
		for (;;) {
			int c = f.read(buffer);
			if (c <= 0) break;
			byte[] s = new byte[c];
			System.arraycopy(buffer, 0, s, 0, c);
			if (encrypt) s = RSAUtil.publicEncrypt(s, RSAUtil.getPublicKey(Global.user[toId].publicEncryptionKey));
			md.update(s);
			out.write(s.length & 0xFF);
			out.write((s.length & 0xFF00) >>> 8);
			out.write(s);
		}
		if (sign) {
			out.write(0);
			out.write(0);
			out.write(RSAUtil.privateEncrypt(md.digest(), RSAUtil.getPrivateKey(privateSignatureKey)));
		}
		System.out.println("�ļ����ͳɹ�����ʱ" + (System.currentTimeMillis() - t) + "����\n");
		f.close();
	}
	
	public void receiveFile(InputStream in, File file) throws Exception {
		System.out.println("��ʼ�����ļ�");
		long t = System.currentTimeMillis();
		MessageDigest md = MessageDigest.getInstance("MD5");
		FileOutputStream f = new FileOutputStream(file);
		int encrypt = in.read();
		int sign = in.read();
		if (encrypt != Global.NONE) {
			System.out.println("���ļ��Ѽ��ܣ�idΪ" + id + "���û��ɽ���");
			if (encrypt != id) {
				System.err.println("���棺����id�����������޷����ܣ�");
			}
		}
		for (;;) {
			int length = in.read();
			if (length == -1) break;
			length |= in.read() << 8;
			if (length == 0) break;
			byte[] s = new byte[length];
			if (in.read(s) < length) {
				System.err.println("�����ļ���ȡ����");
			}
			md.update(s, 0, length);
			if (encrypt != Global.NONE) {
				s = RSAUtil.privateDecrypt(s, RSAUtil.getPrivateKey(privateEncryptionKey));
			}
			f.write(s);
		}
		if (sign != Global.NONE) {
			System.out.println("���ļ��ѱ�ǩ����ǩ����idΪ" + sign);
			boolean ok = true;
			byte ss[] = new byte[4096];
			byte[] signature = new byte[in.read(ss)];
			System.arraycopy(ss, 0, signature, 0, signature.length);
			byte[] s = RSAUtil.publicDecrypt(signature, RSAUtil.getPublicKey(Global.user[sign].publicSignatureKey));
			byte[] s1 = md.digest();
			if (s.length != s1.length) ok = false;
			else for (int i = 0; i < s.length; i++) {
				if (s[i] != s1[i]) {
					ok = false;
					break;
				}
			}
			if (ok) System.out.println("ǩ��У��ɹ���");
			else System.err.println("���棺ǩ��У��ʧ�ܣ�");
		}
		System.out.println("�ļ����ճɹ�����ʱ" + (System.currentTimeMillis() - t) + "����\n");
		f.close();
	}
}
