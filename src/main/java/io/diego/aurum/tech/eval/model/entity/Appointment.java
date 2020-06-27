package io.diego.aurum.tech.eval.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Appointment {

    @Id
    private Long id;

    private LocalDateTime date;

    @Reference
    private Publication publication;

}
