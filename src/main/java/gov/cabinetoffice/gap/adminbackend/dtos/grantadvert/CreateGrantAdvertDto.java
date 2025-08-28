package gov.cabinetoffice.gap.adminbackend.dtos.grantadvert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGrantAdvertDto {

    @NotNull(message = "A valid Scheme ID must be provided")
    @Positive(message = "A valid Scheme ID must be provided")
    private Integer grantSchemeId;

    @NotBlank(message = "Enter the name of your grant")
    @Size(max = 255, message = "Grant name cannot be greater than 255 characters")
    private String advertName;

}
