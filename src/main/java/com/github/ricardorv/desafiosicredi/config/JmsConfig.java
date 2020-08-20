package com.github.ricardorv.desafiosicredi.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;

@Configuration
public class JmsConfig {

    @Value("${activemq.broker-url}")
    private String brokerUrl;

    @Value("${destination.order}")
    private String orderDestination;

    @Bean
    public ActiveMQConnectionFactory senderConnectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory();
        activeMQConnectionFactory.setBrokerURL(brokerUrl);

        return activeMQConnectionFactory;
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        CachingConnectionFactory cachingConnectionFactory =
                new CachingConnectionFactory(senderConnectionFactory());
        cachingConnectionFactory.setSessionCacheSize(10);

        return cachingConnectionFactory;
    }

    @Bean
    public Destination orderDestination() {
        return new ActiveMQQueue(orderDestination);
    }

    @Bean
    public JmsTemplate orderJmsTemplate() {
        JmsTemplate jmsTemplate =
                new JmsTemplate(cachingConnectionFactory());
        jmsTemplate.setDefaultDestination(orderDestination());
        jmsTemplate.setReceiveTimeout(5000);

        return jmsTemplate;
    }

}
