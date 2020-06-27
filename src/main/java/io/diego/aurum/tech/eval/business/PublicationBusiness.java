package io.diego.aurum.tech.eval.business;

import io.diego.aurum.tech.eval.model.dto.PublicationDTO;
import io.diego.aurum.tech.eval.model.entity.Alert;
import io.diego.aurum.tech.eval.model.entity.Appointment;
import io.diego.aurum.tech.eval.model.entity.Publication;
import io.diego.aurum.tech.eval.model.enums.ClassificationType;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class PublicationBusiness {

    private final Pattern DATE_PARSER_PATTERN = Pattern.compile("(?i)(.*)(concilia[c|ç][a|ã]o|audi[e|ê]ncia) para a data de (.*) [a|à]s (\\d{2}:\\d{2})h(.*)");
    private final Collection<DateTimeFormatter> KNOW_DATE_FORMATS = new ArrayList<DateTimeFormatter>() {{
        add(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        add(DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy"));
    }};

    public void beforeSave(Publication publication) {
        createAppointment(publication);
        createAlert(publication);
    }

    private void createAlert(Publication publication) {
        if(!publication.isImportant()){
            return;
        }
        Alert alert = new Alert();
        alert.setDate(LocalDateTime.now());
        publication.setAlert(alert);
    }

    public void createAppointment(Publication publication) {
        if (!ClassificationType.HEARING.equals(publication.getClassificationType())) {
            return;
        }
        Appointment appointment = new Appointment();
        appointment.setDate(publication.getClassifiedDate());
        appointment.setPublication(publication);
        publication.setAppointment(appointment);

        if (appointment.getDate() == null) {
            resolveAppointmentDate(publication);
        }

    }

    private void resolveAppointmentDate(Publication publication) {
        Matcher matcher = DATE_PARSER_PATTERN.matcher(publication.getClippingMatter());
        if (!matcher.matches()) {
            publication.getAppointment().setDate(autoScheduler(publication));
            return;
        }
        String dateText = matcher.group(3);
        String timeText = matcher.group(4);
        LocalDateTime date = parseDateTime(dateText, timeText);
        if (date != null) {
            publication.getAppointment().setDate(date);
            return;
        }
        publication.getAppointment().setDate(autoScheduler(publication));
    }

    private LocalDateTime parseDateTime(String dateText, String timeText) {
        for (DateTimeFormatter dateFormat : KNOW_DATE_FORMATS) {
            try {
                LocalDate date = LocalDate.from(dateFormat.parse(dateText));
                LocalTime time = LocalTime.from(DateTimeFormatter.ofPattern("HH:mm").parse(timeText));
                return LocalDateTime.of(date, time);
            } catch (RuntimeException e) {
                e.printStackTrace();
                //ignore and try the next one
            }
        }
        return null;
    }

    private LocalDateTime autoScheduler(Publication publication) {
        LocalDate schedulerDate = publication.getClippingDate().plusDays(3);
        switch (schedulerDate.getDayOfWeek()) {
            case SATURDAY:
                return schedulerDate.plusDays(2).atStartOfDay();
            case SUNDAY:
                return schedulerDate.plusDays(1).atStartOfDay();
            default:
                return schedulerDate.atStartOfDay();
        }
    }

    public Publication toEntity(PublicationDTO publicationDTO) {
        Publication publication = new Publication();
        publication.setClippingDate(publicationDTO.getClippingDate());
        publication.setClippingMatter(publicationDTO.getClippingMatter());
        publication.setClassificationType(publicationDTO.getClassificationType());

        if (publicationDTO.getClassifiedDate() != null && publicationDTO.getClassifiedTime() != null) {
            publication.setClassifiedDate(LocalDateTime.of(publicationDTO.getClassifiedDate(), publicationDTO.getClassifiedTime()));
        } else if (publicationDTO.getClassifiedDate() != null) {
            publication.setClassifiedDate(publicationDTO.getClassifiedDate().atStartOfDay());
        }

        publication.setImportant(publicationDTO.isImportant());
        return publication;
    }

    public PublicationDTO toDTO(Publication publication) {
        PublicationDTO dto = new PublicationDTO();
        dto.setClippingDate(publication.getClippingDate());
        dto.setClippingMatter(publication.getClippingMatter());
        dto.setClassificationType(publication.getClassificationType());
        if(publication.getClassifiedDate() != null) {
            dto.setClassifiedDate(LocalDate.from(publication.getClassifiedDate()));
            dto.setClassifiedTime(LocalTime.from(publication.getClassifiedDate()));
        }
        dto.setImportant(publication.isImportant());
        return dto;
    }

}
