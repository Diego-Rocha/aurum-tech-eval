package io.diego.aurum.tech.eval.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.diego.aurum.tech.eval.converter.PublicationConverter;
import io.diego.aurum.tech.eval.model.dto.PublicationDTO;
import io.diego.aurum.tech.eval.model.entity.Appointment;
import io.diego.aurum.tech.eval.model.entity.Publication;
import io.diego.aurum.tech.eval.repository.AppointmentRepository;
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
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentRepository appointmentMockRepository;

    private int mockSize = 2;

    @PostConstruct
    public void init() throws Exception {
        initMocks();
    }

    private void initMocks() throws IOException {

        File payload = ResourceUtils.getFile("classpath:payload/publication/valid/1.json");
        Publication publication = PublicationConverter.convert(objectMapper.readValue(payload, PublicationDTO.class));

        List<Appointment> appointments = Arrays.asList(
                Appointment.builder()
                        .id(1L)
                        .date(LocalDateTime.now())
                        .build()
                ,
                Appointment.builder()
                        .id(2L)
                        .date(LocalDateTime.now().plusDays(3))
                        .publication(publication)
                        .build()
        );

        PageRequest pageRequestDefault = PageRequest.of(0, 10);
        when(appointmentMockRepository.findAll(pageRequestDefault)).thenReturn(new PageImpl<>(appointments, pageRequestDefault, mockSize));

        PageRequest pageRequestCustom = PageRequest.of(0, 1);
        when(appointmentMockRepository.findAll(pageRequestCustom)).thenReturn(new PageImpl<>(appointments.subList(0, 1), pageRequestCustom, mockSize));

        PageRequest pageRequestOutOfRange = PageRequest.of(99, 1);
        when(appointmentMockRepository.findAll(pageRequestOutOfRange)).thenReturn(new PageImpl<>(Collections.emptyList(), pageRequestOutOfRange, mockSize));
    }

    @Test
    public void whenGetPaginate_withNoParams_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/appointment")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\"")))
                .andExpect(content().string(containsString("\"date\"")))
                .andExpect(content().string(containsString("\"publication\"")))
                .andExpect(content().string(containsString("\"totalElements\":" + mockSize)))
                .andDo(document("publication/list"));

    }

    @Test
    public void whenGetPaginate_withCustomSizeAndPage_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/appointment")
                .queryParam("page", "0")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\"")))
                .andExpect(content().string(containsString("\"date\"")))
                .andExpect(content().string(containsString("\"publication\"")))
                .andExpect(content().string(containsString("\"totalElements\":" + mockSize)))
                .andDo(document("publication/list/custom"));

    }

    @Test
    public void whenGetPaginate_withOutOfRange_thenOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/appointment")
                .queryParam("page", "99")
                .queryParam("size", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(CoreMatchers.not(containsString("\"publication\""))))
                .andExpect(content().string(containsString("\"numberOfElements\":0")))
                .andDo(document("publication/list/out"));
    }

}
