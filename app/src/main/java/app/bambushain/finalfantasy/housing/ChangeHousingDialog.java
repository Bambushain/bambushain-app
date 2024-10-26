package app.bambushain.finalfantasy.housing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentChangeHousingDialogBinding;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.CharacterHousing;
import app.bambushain.models.finalfantasy.HousingDistrict;
import app.bambushain.models.finalfantasy.HousingType;
import app.bambushain.utils.BundleUtils;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

@AndroidEntryPoint
public class ChangeHousingDialog extends BindingDialogFragment<FragmentChangeHousingDialogBinding> {
    private final static String TAG = ChangeHousingDialog.class.getName();
    @Inject
    BambooApi bambooApi;
    Snackbar snackbar;
    private boolean isCreate = true;
    private int id = 0;
    private Character character;
    private int editPosition = 0;

    @Override
    protected FragmentChangeHousingDialogBinding getViewBinding() {
        return FragmentChangeHousingDialogBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val args = getArguments();
        assert args != null;
        editPosition = args.getInt("position", -1);
        character = BundleUtils.getSerializable(args, "character", Character.class);
        isCreate = editPosition == -1;

        var district = HousingDistrict.THELAVENDERBEDS;
        var ward = 1;
        var plot = 1;
        var type = HousingType.PRIVATE;

        if (!isCreate) {
            val housing = BundleUtils.getSerializable(args, "housing", CharacterHousing.class);
            id = housing.getId();
            plot = housing.getPlot();
            ward = housing.getWard();
            district = housing.getDistrict();
            type = housing.getHousingType();
        }

        binding.actionSaveHousing.setText(isCreate ? R.string.action_add_housing : R.string.action_update_housing);
        binding.actionSaveHousing.setOnClickListener(v -> {
            val savedHousing = new CharacterHousing(
                    id,
                    HousingDistrict.getFromTranslated(requireContext(), Objects.requireNonNull(binding.housingDistrictDropdown.getText().toString())),
                    HousingType.getFromTranslated(requireContext(), Objects.requireNonNull(binding.housingTypeDropdown.getText().toString())),
                    Integer.parseInt(String.valueOf(binding.housingWardDropdown.getText())),
                    Integer.parseInt(String.valueOf(binding.housingPlotDropdown.getText())),
                    character.getId());
            if (isCreate) {
                createHousing(savedHousing);
            } else {
                updateHousing(savedHousing);
            }
        });

        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.housingDistrictDropdown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, Arrays.stream(HousingDistrict.values()).map(d -> d.getTranslated(requireContext())).collect(Collectors.toList())));
        binding.housingTypeDropdown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, Arrays.stream(HousingType.values()).map(t -> t.getTranslated(requireContext())).collect(Collectors.toList())));
        binding.housingWardDropdown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, IntStream.rangeClosed(1, 30).boxed().collect(Collectors.toList())));
        binding.housingPlotDropdown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, IntStream.rangeClosed(1, 60).boxed().collect(Collectors.toList())));

        binding.housingDistrictDropdown.setText(district.getTranslated(requireContext()), false);
        binding.housingTypeDropdown.setText(type.getTranslated(requireContext()), false);
        binding.housingWardDropdown.setText(String.valueOf(ward), false);
        binding.housingPlotDropdown.setText(String.valueOf(plot), false);
    }

    private void updateHousing(CharacterHousing housing) {
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .updateHousing(character.getId(), id, housing)
                .subscribe(() -> {
                    Log.i(TAG, "onViewCreated: housing updated " + housing);
                    val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                    stateHandle.set("position", editPosition);
                    stateHandle.set("updatedHousing", housing);
                    stateHandle.set("character", character);
                    navigator.popBackStack();
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: failed to update housing", throwable);
                    val bambooEx = (BambooException) throwable;
                    var message = R.string.error_housing_update_failed;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_housing_update_exists;
                    }
                    showSnackbar(message);
                });
    }

    private void createHousing(CharacterHousing housing) {
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .createHousing(character.getId(), housing)
                .subscribe(c -> {
                    Log.i(TAG, "onViewCreated: housing created " + housing);
                    val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                    stateHandle.set("createdHousing", c);
                    stateHandle.set("character", character);
                    navigator.popBackStack();
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: failed to create housing", throwable);
                    val bambooEx = (BambooException) throwable;
                    var message = R.string.error_housing_create_failed;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_housing_create_exists;
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
}
