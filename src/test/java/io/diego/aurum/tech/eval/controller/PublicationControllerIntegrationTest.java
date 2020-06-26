package io.diego.aurum.tech.eval.controller;

import io.diego.aurum.tech.eval.entity.Publication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/snippets")
public class PublicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenPost_withValidPublication_thenOk() throws Exception {
        List<Integer> validOnes = IntStream.rangeClosed(1, 5).boxed().collect(Collectors.toList());
        for (Integer validOne : validOnes) {
            File payload = ResourceUtils.getFile("classpath:payload/publication/valid/" + validOne + ".json");
            String content = new String(Files.readAllBytes(payload.toPath()));
            mockMvc.perform(MockMvcRequestBuilders.post("/publication")
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(document("publication/save/" + validOne));
        }
    }

    @Test
    public void whenPost_withEmptyJson_thenError() throws Exception {
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/publication")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(Publication.CLIPPING_DATE_IS_REQUIRED));
        assertTrue(response.contains(Publication.CLIPPING_MATTER_IS_REQUIRED));
    }

    @Test
    public void whenPost_withNoClippingDate_thenError() throws Exception {
        File payload = ResourceUtils.getFile("classpath:payload/publication/invalid/no_clipping_date.json");
        String content = new String(Files.readAllBytes(payload.toPath()));
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/publication")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(Publication.CLIPPING_DATE_IS_REQUIRED));
        assertFalse(response.contains(Publication.CLIPPING_MATTER_IS_REQUIRED));
    }

    @Test
    public void whenPost_withNoClippingMatter_thenError() throws Exception {
        File payload = ResourceUtils.getFile("classpath:payload/publication/invalid/no_clipping_matter.json");
        String content = new String(Files.readAllBytes(payload.toPath()));
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/publication")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertFalse(response.contains(Publication.CLIPPING_DATE_IS_REQUIRED));
        assertTrue(response.contains(Publication.CLIPPING_MATTER_IS_REQUIRED));
    }

}
