package com.example.physicalfitnessexamination.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ViewHolder {
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private Context mContext;

    public ViewHolder(Context context, ViewGroup parent, int layoutId,
                      int position) {
        mContext = context;
        mPosition = position;
        mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mConvertView.setTag(this);
    }

    public static ViewHolder getViewHolder(Context context, View convertView,
                                           ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.mPosition = position;
            return viewHolder;
        }
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public int getPosition() {
        return mPosition;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        if (textView != null) {
            if (TextUtils.isEmpty(text)) {
                textView.setText("");
            } else {
                textView.setText(text);
                textView.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    public ViewHolder setImage(int viewId, int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }


//    public ViewHolder setImageWithNewSize(int viewId, String imageUrl,
//                                          int imageWidth, int imageHeight) {
//        final ImageView imageView = getView(viewId);
//        if (imageView != null) {
//            if (imageUrl.equals("")) {
//                imageView.setImageResource(R.mipmap.timg);
//                imageView.setTag(null);
//            } else {
//                if (imageView.getTag() != null
//                        && imageUrl.equals((String) imageView.getTag())) {
//                    imageView.setImageDrawable(imageView.getDrawable());
//                } else {
//                    Glide.with(imageView.getContext())
//                            .load(imageUrl)
//                            .asBitmap()
//                            .centerCrop().into(imageView);
//
//                }
//                imageView.setTag(imageUrl);
//            }
//        }
//        return this;
//    }
}
