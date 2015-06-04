package messaging;

import model.Bean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;

public class MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @JmsListener(destination = "mainQueue")
    public void processMessage(@Payload Bean message) {
          log.debug("Got a message {} -> {}", message.name, message.value);
    }
}
