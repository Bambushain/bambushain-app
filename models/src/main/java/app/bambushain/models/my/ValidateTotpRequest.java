package app.bambushain.models.my;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ValidateTotpRequest
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateTotpRequest {
    private String code;
    private String password;
}
