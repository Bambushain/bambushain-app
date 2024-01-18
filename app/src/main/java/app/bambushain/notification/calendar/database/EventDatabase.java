package app.bambushain.notification.calendar.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {Event.class},
        version = 1,
        exportSchema = false
)
public abstract class EventDatabase extends RoomDatabase {
    public abstract EventDao eventDao();
}
