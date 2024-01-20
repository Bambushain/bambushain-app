package app.bambushain.finalfantasy.characters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import app.bambushain.R;
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
    @Setter
    private OnEditCharacterListener onEditCharacterListener;
    @Setter
    private OnDeleteCharacterListener onDeleteCharacterListener;
    @Setter
    private OnCharacterDetailsListener onCharacterDetailsListener;

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
        viewHolder.binding.actionMore.setOnClickListener(v -> {
            val popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.characters_card_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_edit_character && onEditCharacterListener != null) {
                    onEditCharacterListener.onEdit(position, character);
                } else if (item.getItemId() == R.id.action_delete_character && onDeleteCharacterListener != null) {
                    onDeleteCharacterListener.onDelete(position, character);
                }
                return true;
            });
            popupMenu.show();
        });
        viewHolder.binding.showDetails.setOnClickListener(v -> {
            if (onCharacterDetailsListener != null) {
                onCharacterDetailsListener.onDetails(character);
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

    public interface OnEditCharacterListener {
        void onEdit(int position, Character character);
    }

    public interface OnDeleteCharacterListener {
        void onDelete(int position, Character character);
    }

    public interface OnCharacterDetailsListener {
        void onDetails(Character character);
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
