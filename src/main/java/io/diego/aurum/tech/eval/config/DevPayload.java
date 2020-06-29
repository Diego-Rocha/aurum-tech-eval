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
        Publication publication = new Publication();
        publication.setClippingDate(LocalDate.now());
        publication.setClassificationType(ClassificationType.DEADLINE);
        publication.setClippingMatter("a");
        add(publication);

        publication = new Publication();
        publication.setClippingDate(LocalDate.now().plusDays(1));
        publication.setClippingMatter("b");
        publication.setClassificationType(ClassificationType.HEARING);
        publication.setClassifiedDate(LocalDateTime.now().plusDays(4).plusHours(2));
        add(publication);

        publication = new Publication();
        publication.setClippingDate(LocalDate.now().plusDays(1));
        publication.setClippingMatter("audiencia para a data de 27 de janeiro de 2020 as 11:30h");
        publication.setImportant(true);
        add(publication);

    }};

    @Bean
    public ApplicationRunner devApplicationRunner() {
        publicationService.deleteAll();
        return arg -> payload.forEach(publicationService::save);
    }

}
