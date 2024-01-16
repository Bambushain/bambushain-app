package app.bambushain.notification.calendar.database;

import android.content.Context;
import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {
    public static EventDao getEventDao(Context context) {
        return Room
                .databaseBuilder(context, EventDatabase.class, "event-database")
                .build()
                .eventDao();
    }

    @Provides
    @Singleton
    public EventDao provideEventDao(@ApplicationContext Context context) {
        return getEventDao(context);
    }
}
