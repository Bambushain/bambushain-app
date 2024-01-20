package app.bambushain.finalfantasy.characters;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentChangeCharacterDialogBinding;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.*;
import app.bambushain.utils.BundleUtils;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.util.*;

@AndroidEntryPoint
public class ChangeCharacterDialog extends BindingDialogFragment<FragmentChangeCharacterDialogBinding> {
    private final static String TAG = ChangeCharacterDialog.class.getName();
    private final Map<String, Set<String>> customFieldValues = new HashMap<>();
    @Inject
    BambooApi bambooApi;
    Snackbar snackbar;
    private List<FreeCompany> freeCompanies;
    private boolean isCreate = true;
    private int id = 0;
    private int editPosition = 0;

    @Override
    protected FragmentChangeCharacterDialogBinding getViewBinding() {
        return FragmentChangeCharacterDialogBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        val args = getArguments();
        viewModel.freeCompany.setValue(getString(R.string.character_no_free_company));
        if (args != null) {
            isCreate = false;
            editPosition = args.getInt("position");
            val ch = BundleUtils.getSerializable(args, "character", Character.class);
            id = ch.getId();
            viewModel.name.setValue(ch.getName());
            viewModel.setRace(ch.getRace());
            viewModel.world.setValue(ch.getWorld());
            if (ch.getFreeCompany() != null) {
                viewModel.freeCompany.setValue(ch.getFreeCompany().getName());
            }
            for (val customField : ch.getCustomFields()) {
                customFieldValues.put(customField.getLabel(), customField.getValues());
            }
        } else {
            viewModel.setRace(CharacterRace.LALAFELL);
        }

        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .getFreeCompanies()
                .subscribe(freeCompanies -> {
                    this.freeCompanies = freeCompanies;
                    val freeCompaniesForDropdown = new ArrayList<FreeCompany>();
                    freeCompaniesForDropdown.add(new FreeCompany(0, getString(R.string.character_no_free_company)));
                    freeCompaniesForDropdown.addAll(freeCompanies);
                    binding.characterFreeCompanyDropdown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, freeCompaniesForDropdown));
                    binding.characterRaceDropdown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, getResources().getStringArray(R.array.character_races)));
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: getFreeCompanies failed", throwable);
                    showSnackbar(R.string.error_character_free_company_loading_failed);
                });
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .getCustomFields()
                .subscribe(customCharacterFields -> {
                    customCharacterFields.sort(Comparator.comparing(CustomCharacterField::getPosition));
                    renderCustomFields(customCharacterFields);
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: getCustomFields failed", throwable);
                    showSnackbar(R.string.error_character_custom_fields_loading_failed);
                });

        binding.actionSaveCharacter.setText(isCreate ? R.string.action_add_character : R.string.action_update_character);
        binding.actionSaveCharacter.setOnClickListener(v -> {
            val freeCompanyString = viewModel.freeCompany.getValue();
            var freeCompany = freeCompanies
                    .stream()
                    .filter(fc -> fc.getName().equals(freeCompanyString))
                    .findFirst()
                    .orElse(null);
            val customFields = new ArrayList<CustomField>();
            for (val customFieldValue : customFieldValues.entrySet()) {
                customFields.add(new CustomField(customFieldValue.getKey(), customFieldValue.getValue()));
            }

            val character = new Character(id, getRaceFromText(Objects.requireNonNull(viewModel.race.getValue())), viewModel.name.getValue(), viewModel.world.getValue(), customFields, freeCompany);
            if (isCreate) {
                createCharacter(character);
            } else {
                updateCharacter(character);
            }
        });

        viewModel.name.observe(getViewLifecycleOwner(), name -> {
            if (name.isBlank()) {
                binding.characterName.setError(getString(R.string.error_character_name_required));
                viewModel.nameIsValid.setValue(false);
            } else {
                binding.characterName.setError(null);
                viewModel.nameIsValid.setValue(true);
            }
        });
        viewModel.world.observe(getViewLifecycleOwner(), world -> {
            if (world.isBlank()) {
                binding.characterWorld.setError(getString(R.string.error_character_world_required));
                viewModel.worldIsValid.setValue(false);
            } else {
                binding.characterWorld.setError(null);
                viewModel.worldIsValid.setValue(true);
            }
        });

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    private void updateCharacter(Character character) {
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .updateCharacter(id, character)
                .subscribe(() -> {
                    Log.i(TAG, "onViewCreated: character updated " + character);
                    val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                    stateHandle.set("position", editPosition);
                    stateHandle.set("updatedCharacter", character);
                    navigator.popBackStack();
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: failed to update character", throwable);
                    val bambooEx = (BambooException) throwable;
                    var message = R.string.error_character_update_failed;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_character_update_exists;
                    }
                    showSnackbar(message);
                });
    }

    private void createCharacter(Character character) {
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .createCharacter(character)
                .subscribe(c -> {
                    Log.i(TAG, "onViewCreated: character created " + character);
                    val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                    stateHandle.set("createdCharacter", c);
                    navigator.popBackStack();
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: failed to create character", throwable);
                    val bambooEx = (BambooException) throwable;
                    var message = R.string.error_character_create_failed;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_character_create_exists;
                    }
                    showSnackbar(message);
                });
    }

    private void showSnackbar(int message) {
        if (snackbar == null) {
            snackbar = Snackbar.make(binding.layout, message, Snackbar.LENGTH_INDEFINITE);
        }

        snackbar
                .setText(message)
                .setBackgroundTint(getColor(R.color.md_theme_error))
                .setTextColor(getColor(R.color.md_theme_onError))
                .show();
    }

    private void renderCustomFields(List<CustomCharacterField> customCharacterFields) {
        val layout = binding.characterCustomFields;
        val layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (val field : customCharacterFields) {
            val customFieldLayout = new LinearLayout(requireContext());
            customFieldLayout.setOrientation(LinearLayout.VERTICAL);
            customFieldLayout.setLayoutParams(layoutParams);
            val label = new TextView(requireContext());
            label.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);
            label.setText(field.getLabel());
            customFieldLayout.addView(label);

            for (val option : field.getOptions()) {
                val checkbox = new MaterialCheckBox(requireContext());
                checkbox.setChecked(Objects.requireNonNull(customFieldValues.getOrDefault(field.getLabel(), new HashSet<>())).contains(option.getLabel()));
                checkbox.setText(option.getLabel());
                checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    var entry = customFieldValues.getOrDefault(field.getLabel(), new HashSet<>());
                    if (entry == null) {
                        entry = new HashSet<>();
                    }
                    if (isChecked) {
                        entry.add(option.getLabel());
                    } else {
                        entry.remove(option.getLabel());
                    }
                    customFieldValues.put(field.getLabel(), entry);
                });
                customFieldLayout.addView(checkbox);
            }

            layout.addView(customFieldLayout);
        }
    }

    private CharacterRace getRaceFromText(String text) {
        val context = requireContext();
        if (text.equals(context.getString(R.string.character_race_hyur))) {
            return CharacterRace.HYUR;
        } else if (text.equals(context.getString(R.string.character_race_elezen))) {
            return CharacterRace.ELEZEN;
        } else if (text.equals(context.getString(R.string.character_race_viera))) {
            return CharacterRace.VIERA;
        } else if (text.equals(context.getString(R.string.character_race_minqote))) {
            return CharacterRace.MIQOTE;
        } else if (text.equals(context.getString(R.string.character_race_roegadyn))) {
            return CharacterRace.ROEGADYN;
        } else if (text.equals(context.getString(R.string.character_race_aura))) {
            return CharacterRace.AURA;
        } else if (text.equals(context.getString(R.string.character_race_hrothgar))) {
            return CharacterRace.HROTHGAR;
        }

        return CharacterRace.LALAFELL;
    }

}
