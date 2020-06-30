package io.diego.aurum.tech.eval.converter;

import io.diego.aurum.tech.eval.model.dto.PublicationDTO;
import io.diego.aurum.tech.eval.model.entity.Publication;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@UtilityClass
public class PublicationConverter {

    public Publication convert(PublicationDTO dto) {
        return Publication.builder()
                .id(dto.getId())
                .clippingDate(dto.getClippingDate())
                .clippingMatter(dto.getClippingMatter())
                .classificationType(dto.getClassificationType())
                .classifiedDate(resolveClassfiedDate(dto))
                .important(dto.isImportant())
                .viewed(dto.isViewed())
                .build();
    }

    public PublicationDTO convert(Publication entity) {
        return PublicationDTO.builder()
                .id(entity.getId())
                .clippingDate(entity.getClippingDate())
                .clippingMatter(entity.getClippingMatter())
                .classificationType(entity.getClassificationType())
                .classifiedDate(entity.getClassifiedDate() != null ? LocalDate.from(entity.getClassifiedDate()) : null)
                .classifiedTime(entity.getClassifiedDate() != null ? LocalTime.from(entity.getClassifiedDate()) : null)
                .important(entity.isImportant())
                .viewed(entity.isViewed())
                .build();
    }

    private LocalDateTime resolveClassfiedDate(PublicationDTO dto) {
        if (dto.getClassifiedDate() != null && dto.getClassifiedTime() != null) {
            return LocalDateTime.of(dto.getClassifiedDate(), dto.getClassifiedTime());
        }
        if (dto.getClassifiedDate() != null) {
            return dto.getClassifiedDate().atStartOfDay();
        }
        return null;
    }

}
