package app.bambushain.models.my;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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

