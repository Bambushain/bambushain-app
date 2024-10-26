package app.bambushain.models.authentication;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements Serializable {
    private String email;
    private String password;
    private String twoFactorCode;
}
