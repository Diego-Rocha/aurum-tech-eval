package io.diego.aurum.tech.eval.converter;

import io.diego.aurum.tech.eval.model.dto.AlertDTO;
import io.diego.aurum.tech.eval.model.entity.Alert;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AlertConverter {

    public AlertDTO convert(Alert entity) {
        return AlertDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .publication(entity.getPublication() != null ? PublicationConverter.convert(entity.getPublication()) : null)
                .build();
    }

}
