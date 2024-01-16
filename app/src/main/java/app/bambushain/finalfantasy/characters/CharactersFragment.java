package app.bambushain.finalfantasy.characters;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import app.bambushain.api.BambooApi;
import app.bambushain.bamboo.pandas.PandasAdapter;
import app.bambushain.bamboo.pandas.PandasFragment;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentCharactersBinding;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.util.ArrayList;

@AndroidEntryPoint
public class CharactersFragment extends BindingFragment<FragmentCharactersBinding> {
    private static final String TAG = CharactersFragment.class.getName();

    @Inject
    public CharactersFragment() {
    }

    @Inject
    BambooApi bambooApi;

    @Override
    protected FragmentCharactersBinding getViewBinding() {
        return FragmentCharactersBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        val view = super.onCreateView(inflater, container, savedInstanceState);
        val adapter = new CharactersAdapter(new ViewModelProvider(this), getViewLifecycleOwner());
        binding.characterList.setAdapter(adapter);
        binding.characterList.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }


    void loadData() {
        bambooApi.getCharacters().subscribe(characters -> {
            val adapter = (CharactersAdapter) binding.characterList.getAdapter();
            adapter.setCharacters(characters);
            adapter.notifyDataSetChanged();
            binding.getViewModel().isLoading.setValue(false);
            Log.i(TAG, "loadData: " + characters.toString());
        }, throwable -> {
            Log.e(TAG, "loadData: failed to load characters", throwable);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(CharacterListViewModel.class);
        viewModel.isLoading.setValue(true);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        loadData();
    }
}