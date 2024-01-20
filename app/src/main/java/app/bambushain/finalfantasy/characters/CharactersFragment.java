package app.bambushain.finalfantasy.characters;


import android.annotation.SuppressLint;
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
import java.util.Objects;

@AndroidEntryPoint
public class CharactersFragment extends BindingFragment<FragmentCharactersBinding> {
    private static final String TAG = CharactersFragment.class.getName();
    @Inject
    BambooApi bambooApi;

    @Inject
    public CharactersFragment() {
    }

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
        binding.characterList.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter.setOnEditCharacterListener((position, character) -> {
            val bundle = new Bundle();
            bundle.putSerializable("character", character);
            bundle.putInt("position", position);
            navigator.navigate(R.id.action_fragment_characters_to_add_character_dialog, bundle);
        });

        adapter.setOnDeleteCharacterListener(this::delete);
        adapter.setOnCharacterDetailsListener(character -> {
            val bundle = new Bundle();
            bundle.putSerializable("character", character);
            navigator.navigate(R.id.action_fragment_characters_to_character_details_dialog, bundle);
        });

        val stateHandle = Objects.requireNonNull(navigator.getCurrentBackStackEntry())
                .getSavedStateHandle();
        stateHandle.getLiveData("createdCharacter", (Character) null).observe(getViewLifecycleOwner(), character -> {
            if (character != null) {
                adapter.addCharacter(character);
            }
        });
        stateHandle.getLiveData("updatedCharacter", (Character) null).observe(getViewLifecycleOwner(), character -> {
            if (character != null) {
                val editPosition = (Integer) stateHandle.get("position");
                //noinspection DataFlowIssue
                adapter.updateCharacter(editPosition, character);
            }
        });

        return view;
    }


    @SuppressLint("NotifyDataSetChanged")
    void loadData() {
        binding.pullToRefreshCharacterList.setRefreshing(true);
        //noinspection ResultOfMethodCallIgnored
        bambooApi.getCharacters().subscribe(characters -> {
            val adapter = (CharactersAdapter) binding.characterList.getAdapter();
            Objects.requireNonNull(adapter).setCharacters(characters);
            adapter.notifyDataSetChanged();
            binding.getViewModel().isLoading.setValue(false);
            binding.pullToRefreshCharacterList.setRefreshing(false);
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
        binding.pullToRefreshCharacterList.setOnRefreshListener(this::loadData);

        loadData();
    }

    private void delete(int position, Character character) {
        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_delete_character)
                .setMessage(getString(R.string.action_delete_character_message, character.getName()))
                .setPositiveButton(R.string.action_delete_character, (dialog, which) -> bambooApi
                        .deleteCharacter(character.getId())
                        .subscribe(() -> {
                            val adapter = (CharactersAdapter) binding.characterList.getAdapter();
                            assert adapter != null;
                            adapter.removeCharacter(position);
                            Toast.makeText(requireContext(), getString(R.string.success_character_delete, character.getName()), Toast.LENGTH_LONG).show();
                        }, throwable -> {
                            Log.e(TAG, "delete: deleting character failed", throwable);
                            Toast
                                    .makeText(requireContext(), getString(R.string.error_character_delete_failed, character.getName()), Toast.LENGTH_LONG)
                                    .show();
                        }))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
}