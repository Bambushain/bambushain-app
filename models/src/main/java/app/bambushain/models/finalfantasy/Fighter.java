package app.bambushain.models.finalfantasy;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Fighter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fighter implements Serializable {
    private Integer id = 0;
    private FighterJob job;
    private String level;
    private String gearScore;
    private Integer characterId;
}
