package app.bambushain.finalfantasy.characters;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import app.bambushain.R;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentCharacterDetailsDialogBinding;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.CustomField;
import app.bambushain.utils.BundleUtils;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@AndroidEntryPoint
public class CharacterDetailsDialog extends BindingDialogFragment<FragmentCharacterDetailsDialogBinding> {
    @Override
    protected FragmentCharacterDetailsDialogBinding getViewBinding() {
        return FragmentCharacterDetailsDialogBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(CharacterViewModel.class);
        viewModel.freeCompany.setValue(getString(R.string.character_no_free_company));
        val character = BundleUtils.getSerializable(getArguments(), "character", Character.class);
        viewModel.name.setValue(character.getName());
        viewModel.setRace(character.getRace());
        viewModel.world.setValue(character.getWorld());
        if (character.getFreeCompany() != null) {
            viewModel.freeCompany.setValue(character.getFreeCompany().getName());
        }

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        renderCustomFields(character.getCustomFields());
    }

    private void renderCustomFields(List<CustomField> customFields) {
        val layout = binding.characterCustomFields;
        val layoutParams = new ViewGroup.MarginLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) getResources().getDimension(R.dimen.margin);
        layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.margin);
        for (val field : customFields) {
            if (field.getValues().isEmpty()) {
                continue;
            }

            var valuesText = "";
            if (field.getValues().size() == 1) {
                valuesText = String.join("", field.getValues());
            } else {
                val valuesAsList = field
                        .getValues()
                        .stream()
                        .sorted()
                        .collect(Collectors.toList());
                val allExceptLast = String.join(", ", valuesAsList.subList(0, valuesAsList.size() - 1));
                valuesText = getString(R.string.character_custom_fields_values, allExceptLast, valuesAsList.get(valuesAsList.size() - 1));
            }

            val customFieldLayout = new LinearLayout(requireContext());
            customFieldLayout.setOrientation(LinearLayout.VERTICAL);
            customFieldLayout.setLayoutParams(layoutParams);

            val label = new TextView(requireContext());
            label.setTextAppearance(R.style.TextAppearance_Bambushain_LabelMedium);
            label.setText(field.getLabel());
            customFieldLayout.addView(label);

            val options = new TextView(requireContext());
            options.setTextAppearance(R.style.TextAppearance_Bambushain_BodyMedium);
            options.setText(valuesText);
            customFieldLayout.addView(options);

            layout.addView(customFieldLayout);
        }
    }
}
