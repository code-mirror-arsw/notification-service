package com.code_room.notification_service.domain.mapper;


import com.code_room.notification_service.domain.model.FcmMessage;
import com.code_room.notification_service.domain.model.enums.NotificationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Map;

public class NotificationsMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
            .enable(SerializationFeature.INDENT_OUTPUT);

    public static FcmMessage mapJsonToFcm(String jsonString) {
        try {
            if (jsonString.startsWith("\"") && jsonString.endsWith("\"")) {
                jsonString = objectMapper.readTree(jsonString).asText();
                System.out.println("JSON deserializado nuevamente: " + jsonString);
            }

            JsonNode rootNode = objectMapper.readTree(jsonString);
            String to = rootNode.path("to").asText();
            NotificationType source = NotificationType.valueOf(rootNode.path("source").asText());

            JsonNode dataNode = rootNode.path("data");
            Map<String, String> data = objectMapper.convertValue(dataNode, objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class));

            return FcmMessage.builder()
                    .to(to)
                    .source(source)
                    .data(data)
                    .build();

        } catch (JsonProcessingException e) {
            System.err.println("❌ Error al deserializar JSON a FcmMessage: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



    public static String mapFcmToJson(FcmMessage fcmMessage) {
        try {
            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            System.err.println("Error al serializar FcmMessage a JSON: " + e.getMessage());
            e.printStackTrace();
            return "{}"; // Devolver JSON vacío en caso de error
        }
    }
}
