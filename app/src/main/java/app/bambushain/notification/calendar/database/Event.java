package app.bambushain.notification.calendar.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.NoArgsConstructor;
import lombok.val;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor
public class Event {
    @PrimaryKey
    public int uid;
    public long startDate;
    public long endDate;
    public String title;

    public static Event fromEvent(app.bambushain.models.bamboo.Event event) {
        val result = new Event();

        val startDate = ZonedDateTime.of(event.getStartDate(), LocalTime.MIN, ZoneId.systemDefault()).toOffsetDateTime();
        val endDate = ZonedDateTime.of(event.getEndDate(), LocalTime.MAX, ZoneId.systemDefault()).toOffsetDateTime();

        result.uid = event.getId();
        result.startDate = startDate.toEpochSecond();
        result.endDate = endDate.toEpochSecond();
        result.title = event.getTitle();

        return result;
    }
}
