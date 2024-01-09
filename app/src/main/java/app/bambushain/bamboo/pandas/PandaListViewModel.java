package app.bambushain.bamboo.pandas;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;

@HiltViewModel
public class PandaListViewModel extends ViewModel {
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    PandaListViewModel() {
    }
}
