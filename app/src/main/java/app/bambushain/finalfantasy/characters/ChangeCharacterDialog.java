package app.bambushain.finalfantasy.characters;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentChangeCharacterDialogBinding;
import app.bambushain.models.finalfantasy.*;
import app.bambushain.models.finalfantasy.Character;
import com.google.android.material.checkbox.MaterialCheckBox;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.util.*;

@AndroidEntryPoint
public class ChangeCharacterDialog extends BindingDialogFragment<FragmentChangeCharacterDialogBinding> {
    @Inject
    BambooApi bambooApi;
    private List<FreeCompany> freeCompanies;
    private Map<String, Set<String>> customFieldValues = new HashMap<>();
    private boolean isCreate = true;
    private int id = 0;

    @Override
    protected FragmentChangeCharacterDialogBinding getViewBinding() {
        return FragmentChangeCharacterDialogBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        val args = getArguments();
        if (args != null) {
            isCreate = false;
            val ch = (Character) args.get("character");
            id = ch.getId();
            viewModel.setRace(ch.getRace());
            // TODO: Handle arguments
        }
        bambooApi
                .getFreeCompanies()
                .subscribe(freeCompanies -> {
                    this.freeCompanies = freeCompanies;
                    binding.characterFreeCompanyDropdown.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, freeCompanies.toArray()));
                }, throwable -> {
                    // TODO: Implement error handling
                });
        bambooApi
                .getCustomFields()
                .subscribe(customCharacterFields -> {
                    customCharacterFields.sort(Comparator.comparing(CustomCharacterField::getPosition));

                    renderCustomFields(customCharacterFields);
                }, throwable -> {
                    // TODO: Implement error handling
                });

        binding.actionSaveCharacter.setText(isCreate ? R.string.action_add_character : R.string.action_update_character);
        binding.actionSaveCharacter.setOnClickListener(v -> {
            val freeCompanyString = viewModel.freeCompany.getValue();
            val freeCompany = freeCompanies
                    .stream()
                    .filter(fc -> freeCompanyString.equals(fc.getName()))
                    .findFirst()
                    .orElse(null);
            val customFields = new ArrayList<CustomField>();
            for (val customFieldValue : customFieldValues.entrySet()) {
                customFields.add(new CustomField(customFieldValue.getKey(), customFieldValue.getValue()));
            }

            val character = new Character(id, getRaceFromText(viewModel.race.getValue()), viewModel.name.getValue(), viewModel.world.getValue(), customFields, freeCompany);
            if (isCreate) {
                bambooApi
                        .createCharacter(character)
                        .subscribe(c -> {
                            val stateHandle = navigator.getPreviousBackStackEntry().getSavedStateHandle();
                            stateHandle.set("character", c);
                            stateHandle.set("action", "create");
                            navigator.popBackStack();
                        }, throwable -> {
                            // TODO: Implement error handling
                        });
            } else {
                bambooApi
                        .updateCharacter(id, character)
                        .subscribe(() -> {
                            val stateHandle = navigator.getPreviousBackStackEntry().getSavedStateHandle();
                            stateHandle.set("character", character);
                            stateHandle.set("action", "update");
                            navigator.popBackStack();
                        }, throwable -> {
                            // TODO: Implement error handling
                        });
            }
        });
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    private void renderCustomFields(List<CustomCharacterField> customCharacterFields) {
        val layout = binding.characterFreeCompany;
        val layoutParams = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (val field : customCharacterFields) {
            val customFieldLayout = new LinearLayout(getContext());
            customFieldLayout.setOrientation(LinearLayout.VERTICAL);
            customFieldLayout.setLayoutParams(layoutParams);
            val label = new TextView(getContext());
            label.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_TitleMedium);
            label.setText(field.getLabel());
            customFieldLayout.addView(label);

            for (val option : field.getOptions()) {
                val checkbox = new MaterialCheckBox(getContext());
                checkbox.setChecked(customFieldValues.getOrDefault(field.getLabel(), new HashSet<>()).contains(option.getLabel()));
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
        val context = getContext();
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
