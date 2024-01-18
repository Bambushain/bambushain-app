package app.bambushain.models.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TwoFactorRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TwoFactorRequest implements Serializable {
    private String email;
    private String password;
}
