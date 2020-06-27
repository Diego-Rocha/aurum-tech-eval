package io.diego.aurum.tech.eval.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.diego.aurum.tech.eval.business.PublicationBusiness;
import io.diego.aurum.tech.eval.model.dto.PublicationDTO;
import io.diego.aurum.tech.eval.model.entity.Publication;
import io.diego.aurum.tech.eval.repository.PublicationRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.LongStream;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/snippets")
public class PublicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublicationRepository publicationMockRepository;

    Map<Long, String> validJsonMap = new HashMap<>();
    private static final int mockSize = 4;

    @PostConstruct
    public void init() throws Exception {
        LongStream.rangeClosed(1, mockSize).forEach(id -> {
            try {
                File payload = ResourceUtils.getFile("classpath:payload/publication/valid/" + id + ".json");
                validJsonMap.put(id, new String(Files.readAllBytes(payload.toPath())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        initMocks();
    }

    private void initMocks() throws IOException {
        List<Publication> savedPublications = new ArrayList<>();
        for (Map.Entry<Long, String> validJson : validJsonMap.entrySet()) {

            PublicationDTO publicationDTO = objectMapper.readValue(validJson.getValue(), PublicationDTO.class);

            Publication publicationToSave = PublicationBusiness.toEntity(publicationDTO);
            Publication publicationSaved = PublicationBusiness.toEntity(publicationDTO);
            publicationSaved.setId(validJson.getKey());
            savedPublications.add(publicationToSave);
            when(publicationMockRepository.save(publicationToSave)).thenReturn(publicationSaved);
        }

        PageRequest pageRequestDefault = PageRequest.of(0, 10);
        when(publicationMockRepository.findAll(pageRequestDefault)).thenReturn(new PageImpl<>(savedPublications, pageRequestDefault, mockSize));

        PageRequest pageRequestCustom = PageRequest.of(0, 1);
        when(publicationMockRepository.findAll(pageRequestCustom)).thenReturn(new PageImpl<>(savedPublications.subList(0, 1), pageRequestCustom, mockSize));

        PageRequest pageRequestOutOfRange = PageRequest.of(99, 1);
        when(publicationMockRepository.findAll(pageRequestOutOfRange)).thenReturn(new PageImpl<>(Collections.emptyList(), pageRequestOutOfRange, mockSize));

    }

    @Test
    public void whenPost_withValidPublication_thenOk() throws Exception {
        for (Map.Entry<Long, String> validJson : validJsonMap.entrySet()) {
            mockMvc.perform(MockMvcRequestBuilders.post("/publication")
                    .content(validJson.getValue())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.valueOf(validJson.getKey())))
                    .andDo(document("publication/save/" + validJson.getKey()));
        }
    }

    @Test
    public void whenPost_withEmptyJson_thenError() throws Exception {
        String response = Objects.requireNonNull(mockMvc.perform(MockMvcRequestBuilders.post("/publication")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn().getResolvedException()).getMessage();
        assertTrue(response.contains(PublicationDTO.CLIPPING_DATE_IS_REQUIRED));
        assertTrue(response.contains(PublicationDTO.CLIPPING_MATTER_IS_REQUIRED));
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
        assertTrue(response.contains(PublicationDTO.CLIPPING_DATE_IS_REQUIRED));
        assertFalse(response.contains(PublicationDTO.CLIPPING_MATTER_IS_REQUIRED));
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
        assertFalse(response.contains(PublicationDTO.CLIPPING_DATE_IS_REQUIRED));
        assertTrue(response.contains(PublicationDTO.CLIPPING_MATTER_IS_REQUIRED));
    }

    @Test
    public void whenDelete_withId_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/publication/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("publication/delete/id"));
    }

    @Test
    public void whenDeleteAll_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/publication").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("publication/delete/all"));
    }

    @Test
    public void whenGetPaginate_withNoParams_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/publication")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"clippingDate\"")))
                .andExpect(content().string(containsString("\"clippingMatter\"")))
                .andExpect(content().string(containsString("\"totalElements\":" + mockSize)))
                .andDo(document("publication/list"));

    }

    @Test
    public void whenGetPaginate_withCustomSizeAndPage_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/publication")
                .queryParam("page", "0")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"clippingDate\"")))
                .andExpect(content().string(containsString("\"clippingMatter\"")))
                .andExpect(content().string(containsString("\"totalElements\":" + mockSize)))
                .andDo(document("publication/list/custom"));

    }

    @Test
    public void whenGetPaginate_withOutOfRange_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/publication")
                .queryParam("page", "99")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.not(containsString("\"clippingDate\""))))
                .andExpect(content().string(containsString("\"numberOfElements\":0")))
                .andDo(document("publication/list/out"));
    }
}
