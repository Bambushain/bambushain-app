package app.bambushain.models.pandas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfile {
    private String email;
    private String displayName;
    private String discordName;
}
