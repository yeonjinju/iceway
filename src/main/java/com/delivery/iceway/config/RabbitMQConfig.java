package com.delivery.iceway.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue.name}")
    private String queueName;
    @Value("${spring.rabbitmq.exchange.name}")
    private String topicExchangeName;
    @Value("${spring.rabbitmq.routing.key}")
    private String routingKey;

    /**
     * RabbitMQ의 큐(Queue)를 생성합니다.
     *
     * @return 새로 생성된 Queue 객체를 반환합니다.
     */
    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    /**
     * TopicExchange를 생성합니다.
     * 
     * @return 새로 생성된 TopicExchange 객체를 반환합니다.
     */
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    /**
     * Queue와 TopicExchange를 바인딩합니다.
     *
     * @param queue Queue 객체
     * @param exchange TopicExchange 객체
     * @return 바인딩된 Binding 객체를 반환합니다.
     */
    @Bean
    Binding bindingDefault(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    /**
     * RabbitTemplate을 설정합니다.
     *
     * @param connectionFactory RabbitMQ 연결 객체
     * @return 설정된 RabbitTemplate 객체를 반환합니다.
     */
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }
}
