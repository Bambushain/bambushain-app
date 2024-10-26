package app.bambushain.models.finalfantasy;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Character
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Character implements Serializable {
    private Integer id = 0;
    private CharacterRace race;
    private String name;
    private String world;
    private List<CustomField> customFields = null;
    private FreeCompany freeCompany;
}

