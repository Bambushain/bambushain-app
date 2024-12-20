package app.bambushain.finalfantasy.crafter;

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
import app.bambushain.databinding.FragmentChangeCrafterDialogBinding;
import app.bambushain.models.exception.BambooException;
import app.bambushain.models.exception.ErrorType;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.Crafter;
import app.bambushain.models.finalfantasy.CrafterJob;
import app.bambushain.utils.BundleUtils;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

@AndroidEntryPoint
public class ChangeCrafterDialog extends BindingDialogFragment<FragmentChangeCrafterDialogBinding> {
    private final static String TAG = ChangeCrafterDialog.class.getName();
    @Inject
    BambooApi bambooApi;
    Snackbar snackbar;
    private boolean isCreate = true;
    private int id = 0;
    private Character character;
    private int editPosition = 0;

    @Override
    protected FragmentChangeCrafterDialogBinding getViewBinding() {
        return FragmentChangeCrafterDialogBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val viewModel = new ViewModelProvider(this).get(CrafterViewModel.class);
        val args = getArguments();
        assert args != null;
        editPosition = args.getInt("position", -1);
        character = BundleUtils.getSerializable(args, "character", Character.class);
        val availableJobs = args
                .getStringArrayList("jobs")
                .stream()
                .map(CrafterJob::fromValue)
                .map(crafterJob -> crafterJob.getTranslated(requireContext()))
                .collect(Collectors.toList());
        isCreate = editPosition == -1;
        var selectedJob = CrafterJob.getFromTranslated(requireContext(), availableJobs.get(0));

        if (!isCreate) {
            val crafter = BundleUtils.getSerializable(args, "crafter", Crafter.class);
            id = crafter.getId();
            viewModel.level.setValue(crafter.getLevel());
            selectedJob = crafter.getJob();
        }
        binding.crafterJob.setEnabled(isCreate);

        binding.actionSaveCrafter.setText(isCreate ? R.string.action_add_crafter : R.string.action_update_crafter);
        binding.actionSaveCrafter.setOnClickListener(v -> {
            val savedCrafter = new Crafter(id, CrafterJob.getFromTranslated(requireContext(), binding.crafterJobDropdown.getText().toString()), viewModel.level.getValue(), character.getId());
            if (isCreate) {
                createCrafter(savedCrafter);
            } else {
                updateCrafter(savedCrafter);
            }
        });

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.crafterJobDropdown.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.select_dialog_item, availableJobs));
        binding.crafterJobDropdown.setText(selectedJob.getTranslated(requireContext()), false);
    }

    private void updateCrafter(Crafter crafter) {
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .updateCrafter(character.getId(), id, crafter)
                .subscribe(() -> {
                    Log.i(TAG, "onViewCreated: crafter updated " + crafter);
                    val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                    stateHandle.set("position", editPosition);
                    stateHandle.set("updatedCrafter", crafter);
                    stateHandle.set("character", character);
                    navigator.popBackStack();
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: failed to update crafter", throwable);
                    val bambooEx = (BambooException) throwable;
                    var message = R.string.error_crafter_update_failed;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_crafter_update_exists;
                    }
                    showSnackbar(message);
                });
    }

    private void createCrafter(Crafter crafter) {
        //noinspection ResultOfMethodCallIgnored
        bambooApi
                .createCrafter(character.getId(), crafter)
                .subscribe(c -> {
                    Log.i(TAG, "onViewCreated: crafter created " + crafter);
                    val stateHandle = Objects.requireNonNull(navigator.getPreviousBackStackEntry()).getSavedStateHandle();
                    stateHandle.set("createdCrafter", c);
                    stateHandle.set("character", character);
                    navigator.popBackStack();
                }, throwable -> {
                    Log.e(TAG, "onViewCreated: failed to create crafter", throwable);
                    val bambooEx = (BambooException) throwable;
                    var message = R.string.error_crafter_create_failed;
                    if (bambooEx.getErrorType() == ErrorType.ExistsAlready) {
                        message = R.string.error_crafter_create_exists;
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
