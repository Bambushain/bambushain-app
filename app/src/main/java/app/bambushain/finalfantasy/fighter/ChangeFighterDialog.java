package app.bambushain.finalfantasy.fighter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingDialogFragment;
import app.bambushain.databinding.FragmentChangeFighterDialogBinding;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.Fighter;
import app.bambushain.models.finalfantasy.FighterJob;
import app.bambushain.utils.BundleUtils;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

@AndroidEntryPoint
public class ChangeFighterDialog extends BindingDialogFragment<FragmentChangeFighterDialogBinding> {
    private final static String TAG = ChangeFighterDialog.class.getName();
    @Inject
    BambooApi bambooApi;
    Snackbar snackbar;
    private boolean isCreate = true;
    private int id = 0;
    private Character character;
    private int editPosition = 0;

    @Override
    protected FragmentChangeFighterDialogBinding getViewBinding() {
        return FragmentChangeFighterDialogBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(FighterViewModel.class);
        val args = getArguments();
        assert args != null;
        editPosition = args.getInt("position", -1);
        character = BundleUtils.getSerializable(args, "character", Character.class);
        val availableJobs = args
                .getStringArrayList("jobs")
                .stream()
                .map(FighterJob::fromValue)
                .map(fighterJob -> fighterJob.getTranslated(requireContext()))
                .collect(Collectors.toList());
        isCreate = editPosition == -1;
        var selectedJob = FighterJob.getFromTranslated(requireContext(), availableJobs.get(0));

        if (!isCreate) {
            val fighter = BundleUtils.getSerializable(args, "fighter", Fighter.class);
            id = fighter.getId();
            viewModel.level.setValue(fighter.getLevel());
            viewModel.gearScore.setValue(fighter.getGearScore());
            selectedJob = fighter.getJob();
        }
        binding.fighterJob.setEnabled(isCreate);

        binding.actionSaveFighter.setText(isCreate ? R.string.action_add_fighter : R.string.action_update_fighter);
        binding.actionSaveFighter.setOnClickListener(v -> {
            val savedFighter = new Fighter(id, FighterJob.getFromTranslated(requireContext(), binding.fighterJobDropdown.getText().toString()), viewModel.level.getValue(), viewModel.gearScore.getValue(), character.getId());
            if (isCreate) {
                createFighter(savedFighter);
            } else {
                updateFighter(savedFighter);
            }
        });

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.fighterJobDropdown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, availableJobs));
        binding.fighterJobDropdown.setText(selectedJob.getTranslated(requireContext()), false);
    }

    private void updateFighter(Fighter fighter) {
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .updateFighter(character.getId(), id, fighter)
                .subscribe(() -> {
                    Log.i(TAG, "onViewCreated: fighter updated " + fighter);
                    val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                    stateHandle.set("position", editPosition);
                    stateHandle.set("updatedFighter", fighter);
                    stateHandle.set("character", character);
                    navigator.popBackStack();
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: failed to update fighter", throwable);
                    val bambooEx = (BambooException) throwable;
                    var message = R.string.error_fighter_update_failed;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_fighter_update_exists;
                    }
                    showSnackbar(message);
                });
    }

    private void createFighter(Fighter fighter) {
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .createFighter(character.getId(), fighter)
                .subscribe(c -> {
                    Log.i(TAG, "onViewCreated: fighter created " + fighter);
                    val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                    stateHandle.set("createdFighter", c);
                    stateHandle.set("character", character);
                    navigator.popBackStack();
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: failed to create fighter", throwable);
                    val bambooEx = (BambooException) throwable;
                    var message = R.string.error_fighter_create_failed;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_fighter_create_exists;
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
