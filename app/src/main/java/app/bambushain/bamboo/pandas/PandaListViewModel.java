package app.bambushain.bamboo.pandas;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PandaListViewModel extends ViewModel {
    public final MutableLiveData<Boolean> canCreate = new MutableLiveData<>(false);

    @Inject
    PandaListViewModel() {
    }
}
