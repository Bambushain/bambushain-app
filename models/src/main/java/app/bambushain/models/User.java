package app.bambushain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String displayName;
    private String email;
    private Boolean isMod;
    private String discordName;
    private Boolean appTotpEnabled;
}
