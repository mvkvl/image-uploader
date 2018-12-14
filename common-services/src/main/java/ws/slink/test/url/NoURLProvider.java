package ws.slink.test.url;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name="images.url-provider.type", havingValue="no", matchIfMissing=true)
public class NoURLProvider implements URLProvider {

	@Override
	public String get(String suffix) {
		return "";
	}

	@Override
	public String get() {
		return get("");
	}

}
