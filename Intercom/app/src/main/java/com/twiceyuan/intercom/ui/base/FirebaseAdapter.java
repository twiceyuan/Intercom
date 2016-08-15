package com.twiceyuan.intercom.ui.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.List;

/**
 * Created by twiceYuan on 8/6/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 对 FirebaseRecyclerAdapter 进行的接口封装的适配器
 */
public class FirebaseAdapter<T, VH extends RecyclerView.ViewHolder> extends FirebaseRecyclerAdapter<T, VH> {

    private OnPopulateViewHolder<T, VH> mPopulateViewHolder;

    public FirebaseAdapter(Class<T> modelClass, int modelLayout, Class<VH> viewHolderClass, Query ref, @NonNull OnPopulateViewHolder<T, VH> onPopulateViewHolder) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mPopulateViewHolder = onPopulateViewHolder;
    }

    public FirebaseAdapter(Class<T> modelClass, int modelLayout, Class<VH> viewHolderClass, DatabaseReference ref, @NonNull OnPopulateViewHolder<T, VH> onPopulateViewHolder) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mPopulateViewHolder = onPopulateViewHolder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    protected void populateViewHolder(VH viewHolder, T model, int position) {
        mPopulateViewHolder.populateViewHolder(viewHolder, model, position);
    }

    public interface OnPopulateViewHolder<T, VH> {
        void populateViewHolder(VH viewHolder, T model, int position);
    }
}
