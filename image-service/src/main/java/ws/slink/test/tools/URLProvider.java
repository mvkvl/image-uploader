package ws.slink.test.tools;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class URLProvider {

	@Autowired
	private HttpServletRequest request;
	
	public String get(String suffix) {
		URL url;
		try {
			url = new URL(new String(request.getRequestURL()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return "";
		}
		String urlStr = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort();
		if (null != suffix && !suffix.isEmpty())
			urlStr += "/" + suffix;
		return urlStr;
	}

	public String get() {
		return get("");
	}

}
