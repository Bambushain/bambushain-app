package app.bambushain.models.pandas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserProfile implements Serializable {
    private String email;
    private String displayName;
    private String discordName;
}
