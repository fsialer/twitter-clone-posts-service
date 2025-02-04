package com.fernando.ms.posts.app.infrastructure.adapter.output.bus;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.posts.app.application.ports.output.PostBusOutputPort;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.output.bus.models.request.CreateMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class PostBusAdapter implements PostBusOutputPort {
//    private final ServiceBusSenderClient sender;
//    private final ObjectMapper objectMapper;
//
//    public PostBusAdapter(ServiceBusSenderClient sender, ObjectMapper objectMapper) {
//        this.sender = sender;
//        this.objectMapper = objectMapper;
//    }
    @Override
    public void sendNotification(Post post) {
//        try{
//            CreateMessageRequest createMessageRequest= CreateMessageRequest.builder()
//                    .userId(post.getUser().getId())
//                    .content(post.getContent())
//                    .datePost(post.getDatePost().toString())
//                    .targetId(post.getId())
//                    .targetType("POST")
//                    .build();
//            String messageContent = this.objectMapper.writeValueAsString(createMessageRequest);
//            sender.sendMessage(new ServiceBusMessage(messageContent));
//        }catch (RuntimeException | JsonProcessingException ex){
//            throw new RuntimeException(ex);
//        }
    }
}
