package app.bambushain.finalfantasy.fighter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import app.bambushain.finalfantasy.characters.CharacterDetailsFragment;
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

        binding.itemList.setAdapter(adapter);
        binding.itemList.setLayoutManager(new LinearLayoutManager(requireContext()));
        val dividerItemDecoration = new MaterialDividerItemDecoration(binding.itemList.getContext(), LinearLayoutManager.VERTICAL);
        binding.itemList.addItemDecoration(dividerItemDecoration);

        adapter.setOnEditFighterListener((position, fighter) -> {
            val bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putSerializable("fighter", fighter);
            bundle.putSerializable("character", character);
            bundle.putStringArrayList("jobs", new ArrayList<>(Collections.singleton(fighter.getJob().getValue())));
            navigator.navigate(R.id.action_fragment_character_details_to_change_fighter_dialog, bundle);
        });
        adapter.setOnDeleteFighterListener(this::delete);

        val stateHandle = Objects.requireNonNull(navigator.getCurrentBackStackEntry())
                .getSavedStateHandle();
        stateHandle.getLiveData("createdFighter", (Fighter) null).observe(getViewLifecycleOwner(), fighter -> {
            if (fighter != null) {
                adapter.addFighter(fighter);
                usedJobs.add(fighter.getJob());
                setupToolbar();
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

    public void addFighter() {
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
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.pullToRefreshItemList.setOnRefreshListener(this::loadData);
        loadData();
    }

    @Override
    public void onStart() {
        super.onStart();
        setupToolbar();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupToolbar();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void loadData() {
        binding.pullToRefreshItemList.setRefreshing(true);
        //noinspection ResultOfMethodCallIgnored
        bambooApi.getFighters(character.getId()).subscribe(fighters -> {
            val adapter = (FightersAdapter) binding.itemList.getAdapter();
            Objects.requireNonNull(adapter).setFighters(fighters);
            adapter.notifyDataSetChanged();
            binding.pullToRefreshItemList.setRefreshing(false);
            usedJobs = fighters
                    .stream()
                    .map(Fighter::getJob)
                    .collect(Collectors.toList());
            setupToolbar();
        }, throwable -> {
            Log.e(TAG, "loadData: failed to load fighter", throwable);
            Toast.makeText(requireContext(), R.string.error_fighters_loading_failed, Toast.LENGTH_LONG).show();
        });
    }

    private void setupToolbar() {
        val parent = (CharacterDetailsFragment) getParentFragment();
        assert parent != null;
        parent.getToolbar().getMenu().clear();
        if (usedJobs.size() != FighterJob.values().length) {
            parent.getToolbar().getMenu()
                    .add(R.string.action_add_fighter)
                    .setIcon(R.drawable.ic_add)
                    .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                    .setOnMenuItemClickListener(item -> {
                        addFighter();

                        return true;
                    });
        }
    }

    private void delete(int position, Fighter fighter) {
        val jobLabel = fighter.getJob().getTranslated(requireContext());

        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_delete_fighter)
                .setMessage(getString(R.string.action_delete_fighter_message, jobLabel))
                .setPositiveButton(R.string.action_delete_fighter, (dialog, which) -> bambooApi
                        .deleteFighter(fighter.getCharacterId(), fighter.getId())
                        .subscribe(() -> {
                            val adapter = (FightersAdapter) binding.itemList.getAdapter();
                            assert adapter != null;
                            adapter.removeFighter(position);
                            usedJobs.remove(fighter.getJob());
                            setupToolbar();
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
}
