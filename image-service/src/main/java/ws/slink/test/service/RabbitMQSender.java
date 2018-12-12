package ws.slink.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(RabbitMQChannel.class)
public class RabbitMQSender {

	@Autowired
	RabbitMQChannel rabbitMQChannel;
	
	public void send(String fileName) {
		rabbitMQChannel.getChannel().send(MessageBuilder.withPayload(fileName).build());
	}

}
