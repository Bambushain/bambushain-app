package app.bambushain.models.my;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ChangeMyPassword
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMyPassword implements Serializable {
    private String oldPassword;
    private String newPassword;
}

