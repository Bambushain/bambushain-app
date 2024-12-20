package app.bambushain.models.authentication;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
