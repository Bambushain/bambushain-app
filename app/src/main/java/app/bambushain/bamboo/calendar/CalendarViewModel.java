package app.bambushain.bamboo.calendar;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public class CalendarViewModel extends ViewModel {
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true);

    @Inject
    public CalendarViewModel() {
    }
}
