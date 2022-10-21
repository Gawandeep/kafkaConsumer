package com.example.kafkaConsumer.config;

import com.example.kafkaConsumer.dto.Order;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class ConsumerConfiguration {

    @Bean
    public ConsumerFactory<String,String> consumerFactory(){
        Map<String,Object> configs=new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "StringGroup");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(configs,new StringDeserializer(),new StringDeserializer());
    }

    @Bean(name="ContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String,String> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,String> concurrentKafkaListenerContainerFactory=new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    public ConsumerFactory<String, Order> orderConsumerFactory(){
        JsonDeserializer<Order> deserializer=new JsonDeserializer<>(Order.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);
        Map<String,Object> configs=new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "OrderGroup");
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(),deserializer);
    }

    @Bean(name="OrderContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String,Order> orderKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String,Order> concurrentKafkaListenerContainerFactory=new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(orderConsumerFactory());
        return concurrentKafkaListenerContainerFactory;
    }

}
