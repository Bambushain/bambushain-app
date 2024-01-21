package app.bambushain.finalfantasy.housing;

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
import app.bambushain.databinding.FragmentHousingBinding;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.models.finalfantasy.CharacterHousing;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import lombok.val;

import java.util.Objects;

public class HousingFragment extends BindingFragment<FragmentHousingBinding> {
    private final static String TAG = HousingFragment.class.getName();

    private final BambooApi bambooApi;

    private final Character character;

    public HousingFragment(BambooApi bambooApi, Character character) {
        this.bambooApi = bambooApi;
        this.character = character;
    }

    @Override
    protected FragmentHousingBinding getViewBinding() {
        return FragmentHousingBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        val view = super.onCreateView(inflater, container, savedInstanceState);
        val adapter = new HousingAdapter(new ViewModelProvider(this), getViewLifecycleOwner());

        binding.housingList.setAdapter(adapter);
        binding.housingList.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter.setOnEditHousingListener((position, housing) -> {
            val bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putSerializable("housing", housing);
            bundle.putSerializable("character", character);
            navigator.navigate(R.id.action_fragment_character_details_to_change_housing_dialog, bundle);
        });
        adapter.setOnDeleteHousingListener(this::delete);

        binding.addHousing.setOnClickListener(v -> {
            val bundle = new Bundle();
            bundle.putSerializable("character", character);
            navigator.navigate(R.id.action_fragment_character_details_to_change_housing_dialog, bundle);
        });

        val stateHandle = Objects.requireNonNull(navigator.getCurrentBackStackEntry())
                .getSavedStateHandle();
        stateHandle.getLiveData("createdHousing", (CharacterHousing) null).observe(getViewLifecycleOwner(), housing -> {
            if (housing != null) {
                adapter.addHousing(housing);
            }
        });
        stateHandle.getLiveData("updatedHousing", (CharacterHousing) null).observe(getViewLifecycleOwner(), housing -> {
            if (housing != null) {
                val editPosition = (Integer) stateHandle.get("position");
                //noinspection DataFlowIssue
                adapter.updateHousing(editPosition, housing);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        binding.pullToRefreshHousingList.setOnRefreshListener(this::loadData);

        loadData();
    }

    @SuppressLint("NotifyDataSetChanged")
    void loadData() {
        binding.pullToRefreshHousingList.setRefreshing(true);
        //noinspection ResultOfMethodCallIgnored
        bambooApi.getHousings(character.getId()).subscribe(housings -> {
            val adapter = (HousingAdapter) binding.housingList.getAdapter();
            Objects.requireNonNull(adapter).setHousings(housings);
            adapter.notifyDataSetChanged();
            binding.pullToRefreshHousingList.setRefreshing(false);
        }, throwable -> {
            Log.e(TAG, "loadData: failed to load housing", throwable);
            Toast.makeText(requireContext(), R.string.error_housings_loading_failed, Toast.LENGTH_LONG).show();
        });
    }

    private void delete(int position, CharacterHousing housing) {
        //noinspection ResultOfMethodCallIgnored
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.action_delete_housing)
                .setMessage(getString(R.string.action_delete_housing_message, housing.getDistrict().getTranslated(requireContext()), housing.getWard(), housing.getPlot()))
                .setPositiveButton(R.string.action_delete_housing, (dialog, which) -> bambooApi
                        .deleteHousing(housing.getCharacterId(), housing.getId())
                        .subscribe(() -> {
                            val adapter = (HousingAdapter) binding.housingList.getAdapter();
                            assert adapter != null;
                            adapter.removeHousing(position);
                            Toast.makeText(requireContext(), R.string.success_housing_delete, Toast.LENGTH_LONG).show();
                        }, throwable -> {
                            Toast
                                    .makeText(requireContext(), R.string.error_housing_delete_failed, Toast.LENGTH_LONG)
                                    .show();
                        }))
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.cancel())
                .create()
                .show();
    }
}
