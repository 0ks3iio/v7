package net.zdsoft.framework.utils;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

/*
 * 这是一个可以把给定密码转变成密码明文显示，也可以从密码明文还原
 * 你可以使用以下写法构造：
 * PWD pwd=new PWD();
 * pwd.setPassword("password");
 * 或
 * PWD pwd=new PWD("password");
 * 
 * 然后调用方法pwd.encode()可以得到转变后的密码明文形式
 * 调用方法pwd.decode(String mPWD)可以从密码明文得到原密码
 */

public class PWD {
	public String pwdString; // string of the password
	public int pwdLength; // length of the password
	public byte[] pwdBytes; // byte array of the password
	public byte[] encodedBytes1, encodedBytes2;
	public int pos1, pos2;
	public int check;
	public byte[] encodedArray;
	public static final char[] CHS = { 'L', 'K', 'J', '4', 'D', 'G', 'F', 'V', 'R', 'T', 'Y', 'B', 'N', 'U', 'P', 'W',
			'3', 'E', '5', 'H', 'M', '7', 'Q', '9', 'S', 'A', 'Z', 'X', '8', 'C', '6', '2' };

	/*
	 * http://192.168.22.35:8085/passport/scriptLogin?action=login&username=meiyan&
	 * server=1663301&root=1&cookieSaveType=3&password=
	 * 9d94e29464094d741fa05cd6eae287474fd9b13baa89e8367e9f4a6b2dac6172d73f769d&
	 * loginMode=0&verifyCode=&url=http://192.168.22.35&id=1499938407797 You can use
	 * PWD(),then call setPassword(String s) param(s) The password
	 */
	public PWD() {
		pwdString = null;
	}

	/*
	 * You can use PWD(String s) param(s) The password
	 */
	public PWD(String pwdS) {
		this.pwdString = pwdS;
		pwdLength = pwdS.length();
		pwdBytes = new byte[pwdLength];
		pwdBytes = pwdS.getBytes();
	}

	public void setPassword(String pwdS) {
		pwdString = pwdS;
		pwdLength = pwdS.length();
		pwdBytes = new byte[pwdLength];
		pwdBytes = pwdS.getBytes();
	}

	public static String encodeIfNot(String password) {
		password = PWD.decode(password);
		PWD pwd = new PWD(password);
		return pwd.encode();
	}

	/*
	 * return the changed password
	 */
	public String encode() {
		/*
		 * if ((pwdBytes.length%2)==0) { encodedBytes1 = new byte[pwdBytes.length];
		 * encodedBytes2 = new byte[pwdBytes.length]; } else { encodedBytes1 = new
		 * byte[pwdBytes.length+1]; encodedBytes2 = new byte[pwdBytes.length-1]; }
		 */

		encodedBytes1 = new byte[30];
		encodedBytes2 = new byte[30];

		int n1 = 0, n2 = 0;
		for (int i = 0; i < pwdBytes.length; i++) {
			if ((i + 1) % 2 != 0) // 奇数位
			{
				encodedBytes1[n1++] = (byte) get32Hi((int) pwdBytes[i] * 4);
				// System.out.println(""+(int)pwdBytes[i]*4+":"+get32Hi((int)pwdBytes[i]*4));
				encodedBytes1[n1++] = (byte) get32Low((int) pwdBytes[i] * 4);
				// System.out.println(""+(int)pwdBytes[i]*4+":"+get32Low((int)pwdBytes[i]*4));
			} else // 偶数位
			{
				encodedBytes2[n2++] = (byte) get32Hi((int) pwdBytes[i] * 4);
				encodedBytes2[n2++] = (byte) get32Low((int) pwdBytes[i] * 4);
			}
		}

		while (n1 < 30)
			encodedBytes1[n1++] = (byte) getRandom(32);

		while (n2 < 30)
			encodedBytes2[n2++] = (byte) getRandom(32);

		pos1 = getRandom(pwdBytes.length);
		pos2 = getRandom(pwdBytes.length);
		// System.out.println(""+pos1+":"+pos2+"\n");
		sort(encodedBytes1, pos1);
		sort(encodedBytes2, pos2);
		check = (sumSqual(encodedBytes1) + sumSqual(encodedBytes2)) % 32;

		encodedArray = new byte[64];
		encodedArray[0] = (byte) pos1;
		encodedArray[1] = (byte) pos2;
		System.arraycopy(encodedBytes1, 0, encodedArray, 2, encodedBytes1.length);
		System.arraycopy(encodedBytes2, 0, encodedArray, 2 + encodedBytes1.length, encodedBytes2.length);
		encodedArray[encodedArray.length - 2] = (byte) pwdLength;
		encodedArray[encodedArray.length - 1] = (byte) check;
		byte[] ps = new byte[encodedArray.length];

		for (int i = 0; i < encodedArray.length; i++)
			ps[i] = (byte) CHS[encodedArray[i]];

		return new String(ps);
	}

