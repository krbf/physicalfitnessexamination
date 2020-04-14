package com.example.physicalfitnessexamination.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.physicalfitnessexamination.viewholder.ViewHolder;

import java.util.List;

/**
 * @param <T>
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int mLayoutId;
    private int selectedPosition;

    public CommonAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public int getCount() {
        try {
            return mDatas.size();
        } catch (Exception e) {
            return 0;
        }

    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent, mLayoutId, position);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();
    }

    public abstract void convert(ViewHolder viewHolder, T t);
}
