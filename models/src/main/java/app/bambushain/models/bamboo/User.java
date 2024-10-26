package app.bambushain.models.bamboo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private Integer id = 0;
    private String displayName;
    private String email;
    private Boolean isMod;
    private String discordName;
    private Boolean appTotpEnabled;
}
