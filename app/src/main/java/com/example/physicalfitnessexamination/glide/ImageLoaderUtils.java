package com.example.physicalfitnessexamination.glide;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.physicalfitnessexamination.R;

import java.io.File;


/**
 * @author wangke
 */
public class ImageLoaderUtils {
    /**
     * 动态使用占用图片加载图片
     *
     * @param imageView   图片控件
     * @param url         url
     * @param placeholder 占用图片id
     * @param error       错误图片id
     */
    public static void display(ImageView imageView, String url, int placeholder, int error) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(imageView.getContext()).load(url).placeholder(placeholder)
                .error(error).crossFade().into(imageView);
    }

    /**
     * 动态使用占用图片加载图片
     *
     * @param imageView 图片控件
     * @param url       url
     * @param error     错误图片id
     */
    public static void display(Context context, ImageView imageView, String url, int error) {
        Glide.with(context).load(url).asBitmap()
                .error(error)
                .into(imageView);
    }

    /**
     * 根据Url加载方图片
     *
     * @param context   上下文
     * @param imageView 图片控件
     * @param url       路径的值
     */
    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (context instanceof Activity) {
            Activity mactivity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (mactivity != null && !mactivity.isDestroyed()) {
                    Glide.with(context).load(url).asBitmap()
                            .format(DecodeFormat.PREFER_ARGB_8888)
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                            .placeholder(R.mipmap.bitmap)
                            .error(R.mipmap.bitmap)
                            .into(imageView);
                }
            } else if (context != null) {
                Glide.with(context).load(url).asBitmap()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.mipmap.bitmap)
                        .error(R.mipmap.bitmap)
                        .into(imageView);
            }
        }

    }

    /**
     * 根据Url加载方图片
     *
     * @param context   上下文
     * @param imageView 图片控件
     * @param url       路径的值
     */
    public static void display(Context context, ImageView imageView, String url, DecodeFormat decodeFormat) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url).asBitmap()
                .format(decodeFormat)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.bitmap)
                .error(R.mipmap.bitmap)
                .into(imageView);
    }

    public static void display(Fragment fragment, ImageView imageView, String url) {
        if (url != null && url.contains("app-img-cdn")) {
            url = url + "?x-oss-process=image/format,webp";
        }
        Glide.with(fragment).load(url).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.bitmap)
                .error(R.mipmap.bitmap)
                .into(imageView);
    }

    /**
     * 根据Url加载方图片
     *
     * @param context   上下文
     * @param imageView 图片控件
     * @param url       路径的值
     */
    public static void displayXY(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.bitmap)
                .error(R.mipmap.bitmap)
                .into(imageView);
    }

    /**
     * 根据Url加载方图片
     *
     * @param context   上下文
     * @param imageView 图片控件
     * @param url       路径的值
     */
    public static void displayBg(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .placeholder(R.mipmap.bitmap)
                .error(R.mipmap.bitmap)
                .into(imageView);
    }

    /**
     * 根据Url加载图片
     *
     * @param context   上下文
     * @param imageView 图片控件
     * @param url       路径的值
     * @param type      图片类型（1：方形图片(其他值默认为1)；2：长型图片；3：高型图片）
     */
//    public static void displayPic(Context context, ImageView imageView, String url, int type) {
//        if (imageView == null) {
//            throw new IllegalArgumentException("argument error");
//        }
//        if (context instanceof Activity) {
//            Activity mactivity = (Activity) context;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                if (mactivity != null && !mactivity.isDestroyed()) {
//                    Glide.with(context).load(url)
//                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                            .centerCrop()
//                            .placeholder(type == 1 ? R.drawable.home_icon_750_375 : (type == 2 ? R.drawable.home_icon_690_222 : (type == 3 ? R.drawable.home_icon_339_200 : (type == 4 ? R.drawable.home_icon_222_222 : (type == 5 ? R.mipmap.bitmap : R.drawable.home_icon_750_422)))))
//                            .error(type == 1 ? R.drawable.home_icon_750_375 : (type == 2 ? R.drawable.home_icon_690_222 : (type == 3 ? R.drawable.home_icon_339_200 : (type == 4 ? R.drawable.home_icon_222_222 : (type == 5 ? R.mipmap.bitmap : R.drawable.home_icon_750_422)))))
//                            .into(imageView);
//                }
//            } else if (context != null) {
//                Glide.with(context).load(url)
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .centerCrop()
//                        .placeholder(type == 1 ? R.drawable.home_icon_750_375 : (type == 2 ? R.drawable.home_icon_690_222 : (type == 3 ? R.drawable.home_icon_339_200 : (type == 4 ? R.drawable.home_icon_222_222 : (type == 5 ? R.mipmap.bitmap : R.drawable.home_icon_750_422)))))
//                        .error(type == 1 ? R.drawable.home_icon_750_375 : (type == 2 ? R.drawable.home_icon_690_222 : (type == 3 ? R.drawable.home_icon_339_200 : (type == 4 ? R.drawable.home_icon_222_222 : (type == 5 ? R.mipmap.bitmap : R.drawable.home_icon_750_422)))))
//                        .into(imageView);
//            }
//        }
//
//    }

    /**
     * 根据图片路径或者ID加载圆角图片
     *
     * @param imageView 图片空间
     * @param value     路径或ID的值
     * @param type      图片类型（1：方形图片(其他值默认为1)；2：长型图片；3：高型图片）
     */
