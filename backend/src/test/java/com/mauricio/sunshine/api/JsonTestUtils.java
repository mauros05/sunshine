package com.mauricio.sunshine.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTestUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String readJsonField(String json, String fieldName) throws Exception {
        JsonNode node = mapper.readTree(json);
        return node.get(fieldName).asText();
    }

}
