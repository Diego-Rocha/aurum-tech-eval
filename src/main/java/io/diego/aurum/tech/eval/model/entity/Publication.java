package io.diego.aurum.tech.eval.model.entity;

import io.diego.aurum.tech.eval.model.enums.ClassificationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Publication {

    @Id
    private Long id;

    private LocalDate clippingDate;
    private String clippingMatter;
    private ClassificationType classificationType;
    private LocalDateTime classifiedDate;

    private boolean important;

    @Reference
    private Appointment appointment;

    @Reference
    private Alert alert;
}
