package app.bambushain.finalfantasy.characters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.base.listener.OnDeleteListener;
import app.bambushain.base.listener.OnEditListener;
import app.bambushain.databinding.CharacterCardBinding;
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
    @Setter
    private OnEditListener<Character> onEditCharacterListener;
    @Setter
    private OnDeleteListener<Character> onDeleteCharacterListener;
    @Setter
    private OnCharacterDetailsListener onCharacterDetailsListener;
    @Setter
    private OnCrafterClickListener onCrafterClickListener;
    @Setter
    private OnFighterClickListener onFighterClickListener;
    @Setter
    private OnHousingClickListener onHousingClickListener;

    public CharactersAdapter(ViewModelProvider viewModelProvider, LifecycleOwner lifecycleOwner) {
        this.viewModelProvider = viewModelProvider;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        val binding = CharacterCardBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false);
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
        viewHolder.binding.editCharacter.setOnClickListener(v -> {
            if (onEditCharacterListener != null) {
                onEditCharacterListener.onEdit(position, character);
            }
        });
        viewHolder.binding.deleteCharacter.setOnClickListener(v -> {
            if (onDeleteCharacterListener != null) {
                onDeleteCharacterListener.onDelete(position, character);
            }
        });
        viewHolder.binding.showDetails.setOnClickListener(v -> {
            if (onCharacterDetailsListener != null) {
                onCharacterDetailsListener.onDetails(character);
            }
        });
        viewHolder.binding.showCrafter.setOnClickListener(v -> {
            if (onCrafterClickListener != null) {
                onCrafterClickListener.onCrafter(character);
            }
        });
        viewHolder.binding.showFighter.setOnClickListener(v -> {
            if (onFighterClickListener != null) {
                onFighterClickListener.onFighter(character);
            }
        });
        viewHolder.binding.showHousing.setOnClickListener(v -> {
            if (onHousingClickListener != null) {
                onHousingClickListener.onHousing(character);
            }
        });
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

    @SuppressLint("NotifyDataSetChanged")
    public void updateCharacter(int position, Character character) {
        characters.set(position, character);
        characters.sort(Comparator.comparing(Character::getName));
        notifyDataSetChanged();
    }

    public void removeCharacter(int position) {
        characters.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnCharacterDetailsListener {
        void onDetails(Character character);
    }

    public interface OnCrafterClickListener {
        void onCrafter(Character character);
    }

    public interface OnFighterClickListener {
        void onFighter(Character character);
    }

    public interface OnHousingClickListener {
        void onHousing(Character character);
    }

    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final CharacterCardBinding binding;

        public ViewHolder(CharacterCardBinding binding) {
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
