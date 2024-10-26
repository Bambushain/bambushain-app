package app.bambushain.models.finalfantasy;

import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomField {
    private final Integer position = 0;
    @Setter
    private String label;
    @Setter
    private Set<String> values;

    public CustomField(String label, Set<String> values) {
        this.label = label;
        this.values = values;
    }
}
