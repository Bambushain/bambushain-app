package app.bambushain.models.finalfantasy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Crafter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crafter {
    private Integer id = 0;
    private CrafterJob job;
    private String level;
    private Integer characterId;
}

