package app.bambushain.models.finalfantasy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Fighter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fighter {
    private Integer id;
    private FighterJob job;
    private String level;
    private String gearScore;
    private Integer characterId;
}
