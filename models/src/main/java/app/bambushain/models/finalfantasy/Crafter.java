package app.bambushain.models.finalfantasy;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Crafter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Crafter implements Serializable {
    private Integer id = 0;
    private CrafterJob job;
    private String level;
    private Integer characterId;
}

