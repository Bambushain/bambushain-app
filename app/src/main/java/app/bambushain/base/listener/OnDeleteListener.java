package app.bambushain.base.listener;

public interface OnDeleteListener<T> {
    void onDelete(int position, T item);
}
