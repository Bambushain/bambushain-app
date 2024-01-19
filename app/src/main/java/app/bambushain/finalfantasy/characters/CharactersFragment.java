package app.bambushain.finalfantasy.characters;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentCharactersBinding;
import app.bambushain.models.finalfantasy.Character;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;

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

        adapter.setOnEditCharacterListener((position, character) -> {
            val bundle = new Bundle();
            bundle.putSerializable("character", character);
            navigator.navigate(R.id.action_fragment_characters_to_add_character_dialog, bundle);
        });

        adapter.setOnDeleteCharacterListener(this::delete);

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
            Toast.makeText(requireContext(), R.string.error_characters_loading_failed, Toast.LENGTH_LONG).show();
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

    private void delete(int position, Character character) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_delete_character)
                .setMessage(getString(R.string.action_delete_character_message, character.getName()))
                .setPositiveButton(R.string.action_delete_character, (dialog, which) -> {
                    bambooApi
                            .deleteCharacter(character.getId())
                            .subscribe(() -> {
                                val adapter = (CharactersAdapter) binding.characterList.getAdapter();
                                adapter.removeCharacter(position);
                                Toast.makeText(requireContext(), getString(R.string.success_character_delete, character.getName()), Toast.LENGTH_LONG).show();
                            }, throwable -> {
                                Log.e(TAG, "delete: deleting character failed", throwable);
                                Toast.makeText(requireContext(), getString(R.string.error_character_delete_failed, character.getName()), Toast.LENGTH_LONG);
                            });
                })
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
}