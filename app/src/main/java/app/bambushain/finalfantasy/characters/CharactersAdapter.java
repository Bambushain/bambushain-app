package app.bambushain.finalfantasy.characters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.databinding.CharactersCardBinding;
import app.bambushain.models.finalfantasy.Character;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.ViewHolder> {
    private static final String TAG = CharactersAdapter.class.getName();
    private final ViewModelProvider viewModelProvider;
    private final LifecycleOwner lifecycleOwner;
    @Setter
    private List<Character> characters = new ArrayList<>();

    public CharactersAdapter(ViewModelProvider viewModelProvider, LifecycleOwner lifecycleOwner) {
        this.viewModelProvider = viewModelProvider;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        val binding = CharactersCardBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
        binding.setLifecycleOwner(lifecycleOwner);

        return new CharactersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CharactersAdapter.ViewHolder viewHolder, final int position) {
        Log.i(TAG, "onBindViewHolder: " + characters.toString());
        val character = characters.get(position);
        val viewModel = viewModelProvider.get(character.getId().toString(), CharacterViewModel.class);
        viewHolder.binding.setViewModel(viewModel);
        viewHolder.setCharacter(character);
    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addCharacter(Character character) {
        characters.add(character);
        characters.sort(Comparator.comparing(Character::getName));
        notifyDataSetChanged();
    }

    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CharactersCardBinding binding;

        public ViewHolder(CharactersCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setCharacter(Character character) {
            Log.i(TAG, "setCharacter: character values set");
            binding.getViewModel().id.setValue(character.getId());
            binding.getViewModel().name.setValue(character.getName());
            binding.getViewModel().setRace(character.getRace());
            binding.getViewModel().world.setValue(character.getWorld());
            if (character.getFreeCompany() != null) {
                binding.getViewModel().freeCompany.setValue(character.getFreeCompany().getName());
            }
        }

    }
}
