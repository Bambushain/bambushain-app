package app.bambushain.notification.calendar.database;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import lombok.val;

import java.time.LocalDate;
import java.time.ZoneId;

public class CleanupWorker extends Worker {
    private final static String TAG = CleanupWorker.class.getName();

    private final EventDao eventDao;

    public CleanupWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.eventDao = DatabaseModule.getEventDao(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        val todayMinTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
        eventDao
                .deleteEventsBeforeDay(todayMinTime)
                .subscribe(() -> Log.d(TAG, "doWork: Successful cleanup done"), throwable -> Log.e(TAG, "doWork: Failed to run cleanup", throwable));

        return Result.success();
    }
}
