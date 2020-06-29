package io.diego.aurum.tech.eval.converter;

import io.diego.aurum.tech.eval.model.dto.AppointmentDTO;
import io.diego.aurum.tech.eval.model.entity.Appointment;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppointmentConverter {

    public AppointmentDTO convert(Appointment entity) {
        return AppointmentDTO.builder()
                .id(entity.getId())
                .date(entity.getDate())
                .publication(entity.getPublication() != null ? PublicationConverter.convert(entity.getPublication()) : null)
                .build();
    }

}
