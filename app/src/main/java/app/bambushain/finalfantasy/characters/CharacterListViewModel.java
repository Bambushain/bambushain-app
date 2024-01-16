package app.bambushain.finalfantasy.characters;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;

import javax.inject.Inject;
import java.net.HttpCookie;

@HiltViewModel
public class CharacterListViewModel extends ViewModel {
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public CharacterListViewModel() {
    }
}
