package io.diego.aurum.tech.eval.converter;

import io.diego.aurum.tech.eval.model.dto.AppointmentDTO;
import io.diego.aurum.tech.eval.model.entity.Appointment;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppointmentConverter {

    public AppointmentDTO convert(Appointment entity) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        if (entity.getPublication() != null) {
            dto.setPublication(PublicationConverter.convert(entity.getPublication()));
        }
        return dto;
    }

}
