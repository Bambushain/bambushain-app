package app.bambushain.notification.calendar.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event WHERE startDate < unixepoch() AND endDate > unixepoch()")
    Observable<List<Event>> getEventsForDay();

    @Upsert
    Completable upsertEvent(Event event);

    @Upsert
    Completable createOrUpdateEvents(List<Event> events);

    @Delete
    Completable deleteEvent(Event event);

    @Query("DELETE FROM event WHERE endDate < :epoch")
    Completable deleteEventsBeforeDay(long epoch);

    @Query("DELETE FROM event")
    Completable cleanDatabase();
}
