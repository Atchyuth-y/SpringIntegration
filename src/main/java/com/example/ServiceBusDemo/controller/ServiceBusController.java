package com.example.ServiceBusDemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.azure.messaging.servicebus.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

    @RestController
    @RequestMapping("/api")
    public class ServiceBusController {

        @PostMapping("/SentToServiceBus")
        public ResponseEntity<String> sendToServiceBus(@RequestBody String payload) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonData = objectMapper.readTree(payload);
                String commitId = jsonData.get("after").asText();

                String connectionString = "Endpoint=sb://servicebus8.servicebus.windows.net/;SharedAccessKeyName=policy1;SharedAccessKey=kStCecNVjThMcJXsS0OxxZMZkrpU4V0M6+ASbD+eLcY=;EntityPath=javaqueue";
                ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                        .connectionString(connectionString)
                        .sender()
                        .queueName("javaqueue")
                        .buildClient();

                ServiceBusMessage message = new ServiceBusMessage(payload);
                message.setContentType("application/json");

                senderClient.sendMessage(message);
                senderClient.close();

                return ResponseEntity.ok("Data Sent To Topic");
            } catch (IOException | ServiceBusException ex) {
                return ResponseEntity.ok(ex.toString());
            }
        }

}
