package app.bambushain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Character
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Character {
    private Integer id;
    private CharacterRace race;
    private String name;
    private String world;
    private List<Object> customFields = null;
    private FreeCompany freeCompany;
}

