package com.amalitechfileserver.fileserverbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}",
            message = "Password should contain numbers and digits only. 3 to 20 characters")
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @Pattern(regexp = "^[a-zA-Z0-9]{3,20}",
            message = "Password should contain numbers and digits only. 3 to 20 characters")
    @NotBlank(message = "New password is required")
    private String newPassword;

    @NotBlank(message = "Token is required")
    private String jwt;

}
