package ws.slink.test.url;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name="images.url-provider.type", havingValue="self", matchIfMissing=false)
public class SelfURLProvider implements URLProvider {

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
