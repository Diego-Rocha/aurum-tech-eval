package io.diego.aurum.tech.eval.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity(name = "sample")
@Data
public class Publication {

    public static final String CLIPPING_DATE_IS_REQUIRED = "clippingDate is required";
    public static final String CLIPPING_MATTER_IS_REQUIRED = "clippingMatter is required";

    @Id
    private Long id;

    @NotNull(message = CLIPPING_DATE_IS_REQUIRED)
    private Date clippingDate;

    @NotBlank(message = CLIPPING_MATTER_IS_REQUIRED)
    private String clippingMatter;

    private ClassificationType classificationType;
    private Date classifiedDate;
    private String classifiedTime;
    private boolean important;
}
