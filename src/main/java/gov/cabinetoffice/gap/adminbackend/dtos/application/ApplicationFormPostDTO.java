package gov.cabinetoffice.gap.adminbackend.dtos.application;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ApplicationFormPostDTO {

    @NotNull(message = "A valid Scheme ID must be provided")
    @Positive(message = "A valid Scheme ID must be provided")
    private Integer grantSchemeId;

    @NotBlank(message = "Enter the name of your application")
    @Size(max = 255, message = "Application name cannot be greater than 255 characters")
    private String applicationName;

}
