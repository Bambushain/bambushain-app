package app.bambushain.finalfantasy.characters;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import app.bambushain.R;
import app.bambushain.api.BambooApi;
import app.bambushain.base.BindingFragment;
import app.bambushain.databinding.FragmentCharacterDetailsBinding;
import app.bambushain.finalfantasy.crafter.CraftersFragment;
import app.bambushain.finalfantasy.fighter.FightersFragment;
import app.bambushain.finalfantasy.housing.HousingFragment;
import app.bambushain.models.finalfantasy.Character;
import app.bambushain.utils.BundleUtils;
import com.google.android.material.tabs.TabLayoutMediator;
import dagger.hilt.android.AndroidEntryPoint;
import lombok.Getter;
import lombok.val;

import javax.inject.Inject;

@AndroidEntryPoint
public class CharacterDetailsFragment extends BindingFragment<FragmentCharacterDetailsBinding> {

    public final static int TAB_FIGHTER = 0;
    public final static int TAB_CRAFTER = 1;
    public final static int TAB_HOUSING = 2;

    @Inject
    BambooApi bambooApi;

    Character character;

    public Toolbar getToolbar() {
        return toolbar;
    }

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
            binding.toolbar.getMenu().clear();
            if (position == TAB_FIGHTER) {
                tab.setText(R.string.action_character_fighter);
                if (!getResources().getBoolean(R.bool.is_landscape)) {
                    tab.setIcon(R.drawable.ic_fighter);
                }
            } else if (position == TAB_CRAFTER) {
                tab.setText(R.string.action_character_crafter);
                if (!getResources().getBoolean(R.bool.is_landscape)) {
                    tab.setIcon(R.drawable.ic_crafter);
                }
            } else if (position == TAB_HOUSING) {
                tab.setText(R.string.action_character_housing);
                if (!getResources().getBoolean(R.bool.is_landscape)) {
                    tab.setIcon(R.drawable.ic_housing);
                }
            }
        }).attach();
        binding.tabViewPager.setCurrentItem(activeTabIdx, false);
    }

    @Getter
    public class CharacterDetailsFragmentTabsAdapter extends FragmentStateAdapter {

        public CharacterDetailsFragmentTabsAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        private Fragment currentFragment;

        @NonNull
        @Override
        public Fragment createFragment(int i) {
            if (i == TAB_CRAFTER) {
                currentFragment = new CraftersFragment(bambooApi, character);
            } else if (i == TAB_HOUSING) {
                currentFragment = new HousingFragment(bambooApi, character);
            } else if (i == TAB_FIGHTER) {
                currentFragment = new FightersFragment(bambooApi, character);
            }

            return currentFragment;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}