	/*
	 * return the old password form changed password param(s) the changed password
	 */
	public static String decode(String s) {
		if (s == null || s.trim().length() != 64) {
			return s;
		}
		byte[] sb = new byte[s.length()];
		byte[] bb = new byte[s.length()];

		sb = s.getBytes();

		for (int i = 0; i < sb.length; i++) {
			for (int j = 0; j < 32; j++) {
				if (((byte) CHS[j]) == sb[i]) {
					bb[i] = (byte) j;
					break;
				}
			}
		}

		int sl = (int) bb[bb.length - 2];
		int p1 = (int) bb[0];
		int p2 = (int) bb[1];

		byte[] bb1 = new byte[30];
		byte[] bb2 = new byte[30];

		@SuppressWarnings("unused")
		int bb1l, bb2l;
		if (sl % 2 == 0) {
			bb1l = sl;
			bb2l = sl;
		} else {
			bb1l = sl + 1;
			bb2l = sl - 1;
		}

		/*
		 * byte[] bb1,bb2; if (sl%2==0) { bb1=new byte[sl]; bb2=new byte[sl]; } else {
		 * bb1=new byte[sl+1]; bb2=new byte[sl-1]; }
		 */

		// System.out.println(""+bb1.length+":"+bb2.length);
		// System.out.println(""+p1+":"+p2);
		System.arraycopy(bb, 2, bb1, 0, bb1.length);
		System.arraycopy(bb, 2 + bb1.length, bb2, 0, bb2.length);

		unsort(bb1, p1);
		unsort(bb2, p2);
		byte[] oldb = new byte[sl];
		for (int i = 0; i < sl; i += 2) {
			oldb[i] = (byte) (getIntFrom32((int) bb1[i], (int) bb1[i + 1]) / 4);
			if (i + 1 < bb2l)
				oldb[i + 1] = (byte) (getIntFrom32((int) bb2[i], (int) bb2[i + 1]) / 4);
		}

		return new String(oldb);
	}

	public void test() {
		String ss = encode();
		System.out.println("The password be changed is :");
		System.out.println(ss);
		System.out.println("The source password is:");
		System.out.println(decode(ss));
	}

	private int sumSqual(byte[] b) {
		int sum = 0;
		for (int i = 0; i < b.length; i++)
			sum += (int) Math.pow(b[i], 2);
		return sum;
	}

	private int getRandom(int max) {
		return (int) (Math.random() * max);
	}

	private void sort(byte[] b, int pos) {
		if (pos < 0 || pos > b.length)
			System.out.println("pos is not validate");
		byte[] tmp = new byte[pos];
		System.arraycopy(b, 0, tmp, 0, pos);
		System.arraycopy(b, pos, b, 0, b.length - pos);
		System.arraycopy(tmp, 0, b, b.length - pos, pos);
	}

	private static void unsort(byte[] b, int pos) {
		if (pos < 0 || pos > b.length)
			System.out.println("pos is not validate");

		byte[] tmp = new byte[pos];
		System.arraycopy(b, b.length - pos, tmp, 0, pos);
		System.arraycopy(b, 0, b, pos, b.length - pos);
		System.arraycopy(tmp, 0, b, 0, pos);
	}

	private int get32Low(int num) {
		return (int) num % 32;
	}

	private int get32Hi(int num) {
		return (int) num / 32;
	}

	private static int getIntFrom32(int hi, int low) {
		return hi * 32 + low;
	}

	public static void main(String[] args) throws Exception {

//    	List<String> list = FileUtils.readLines(new File("C:\\Users\\user\\Documents\\3.txt"));
//    	File f = new File("C:\\Users\\user\\Documents\\2.txt");
//    	List<String> file = new ArrayList<>();
//    	for(String s : list) {
//    		String[] ss = s.split("\\t");
//    		String pwd = ss[1].trim();
//    		pwd = PWD.decode(pwd);
//    		String pwd2 = DigestUtils.md5Hex(pwd) + DigestUtils.sha1Hex(pwd);
//    		file.add(ss[0] + "	" + ss[1] + "	" + pwd2 + "	" + ss[0] +"," + pwd2);
//    	}
//    	FileUtils.writeLines(f, file);

//    	System.out.print(PWD.decode("G4NFMF8PL34W9UULZMKCUJQLF4NPLFDFRF3FSVLWR376W2Q7N3P3B73RZSKWRFN9"));

//    	Collection<File> files = FileUtils.listFiles(new File("C:\\Users\\user\\Documents\\workspaces\\v7\\framework\\src\\java"), org.apache.commons.lang3.ArrayUtils.toArray("java", "JAVA"), true);
//    	Set<String> set = new HashSet<>();
//    	for(File file : files) {
//    		List<String> ss = FileUtils.readLines(file, "UTF8");
//    		for(String s : ss) {
//    			s = s.trim();
//    			if(StringUtils.isBlank(s))
//    				continue;
//    			if(!StringUtils.startsWith(s, "import ")) 
//    				continue;
//    			if(StringUtils.startsWith(s, "public"))
//    				break;
//    			s = StringUtils.substringAfter(s, "import ");
//    			if(StringUtils.startsWithAny(s, "freemarker.", "sum.", "java.", "java.util.", "org.apache.","net.zdsoft.", "java.io.", "javax.", "java.lang.", "java.net.", "org.springframework."))
//   					continue;
//    			
//    			set.add(s);   				
//    		}
//    	}
//    	for(String s : set) {
//    		System.out.println(s);
//    	}

		File file = new File("C:\\Users\\user\\Documents\\workspaces\\v7\\remote-lib\\remote-basedata");
		Collection<File> files = FileUtils.listFiles(file, new String[] { "class" }, true);
		for (File f : files) {
			String path = f.getPath();
			path = StringUtils.substringAfter(path, "remote-basedata\\");
			path = path.replaceAll("\\\\", ".").replaceAll(".class", "");
			String name = f.getName().replaceAll(".class", "");
			name = StringUtils.lowerCase(name.substring(0, 1)) + name.substring(1);
			if (StringUtils.contains(name, "Remote")) {
				System.out.println("<dubbo:reference interface=\"" + path + "\" id=\"" + name + "\" />	");
//				System.out.println("<dubbo:service interface=\"" + path + "\" ref=\"" + name + "\" />");
			}
		}

	}

}
