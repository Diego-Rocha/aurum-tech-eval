package io.diego.aurum.tech.eval.converter;

import io.diego.aurum.tech.eval.model.dto.AlertDTO;
import io.diego.aurum.tech.eval.model.entity.Alert;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AlertConverter {

    public AlertDTO convert(Alert entity) {
        AlertDTO dto = new AlertDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        if (entity.getPublication() != null) {
            dto.setPublication(PublicationConverter.convert(entity.getPublication()));
        }
        return dto;
    }

}
