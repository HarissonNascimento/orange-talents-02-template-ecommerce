package br.com.zup.desafiomercadolivre.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class RequisitionBuilder {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ResultActions postImages(String url, Long productId, MockMvc mockMvc, byte[] archiveBytes) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.multipart(url, productId)
                .file("images", archiveBytes));
    }

    public static ResultActions postRequest(String url, Object content, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content)));
    }

    public static ResultActions postRequestWithCsrf(String url, Object content, MockMvc mockMvc) throws Exception {
        return mockMvc.perform(post(url)
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content)));
    }

    private static String toJson(Object requestBody) throws JsonProcessingException {
        return objectMapper.writeValueAsString(requestBody);
    }
}
