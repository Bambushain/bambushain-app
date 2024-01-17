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
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.bamboo.pandas.PandasAdapter;
import app.bambushain.bamboo.pandas.PandasFragment;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentCharactersBinding;
import app.bambushain.models.bamboo.User;
import app.bambushain.models.finalfantasy.Character;
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

        val stateHandle = navigator
                .getCurrentBackStackEntry()
                .getSavedStateHandle();
        val action = stateHandle.getLiveData("action", "");
        action.observe(getViewLifecycleOwner(), a -> {
            if (a.equals("update")) {
                action.setValue("");
            } else if (a.equals("create")) {
                val character = (Character) stateHandle.get("character");
                adapter.addCharacter(character);

                action.setValue("");
            }
        });

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
            // TODO: Show error message
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(CharacterListViewModel.class);
        viewModel.isLoading.setValue(true);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.addCharacter.setOnClickListener(v -> navigator.navigate(R.id.action_fragment_characters_to_add_character_dialog));

        loadData();
    }
}