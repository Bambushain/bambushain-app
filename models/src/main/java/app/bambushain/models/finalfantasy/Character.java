package app.bambushain.models.finalfantasy;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

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

