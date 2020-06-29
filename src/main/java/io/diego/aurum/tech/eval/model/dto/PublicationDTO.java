package io.diego.aurum.tech.eval.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.diego.aurum.tech.eval.model.enums.ClassificationType;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PublicationDTO {

    public static final String CLIPPING_DATE_IS_REQUIRED = "clippingDate is required";
    public static final String CLIPPING_MATTER_IS_REQUIRED = "clippingMatter is required";

    private Long id;

    @NotNull(message = CLIPPING_DATE_IS_REQUIRED)
    private LocalDate clippingDate;

    @NotBlank(message = CLIPPING_MATTER_IS_REQUIRED)
    private String clippingMatter;

    private ClassificationType classificationType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate classifiedDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime classifiedTime;

    private boolean important;
    private boolean viewed;
}
