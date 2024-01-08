package app.bambushain.models.my;

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

