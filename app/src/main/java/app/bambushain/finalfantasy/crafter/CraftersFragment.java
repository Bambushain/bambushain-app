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
import com.google.android.material.divider.MaterialDividerItemDecoration;
import lombok.val;

import java.util.Objects;

public class CraftersFragment extends BindingFragment<FragmentCraftersBinding> {
    private final static String TAG = CraftersFragment.class.getName();

    private final BambooApi bambooApi;

    private final Character character;

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

        binding.crafterList.setAdapter(adapter);
        binding.crafterList.setLayoutManager(new LinearLayoutManager(requireContext()));
        val dividerItemDecoration = new MaterialDividerItemDecoration(binding.crafterList.getContext(), LinearLayoutManager.VERTICAL);
        binding.crafterList.addItemDecoration(dividerItemDecoration);

        val stateHandle = Objects.requireNonNull(navigator.getCurrentBackStackEntry())
                .getSavedStateHandle();
        stateHandle.getLiveData("createdCrafter", (Character) null).observe(getViewLifecycleOwner(), crafter -> {
            if (crafter != null) {
            }
        });
        stateHandle.getLiveData("updatedCrafter", (Character) null).observe(getViewLifecycleOwner(), crafter -> {
            if (crafter != null) {
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.pullToRefreshCrafterList.setOnRefreshListener(this::loadData);

        loadData();
    }

    @SuppressLint("NotifyDataSetChanged")
    void loadData() {
        binding.pullToRefreshCrafterList.setRefreshing(true);
        //noinspection ResultOfMethodCallIgnored
        bambooApi.getCrafters(character.getId()).subscribe(crafters -> {
            val adapter = (CraftersAdapter) binding.crafterList.getAdapter();
            Objects.requireNonNull(adapter).setCrafters(crafters);
            adapter.notifyDataSetChanged();
            binding.pullToRefreshCrafterList.setRefreshing(false);
        }, throwable -> {
            Log.e(TAG, "loadData: failed to load crafter", throwable);
            Toast.makeText(requireContext(), R.string.error_crafters_loading_failed, Toast.LENGTH_LONG).show();
        });
    }
}
