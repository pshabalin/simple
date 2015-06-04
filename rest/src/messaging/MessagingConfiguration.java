package messaging;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.destination.DestinationResolver;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.ConnectionFactory;

@Configuration
@EnableJms
public class MessagingConfiguration {

    @Bean(initMethod = "start")
    public BrokerService brokerService() {
        BrokerService brokerService = new BrokerService();
        brokerService.setBrokerName("main");
        brokerService.setPersistent(false);
        brokerService.setUseShutdownHook(true);
        return brokerService;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new SingleConnectionFactory(new ActiveMQConnectionFactory("vm://main?create=false"));
    }

    @Bean
    public DestinationResolver destinationResolver() {
        return new DynamicDestinationResolver();
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setDestinationResolver(destinationResolver());
        factory.setConcurrency("5");
        factory.setMessageConverter(messageConverter());
        return factory;
    }

    @Bean
    public JmsMessagingTemplate messagingTemplate() {
        JmsMessagingTemplate messagingTemplate = new JmsMessagingTemplate(connectionFactory());
        org.springframework.messaging.converter.MappingJackson2MessageConverter converter
                = new org.springframework.messaging.converter.MappingJackson2MessageConverter();
        messagingTemplate.setMessageConverter(converter);
        return messagingTemplate;
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }

    @Bean
    public MessageConverter messageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTypeIdPropertyName("class");
        return converter;
    }
}
