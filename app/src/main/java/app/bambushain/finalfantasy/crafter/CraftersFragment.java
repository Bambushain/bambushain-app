package app.bambushain.finalfantasy.crafter;

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
import app.bambushain.databinding.FragmentCraftersBinding;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.Crafter;
import app.bambushain.models.finalfantasy.CrafterJob;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import lombok.val;

import java.util.*;
import java.util.stream.Collectors;

public class CraftersFragment extends BindingFragment<FragmentCraftersBinding> {
    private final static String TAG = CraftersFragment.class.getName();

    private final BambooApi bambooApi;

    private final Character character;

    private List<CrafterJob> usedJobs = new ArrayList<>();

    public CraftersFragment(BambooApi bambooApi, Character character) {
        this.bambooApi = bambooApi;
        this.character = character;
    }

    @Override
    protected FragmentCraftersBinding getViewBinding() {
        return FragmentCraftersBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        val view = super.onCreateView(inflater, container, savedInstanceState);
        val adapter = new CraftersAdapter(new ViewModelProvider(this), getViewLifecycleOwner());

        binding.itemList.setAdapter(adapter);
        binding.itemList.setLayoutManager(new LinearLayoutManager(requireContext()));
        val dividerItemDecoration = new MaterialDividerItemDecoration(binding.itemList.getContext(), LinearLayoutManager.VERTICAL);
        binding.itemList.addItemDecoration(dividerItemDecoration);

        adapter.setOnEditCrafterListener((position, crafter) -> {
            val bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putSerializable("crafter", crafter);
            bundle.putSerializable("character", character);
            bundle.putStringArrayList("jobs", new ArrayList<>(Collections.singleton(crafter.getJob().getValue())));
            navigator.navigate(R.id.action_fragment_character_details_to_change_crafter_dialog, bundle);
        });
        adapter.setOnDeleteCrafterListener(this::delete);

        binding.addCrafter.setOnClickListener(v -> {
            val currentUsedJobs = usedJobs
                    .stream()
                    .map(CrafterJob::getValue)
                    .collect(Collectors.toList());
            val jobs = Arrays
                    .stream(CrafterJob.values())
                    .map(CrafterJob::getValue)
                    .filter(crafterJob -> !currentUsedJobs.contains(crafterJob))
                    .collect(Collectors.toList());

            val bundle = new Bundle();
            bundle.putSerializable("character", character);
            bundle.putStringArrayList("jobs", new ArrayList<>(jobs));
            navigator.navigate(R.id.action_fragment_character_details_to_change_crafter_dialog, bundle);
        });

        val stateHandle = Objects.requireNonNull(navigator.getCurrentBackStackEntry())
                .getSavedStateHandle();
        stateHandle.getLiveData("createdCrafter", (Crafter) null).observe(getViewLifecycleOwner(), crafter -> {
            if (crafter != null) {
                adapter.addCrafter(crafter);
                usedJobs.add(crafter.getJob());
                checkIfAddEnabled();
            }
        });
        stateHandle.getLiveData("updatedCrafter", (Crafter) null).observe(getViewLifecycleOwner(), crafter -> {
            if (crafter != null) {
                val editPosition = (Integer) stateHandle.get("position");
                //noinspection DataFlowIssue
                adapter.updateCrafter(editPosition, crafter);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.pullToRefreshItemList.setOnRefreshListener(this::loadData);

        loadData();
    }

    @SuppressLint("NotifyDataSetChanged")
    void loadData() {
        binding.pullToRefreshItemList.setRefreshing(true);
        //noinspection ResultOfMethodCallIgnored
        bambooApi.getCrafters(character.getId()).subscribe(crafters -> {
            val adapter = (CraftersAdapter) binding.itemList.getAdapter();
            Objects.requireNonNull(adapter).setCrafters(crafters);
            adapter.notifyDataSetChanged();
            binding.pullToRefreshItemList.setRefreshing(false);
            usedJobs = crafters
                    .stream()
                    .map(Crafter::getJob)
                    .collect(Collectors.toList());
            checkIfAddEnabled();
        }, throwable -> {
            Log.e(TAG, "loadData: failed to load crafter", throwable);
            Toast.makeText(requireContext(), R.string.error_crafters_loading_failed, Toast.LENGTH_LONG).show();
        });
    }

    private void checkIfAddEnabled() {
        if (usedJobs.size() == CrafterJob.values().length) {
            binding.addCrafter.setVisibility(View.GONE);
        } else {
            binding.addCrafter.setVisibility(View.VISIBLE);
        }
    }

    private void delete(int position, Crafter crafter) {
        val jobLabel = crafter.getJob().getTranslated(requireContext());

        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_delete_crafter)
                .setMessage(getString(R.string.action_delete_crafter_message, jobLabel))
                .setPositiveButton(R.string.action_delete_crafter, (dialog, which) -> bambooApi
                        .deleteCrafter(crafter.getCharacterId(), crafter.getId())
                        .subscribe(() -> {
                            val adapter = (CraftersAdapter) binding.itemList.getAdapter();
                            assert adapter != null;
                            adapter.removeCrafter(position);
                            usedJobs.remove(crafter.getJob());
                            checkIfAddEnabled();
                            Toast.makeText(requireContext(), getString(R.string.success_crafter_delete, jobLabel), Toast.LENGTH_LONG).show();
                        }, throwable -> {
                            Log.e(TAG, "delete: deleting character failed", throwable);
                            Toast
                                    .makeText(requireContext(), getString(R.string.error_crafter_delete_failed, jobLabel), Toast.LENGTH_LONG)
                                    .show();
                        }))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
}
