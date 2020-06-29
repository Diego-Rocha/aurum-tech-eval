package io.diego.aurum.tech.eval.converter;

import io.diego.aurum.tech.eval.model.dto.PublicationDTO;
import io.diego.aurum.tech.eval.model.entity.Publication;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@UtilityClass
public class PublicationConverter {

    public Publication convert(PublicationDTO publicationDTO) {
        Publication publication = new Publication();
        publication.setId(publicationDTO.getId());
        publication.setClippingDate(publicationDTO.getClippingDate());
        publication.setClippingMatter(publicationDTO.getClippingMatter());
        publication.setClassificationType(publicationDTO.getClassificationType());

        if (publicationDTO.getClassifiedDate() != null && publicationDTO.getClassifiedTime() != null) {
            publication.setClassifiedDate(LocalDateTime.of(publicationDTO.getClassifiedDate(), publicationDTO.getClassifiedTime()));
        } else if (publicationDTO.getClassifiedDate() != null) {
            publication.setClassifiedDate(publicationDTO.getClassifiedDate().atStartOfDay());
        }

        publication.setImportant(publicationDTO.isImportant());
        publication.setViewed(publicationDTO.isViewed());
        return publication;
    }

    public PublicationDTO convert(Publication publication) {
        PublicationDTO dto = new PublicationDTO();
        dto.setId(publication.getId());
        dto.setClippingDate(publication.getClippingDate());
        dto.setClippingMatter(publication.getClippingMatter());
        dto.setClassificationType(publication.getClassificationType());
        if (publication.getClassifiedDate() != null) {
            dto.setClassifiedDate(LocalDate.from(publication.getClassifiedDate()));
            dto.setClassifiedTime(LocalTime.from(publication.getClassifiedDate()));
        }
        dto.setImportant(publication.isImportant());
        dto.setViewed(publication.isViewed());
        return dto;
    }

}
