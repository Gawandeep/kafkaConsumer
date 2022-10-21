package com.example.kafkaConsumer.Controller;

import com.example.kafkaConsumer.dto.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Component
public class ConsumerController {

    List<String> messages=new ArrayList<>();
    Order orderFromKafka=null;

    @GetMapping("/consume")
    public List<String> consume(){
        return messages;
    }

    @GetMapping("/consumeOrder")
    public Order consumeOrder(){
        return orderFromKafka;
    }

    @KafkaListener(groupId = "StringGroup",topics = "kafkaTopic4",containerFactory = "ContainerFactory")
    public List<String> consumeMsg(@Payload String data){
    messages.add(data);
    return messages;
    }

    @KafkaListener(groupId = "OrderGroup",topics = "userTopic",containerFactory = "OrderContainerFactory")
    public Order consumeMsg(@Payload Order order){
        System.out.println("Order: "+order);
        orderFromKafka=order;
        return orderFromKafka;
    }

}
