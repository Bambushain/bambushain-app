package app.bambushain.notification.calendar.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.NoArgsConstructor;
import lombok.val;

@Entity
@NoArgsConstructor
public class Event {
    @PrimaryKey
    public int uid;
    public String startDate;
    public String endDate;
    public String title;

    public static Event fromEvent(app.bambushain.models.bamboo.Event event) {
        val result = new Event();

        result.uid = event.getId();
        result.startDate = event.getStartDate().toString();
        result.endDate = event.getEndDate().toString();
        result.title = event.getTitle();

        return result;
    }
}
