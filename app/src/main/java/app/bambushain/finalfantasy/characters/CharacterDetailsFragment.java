package app.bambushain.finalfantasy.characters;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentCharacterDetailsBinding;
import app.bambushain.finalfantasy.crafter.CraftersFragment;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.utils.BundleUtils;
import com.google.android.material.tabs.TabLayoutMediator;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.val;

import javax.inject.Inject;

@AndroidEntryPoint
public class CharacterDetailsFragment extends BindingFragment<FragmentCharacterDetailsBinding> {

    public final static int TAB_FIGHTER = 0;
    public final static int TAB_CRAFTER = 1;
    public final static int TAB_HOUSING = 2;

    protected NavController tabNavigator;

    @Inject
    BambooApi bambooApi;

    Character character;

    private CharacterDetailsFragmentTabsAdapter characterDetailsFragmentTabsAdapter;

    @Override
    protected FragmentCharacterDetailsBinding getViewBinding() {
        return FragmentCharacterDetailsBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        val args = getArguments();
        assert args != null;
        var activeTabIdx = args.getInt("activeTab", TAB_FIGHTER);
        character = BundleUtils.getSerializable(args, "character", Character.class);
        binding.toolbar.setTitle(character.getName());
        binding.tabViewPager.setAdapter(new CharacterDetailsFragmentTabsAdapter(this));
        new TabLayoutMediator(binding.tabs, binding.tabViewPager, (tab, position) -> {
            if (position == TAB_FIGHTER) {
                tab.setText(R.string.action_character_fighter);
                tab.setIcon(R.drawable.ic_fighter);
            } else if (position == TAB_CRAFTER) {
                tab.setText(R.string.action_character_crafter);
                tab.setIcon(R.drawable.ic_crafter);
            } else if (position == TAB_HOUSING) {
                tab.setText(R.string.action_character_housing);
                tab.setIcon(R.drawable.ic_housing);
            }
        }).attach();
        binding.tabViewPager.setCurrentItem(activeTabIdx, false);
    }

    public class CharacterDetailsFragmentTabsAdapter extends FragmentStateAdapter {

        public CharacterDetailsFragmentTabsAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int i) {
            if (i == TAB_CRAFTER) {
                return new CraftersFragment(bambooApi, character);
            }

            return new CraftersFragment(bambooApi, character);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}