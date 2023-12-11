package app.bambushain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChangeMyPassword
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMyPassword {
    private String oldPassword;
    private String newPassword;
}

