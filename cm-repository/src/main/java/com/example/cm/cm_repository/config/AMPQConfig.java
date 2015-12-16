package com.example.cm.cm_repository.config;

import com.example.cm.cm_repository.alerts.CMSAlertHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;

public class AMPQConfig {

    public static final String queueName = "cms.queue";
    public static final String exchangeName = "cms.exchange";
    public static final String routingKey = "cms.key";

    static final String AMPQ_HOST = "localhost";
    static final int AMPQ_PORT = 5672;

    /* Shared */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(AMPQ_HOST, AMPQ_PORT);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public Queue cmsAlertQueue() {
        return new Queue(queueName);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setRoutingKey(routingKey);
        template.setQueue(queueName);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }


    @Bean
    public TopicExchange cmsExchange() {
        TopicExchange exchange = new TopicExchange(exchangeName);
        return exchange;
    }

    /* Consumer */
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter messageListenerAdapter) {
        SimpleMessageListenerContainer container
                = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(messageListenerAdapter);
        return container;
    }


    /**
     * A POJO to be used as a listener
     */
    @Bean
    public CMSAlertHandler receiver() {
        return new CMSAlertHandler();
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Wrap a POJO - CMSAlertHandler - as a listener
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(CMSAlertHandler receiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "handleMessage");
        adapter.setMessageConverter(jsonMessageConverter());
        return adapter;
    }
}
