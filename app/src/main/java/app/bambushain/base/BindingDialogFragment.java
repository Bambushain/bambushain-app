package app.bambushain.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.viewbinding.ViewBinding;
import app.bambushain.MainActivity;
import app.bambushain.R;
import lombok.val;

public abstract class BindingDialogFragment<T extends ViewBinding> extends DialogFragment {
    protected T binding;
    protected NavController navigator;
    protected MainActivity activity;
    protected Toolbar toolbar;

    protected abstract T getViewBinding();

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_FullScreenDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        val dialog = getDialog();
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT;
            val height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getViewBinding();
        navigator = NavHostFragment.findNavController(this);
        activity = (MainActivity) getActivity();
        val root = binding.getRoot();
        toolbar = root.findViewById(R.id.toolbar);
        if (toolbar != null) {
            NavigationUI.setupWithNavController(toolbar, navigator);
        }

        return root;
    }

    protected @ColorInt int getColor(@ColorRes int id) {
        return getResources().getColor(id, null);
    }
}
