package ws.slink.test.url;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name="images.url-provider.type", havingValue="static", matchIfMissing=false)
public class StaticURLProvider implements URLProvider {

	@Value("${images.url-provider.base-url}")
	private String baseUrl;
	
	@Override
	public String get(String suffix) {
		return baseUrl  +"/" + suffix;
	}

	@Override
	public String get() {
		return get("");
	}

}
