package br.com.zup.desafiomercadolivre.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RequisitionBuilder {

    public static ResultActions postRequisition(String url, Object content, ObjectMapper objectMapper, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content, objectMapper)));
    }

    private static String toJson(Object requestBody, ObjectMapper objectMapper) throws JsonProcessingException {
        return objectMapper.writeValueAsString(requestBody);
    }
}
