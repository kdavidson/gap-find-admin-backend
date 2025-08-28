package gov.cabinetoffice.gap.adminbackend.dtos.application;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchSectionDTO {

    // following regex allows empty string, letters, spaces, apostrophes, commas, and
    // hyphens.
    // the reason it allows empty string is so that only the @NotBlank error message will
    // return on empty string
    @Pattern(regexp = "^(?![\\s\\S])|^[a-zA-Z\\s',-]+$",
            message = "Section name must only use letters a to z, and special characters such as hyphens, spaces and apostrophes")
    @NotBlank(message = "Enter a section name")
    @Size(max = 250, message = "Your Section name must be 250 characters or less.")
    private String sectionTitle;

    private Integer version;
}
