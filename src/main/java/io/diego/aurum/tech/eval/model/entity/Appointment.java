package io.diego.aurum.tech.eval.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    private Long id;

    private LocalDateTime date;

    @Reference
    private Publication publication;

}