//    public static void displayTransRound(Context context, ImageView imageView, Object value, int type) {
//        if (imageView == null) {
//            throw new IllegalArgumentException("argument error");
//        }
//        Glide.with(context).load(null == value ? R.mipmap.bitmap : (value instanceof Integer ? ((Integer) value) : Integer.valueOf((value.toString()))))
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .placeholder(type == 3 ? R.mipmap.bitmap : (type == 2 ? R.drawable.home_icon_650_200 : R.mipmap.bitmap))
//                .error(type == 3 ? R.mipmap.bitmap : (type == 2 ? R.drawable.home_icon_650_200 : R.mipmap.bitmap))
//                .transform(new CenterCrop(context), new GlideRoundTransform(context)).into(imageView);
//    }

    /**
     * 根据文件加载方图片
     *
     * @param imageView 图片控件
     * @param url       路径的值
     */
    public static void display(ImageView imageView, File url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(imageView.getContext()).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .placeholder(R.mipmap.bitmap)
                .error(R.mipmap.bitmap)
                .crossFade().into(imageView);
    }

    /**
     * 根据图片路径加载头像小图片
     *
     * @param imageView 图片控件
     * @param url       路径的值
     */
    public static void displaySmallPhoto(ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(imageView.getContext()).load(url).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.bitmap)
                .error(R.mipmap.bitmap)
                .thumbnail(0.5f)
                .into(imageView);
    }

    /**
     * 根据图片路径加载头像小图片
     *
     * @param imageView 图片控件
     * @param url       路径的值
     */
    public static void displaySmallRoundPhoto(ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(imageView.getContext()).load(url).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.bitmap)
                .error(R.mipmap.bitmap)
                .thumbnail(0.5f)
                .centerCrop().transform(new GlideRoundTransformUtil(imageView.getContext())).into(imageView);
    }

    /**
     * 根据图片路径加载大头像图片
     *
     * @param imageView 图片控件
     * @param url       路径的值
     */
    public static void displayBigPhoto(ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(imageView.getContext()).load(url).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.bitmap)
                .error(R.mipmap.bitmap)
                .into(imageView);
    }

    /**
     * 根据图片路径加载大广告图片
     *
     * @param imageView 图片控件
     * @param url       路径的值
     */
    public static void displayBigAdPhoto(ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(imageView.getContext()).load(url).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.bitmap)
                .into(imageView);
    }

    /**
     * 根据图片ID加载清晰图片
     *
     * @param imageView 图片空间
     * @param value     路径或ID的值
     */
    public static void display(ImageView imageView, Object value) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(imageView.getContext()).load(null == value ? R.mipmap.bitmap : (value instanceof Integer ? ((Integer) value) : Integer.valueOf((value.toString())))).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.bitmap)
                .error(R.mipmap.bitmap)
                .into(imageView);

    }

    /**
     * 根据图片路径或者ID加载圆形图片
     *
     * @param imageView 图片空间
     * @param value     路径或ID的值
     */
//    public static void displayRound(Context context, ImageView imageView, Object value) {
//        if (context == null) {
//            return;
//        }
//        if (imageView == null) {
//            throw new IllegalArgumentException("argument error");
//        }
//        if (context instanceof Activity) {
//            Activity mactivity = (Activity) context;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                if (mactivity != null && !mactivity.isDestroyed()) {
//                    Glide.with(context).load(null == value ? R.drawable.defaulthead : value.toString()).asBitmap()
//                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                            .placeholder(R.drawable.defaulthead)
//                            .error(R.drawable.defaulthead)
//                            .centerCrop().transform(new GlideCircleTransfromUtil(context)).into(imageView);
//                }
//            } else if (context != null) {
//                Glide.with(context).load(null == value ? R.drawable.defaulthead : value.toString()).asBitmap()
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                        .placeholder(R.drawable.defaulthead)
//                        .error(R.drawable.defaulthead)
//                        .centerCrop().transform(new GlideCircleTransfromUtil(context)).into(imageView);
//            }
//        }
//    }

    /**
     * 根据图片路径或者ID加载高斯模糊图片
     *
     * @param imageView 图片空间
     * @param value     路径或ID的值
     */
    public static void displayGaoSiPic(Context context, ImageView imageView, Object value) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(context).load(null == value ? R.mipmap.bitmap : (value instanceof Integer ? ((Integer) value) : Integer.valueOf((value.toString())))).bitmapTransform(new BlurTransformation(context, 25)).crossFade(1000).into(imageView);

    }

    /**
     * 根据图片路径或者ID加载GIF图片
     *
     * @param imageView   图片空间
     * @param value       路径或ID的值
     * @param repeatTimes 重复次数(0:默认为无限次重复)
     */
    public static void displayGif(Context context, ImageView imageView, Object value, int repeatTimes) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if (value instanceof String) {
            if (repeatTimes > 0) {
                Glide.with(context).load(value.toString()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            } else {
                Glide.with(context).load(value.toString()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new GlideDrawableImageViewTarget(imageView, repeatTimes));
            }
        } else if (value instanceof Integer) {
            if (repeatTimes > 0) {
                Glide.with(context).load((Integer) value).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(new GlideDrawableImageViewTarget(imageView, repeatTimes));
            } else {
                Glide.with(context).load((Integer) value).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
            }
        }
    }

    /**
     * 根据图片ID加载清晰图片
     *
     * @param imageView 图片空间
     * @param value     路径或ID的值
     */
    public static void display1(ImageView imageView, Object value) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        Glide.with(imageView.getContext()).load(null == value ? R.mipmap.bitmap : (value instanceof Integer ? ((Integer) value) : Integer.valueOf((value.toString())))).asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.mipmap.bitmap)
                .into(imageView);

    }

}
