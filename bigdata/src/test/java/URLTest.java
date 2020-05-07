import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午4:46
 */
public class URLTest {

	@Test
	public void _url() {
		try {
			URL url = new URL("jdbc:mysql://localhost:3306/uc?useUnicode=true&characterEncoding=utf8&&failOverReadOnly=false&useSSL=false");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void _concatWithInt() {
		System.out.println("asdfas" + -1);
	}
}
