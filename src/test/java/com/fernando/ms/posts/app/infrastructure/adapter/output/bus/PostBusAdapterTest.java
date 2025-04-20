package com.fernando.ms.posts.app.infrastructure.adapter.output.bus;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.ms.posts.app.domain.models.Post;
import com.fernando.ms.posts.app.infrastructure.adapter.output.bus.models.request.CreateMessageRequest;
import com.fernando.ms.posts.app.utils.TestUtilPost;
import com.fernando.ms.posts.app.utils.TestUtilsMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PostBusAdapterTest {
    @Mock
    private ServiceBusSenderClient sender;
    @Autowired
    private ObjectMapper objectMapper;

    //@InjectMocks
    private PostBusAdapter postBusAdapter;

    @BeforeEach
    void setUp() {
        sender = Mockito.mock(ServiceBusSenderClient.class);
        objectMapper = new ObjectMapper();
        postBusAdapter = new PostBusAdapter(sender, objectMapper);
    }

    @Test
    @DisplayName("When sendNotification is called, expect message to be sent")
    void whenSendNotificationIsCalled_ExpectMessageToBeSent() throws JsonProcessingException {
        Post post = TestUtilPost.buildPostMock();

        CreateMessageRequest createMessageRequest = CreateMessageRequest.builder()
                .userId(post.getUserId())
                .targetId(post.getId())
                .content(post.getContent())
                .datePost(post.getDatePost().toString())
                .targetType("POST")
                .build();

        String expectedMessageContent = objectMapper.writeValueAsString(createMessageRequest);

        postBusAdapter.sendNotification(post);

        ArgumentCaptor<ServiceBusMessage> messageCaptor = ArgumentCaptor.forClass(ServiceBusMessage.class);
        verify(sender, times(1)).sendMessage(messageCaptor.capture());

        ServiceBusMessage capturedMessage = messageCaptor.getValue();
        assertEquals(expectedMessageContent, capturedMessage.getBody().toString());
    }
}
