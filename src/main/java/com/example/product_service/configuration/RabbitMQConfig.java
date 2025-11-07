package com.example.product_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE = "product_exchange";
    //    public static final String ROUTING_KEY = "product.search";
    public static final String SEARCH_QUEUE = "product_search_queue";
    public static final String CART_QUEUE = "product_cart_queue";

    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE);
    }

//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE, true);
//    }

    //    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
//    }
// 2️⃣ Khai báo queue cho Search Service
    @Bean
    public Queue searchQueue() {
        return new Queue(SEARCH_QUEUE, true);
    }

    // 3️⃣ Khai báo queue cho Cart Service
    @Bean
    public Queue cartQueue() {
        return new Queue(CART_QUEUE, true);
    }

    @Bean
    public Binding bindSearchQueue(Queue searchQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(searchQueue).to(fanoutExchange);
    }

    @Bean
    public Binding bindCartQueue(Queue cartQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(cartQueue).to(fanoutExchange);
    }
    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}



