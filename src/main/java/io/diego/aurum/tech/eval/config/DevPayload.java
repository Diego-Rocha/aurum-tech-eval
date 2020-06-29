package io.diego.aurum.tech.eval.config;

import io.diego.aurum.tech.eval.model.entity.Publication;
import io.diego.aurum.tech.eval.model.enums.ClassificationType;
import io.diego.aurum.tech.eval.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Profile("dev")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DevPayload {

    @Autowired
    private final PublicationService publicationService;

    private final List<Publication> payload = new ArrayList<Publication>() {{
        Publication publication = Publication.builder()
                .clippingDate(LocalDate.now())
                .classificationType(ClassificationType.DEADLINE)
                .clippingMatter("a")
                .build();
        add(publication);

        publication = Publication.builder()
                .clippingDate(LocalDate.now().plusDays(1))
                .clippingMatter("b")
                .classificationType(ClassificationType.HEARING)
                .classifiedDate(LocalDateTime.now().plusDays(4).plusHours(2))
                .build();
        add(publication);

        publication = Publication.builder()
                .clippingDate(LocalDate.now().plusDays(1))
                .clippingMatter("audiencia para a data de 27 de janeiro de 2020 as 11:30h")
                .important(true)
                .build();
        add(publication);

    }};

    @Bean
    public ApplicationRunner devApplicationRunner() {
        publicationService.deleteAll();
        return arg -> payload.forEach(publicationService::save);
    }

}
