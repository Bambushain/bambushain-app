package app.bambushain.finalfantasy.fighter;

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
import app.bambushain.databinding.FragmentFightersBinding;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.Fighter;
import app.bambushain.models.finalfantasy.FighterJob;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.divider.MaterialDividerItemDecoration;
import lombok.val;

import java.util.*;
import java.util.stream.Collectors;

public class FightersFragment extends BindingFragment<FragmentFightersBinding> {
    private final static String TAG = FightersFragment.class.getName();

    private final BambooApi bambooApi;

    private final Character character;

    private List<FighterJob> usedJobs = new ArrayList<>();

    public FightersFragment(BambooApi bambooApi, Character character) {
        this.bambooApi = bambooApi;
        this.character = character;
    }

    @Override
    protected FragmentFightersBinding getViewBinding() {
        return FragmentFightersBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        val view = super.onCreateView(inflater, container, savedInstanceState);
        val adapter = new FightersAdapter(new ViewModelProvider(this), getViewLifecycleOwner());

        binding.fighterList.setAdapter(adapter);
        binding.fighterList.setLayoutManager(new LinearLayoutManager(requireContext()));
        val dividerItemDecoration = new MaterialDividerItemDecoration(binding.fighterList.getContext(), LinearLayoutManager.VERTICAL);
        binding.fighterList.addItemDecoration(dividerItemDecoration);

        adapter.setOnEditFighterListener((position, fighter) -> {
            val bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putSerializable("fighter", fighter);
            bundle.putSerializable("character", character);
            bundle.putStringArrayList("jobs", new ArrayList<>(Collections.singleton(fighter.getJob().getValue())));
            navigator.navigate(R.id.action_fragment_character_details_to_change_fighter_dialog, bundle);
        });
        adapter.setOnDeleteFighterListener(this::delete);

        binding.addFighter.setOnClickListener(v -> {
            val currentUsedJobs = usedJobs
                    .stream()
                    .map(FighterJob::getValue)
                    .collect(Collectors.toList());
            val jobs = Arrays
                    .stream(FighterJob.values())
                    .map(FighterJob::getValue)
                    .filter(fighterJob -> !currentUsedJobs.contains(fighterJob))
                    .collect(Collectors.toList());

            val bundle = new Bundle();
            bundle.putSerializable("character", character);
            bundle.putStringArrayList("jobs", new ArrayList<>(jobs));
            navigator.navigate(R.id.action_fragment_character_details_to_change_fighter_dialog, bundle);
        });

        val stateHandle = Objects.requireNonNull(navigator.getCurrentBackStackEntry())
                .getSavedStateHandle();
        stateHandle.getLiveData("createdFighter", (Fighter) null).observe(getViewLifecycleOwner(), fighter -> {
            if (fighter != null) {
                adapter.addFighter(fighter);
                usedJobs.add(fighter.getJob());
                checkIfAddEnabled();
            }
        });
        stateHandle.getLiveData("updatedFighter", (Fighter) null).observe(getViewLifecycleOwner(), fighter -> {
            if (fighter != null) {
                val editPosition = (Integer) stateHandle.get("position");
                //noinspection DataFlowIssue
                adapter.updateFighter(editPosition, fighter);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.pullToRefreshFighterList.setOnRefreshListener(this::loadData);

        loadData();
    }

    @SuppressLint("NotifyDataSetChanged")
    void loadData() {
        binding.pullToRefreshFighterList.setRefreshing(true);
        //noinspection ResultOfMethodCallIgnored
        bambooApi.getFighters(character.getId()).subscribe(fighters -> {
            val adapter = (FightersAdapter) binding.fighterList.getAdapter();
            Objects.requireNonNull(adapter).setFighters(fighters);
            adapter.notifyDataSetChanged();
            binding.pullToRefreshFighterList.setRefreshing(false);
            usedJobs = fighters
                    .stream()
                    .map(Fighter::getJob)
                    .collect(Collectors.toList());
            checkIfAddEnabled();
        }, throwable -> {
            Log.e(TAG, "loadData: failed to load fighter", throwable);
            Toast.makeText(requireContext(), R.string.error_fighters_loading_failed, Toast.LENGTH_LONG).show();
        });
    }

    private void checkIfAddEnabled() {
        if (usedJobs.size() == FighterJob.values().length) {
            binding.addFighter.setVisibility(View.GONE);
        } else {
            binding.addFighter.setVisibility(View.VISIBLE);
        }
    }

    private void delete(int position, Fighter fighter) {
        val jobLabel = getFighterJobLabel(fighter.getJob());

        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_delete_fighter)
                .setMessage(getString(R.string.action_delete_fighter_message, jobLabel))
                .setPositiveButton(R.string.action_delete_fighter, (dialog, which) -> bambooApi
                        .deleteFighter(fighter.getCharacterId(), fighter.getId())
                        .subscribe(() -> {
                            val adapter = (FightersAdapter) binding.fighterList.getAdapter();
                            assert adapter != null;
                            adapter.removeFighter(position);
                            usedJobs.remove(fighter.getJob());
                            checkIfAddEnabled();
                            Toast.makeText(requireContext(), getString(R.string.success_fighter_delete, jobLabel), Toast.LENGTH_LONG).show();
                        }, throwable -> {
                            Log.e(TAG, "delete: deleting character failed", throwable);
                            Toast
                                    .makeText(requireContext(), getString(R.string.error_fighter_delete_failed, jobLabel), Toast.LENGTH_LONG)
                                    .show();
                        }))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
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
}
