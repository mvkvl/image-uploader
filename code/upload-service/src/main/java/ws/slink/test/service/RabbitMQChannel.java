package ws.slink.test.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface RabbitMQChannel {

	@Output("images.preview.queue")
    MessageChannel getChannel();
	
}
