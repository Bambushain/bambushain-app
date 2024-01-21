package app.bambushain.finalfantasy.fighter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
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
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;
import java.util.Objects;
import java.util.stream.Collectors;

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
                .map(this::getFighterJobLabel)
                .collect(Collectors.toList());
        isCreate = editPosition == -1;
        var selectedJob = getFighterJobFromText(availableJobs.get(0));

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
            val savedFighter = new Fighter(id, getFighterJobFromText(binding.fighterJobDropdown.getText().toString()), viewModel.level.getValue(), viewModel.gearScore.getValue(), character.getId());
            if (isCreate) {
                createFighter(savedFighter);
            } else {
                updateFighter(savedFighter);
            }
        });

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.fighterJobDropdown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, availableJobs));
        binding.fighterJobDropdown.setText(getFighterJobLabel(selectedJob), false);
    }

    private String getFighterJobLabel(FighterJob fighterJob) {
        return getString(switch (fighterJob) {
            case PALADIN -> R.string.fighter_job_paladin;
            case WARRIOR -> R.string.fighter_job_warrior;
            case DARKKNIGHT -> R.string.fighter_job_darkknight;
            case GUNBREAKER -> R.string.fighter_job_gunbreaker;
            case WHITEMAGE -> R.string.fighter_job_whitemage;
            case SCHOLAR -> R.string.fighter_job_scholar;
            case ASTROLOGIAN -> R.string.fighter_job_astrologian;
            case SAGE -> R.string.fighter_job_sage;
            case MONK -> R.string.fighter_job_monk;
            case DRAGOON -> R.string.fighter_job_dragoon;
            case NINJA -> R.string.fighter_job_ninja;
            case SAMURAI -> R.string.fighter_job_samurai;
            case REAPER -> R.string.fighter_job_reaper;
            case BARD -> R.string.fighter_job_bard;
            case MACHINIST -> R.string.fighter_job_machinist;
            case DANCER -> R.string.fighter_job_dancer;
            case BLACKMAGE -> R.string.fighter_job_blackmage;
            case SUMMONER -> R.string.fighter_job_summoner;
            case REDMAGE -> R.string.fighter_job_redmage;
            case BLUEMAGE -> R.string.fighter_job_bluemage;
        });
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

    private FighterJob getFighterJobFromText(String text) {
        if (text.equals(getString(R.string.fighter_job_paladin))) {
            return FighterJob.PALADIN;
        }
        if (text.equals(getString(R.string.fighter_job_warrior))) {
            return FighterJob.WARRIOR;
        }
        if (text.equals(getString(R.string.fighter_job_darkknight))) {
            return FighterJob.DARKKNIGHT;
        }
        if (text.equals(getString(R.string.fighter_job_gunbreaker))) {
            return FighterJob.GUNBREAKER;
        }
        if (text.equals(getString(R.string.fighter_job_whitemage))) {
            return FighterJob.WHITEMAGE;
        }
        if (text.equals(getString(R.string.fighter_job_scholar))) {
            return FighterJob.SCHOLAR;
        }
        if (text.equals(getString(R.string.fighter_job_astrologian))) {
            return FighterJob.ASTROLOGIAN;
        }
        if (text.equals(getString(R.string.fighter_job_sage))) {
            return FighterJob.SAGE;
        }
        if (text.equals(getString(R.string.fighter_job_monk))) {
            return FighterJob.MONK;
        }
        if (text.equals(getString(R.string.fighter_job_dragoon))) {
            return FighterJob.DRAGOON;
        }
        if (text.equals(getString(R.string.fighter_job_ninja))) {
            return FighterJob.NINJA;
        }
        if (text.equals(getString(R.string.fighter_job_samurai))) {
            return FighterJob.SAMURAI;
        }
        if (text.equals(getString(R.string.fighter_job_reaper))) {
            return FighterJob.REAPER;
        }
        if (text.equals(getString(R.string.fighter_job_bard))) {
            return FighterJob.BARD;
        }
        if (text.equals(getString(R.string.fighter_job_machinist))) {
            return FighterJob.MACHINIST;
        }
        if (text.equals(getString(R.string.fighter_job_dancer))) {
            return FighterJob.DANCER;
        }
        if (text.equals(getString(R.string.fighter_job_blackmage))) {
            return FighterJob.BLACKMAGE;
        }
        if (text.equals(getString(R.string.fighter_job_summoner))) {
            return FighterJob.SUMMONER;
        }
        if (text.equals(getString(R.string.fighter_job_redmage))) {
            return FighterJob.REDMAGE;
        }

        return FighterJob.BLUEMAGE;
    }
}
