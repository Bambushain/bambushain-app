package app.bambushain.models.finalfantasy;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CustomCharacterFieldOption
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomCharacterFieldOption implements Serializable {
    private Integer id = 0;
    private String label;
}
