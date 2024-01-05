package app.bambushain.bamboo.calendar;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentEventCalendarBinding;
import dagger.hilt.android.AndroidEntryPoint;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

@AndroidEntryPoint
public class EventCalendarFragment extends BindingFragment<FragmentEventCalendarBinding> {

    private static final String TAG = EventCalendarFragment.class.getName();

    @Inject
    public EventCalendarFragment() {
    }

    @Override
    protected FragmentEventCalendarBinding getViewBinding() {
        return FragmentEventCalendarBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}