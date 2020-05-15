package com.czy.module_common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tool {
    static final String DEFAULT_ENCODING = "utf-8";

    /**
     * 从输入流中读取JSON字符串
     *
     * @param in
     * @return
     * @throws Exception
     */
    public static String getRequestJsonStream(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int length = 8192;
            byte[] datas = new byte[length];
            while ((length = (in.read(datas, 0, length))) != -1) {
                out.write(datas, 0, length);
            }
            byte[] ds = out.toByteArray();
            return new String(ds, "utf-8");
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        }
    }

    /**
     * 向输出流中写入数据
     *
     * @param out
     * @param responseJson
     * @throws Exception
     */
    public static void sendResponseJson(OutputStream out, String responseJson)
            throws Exception {
        try {
            out.write(responseJson.getBytes(DEFAULT_ENCODING));
        } catch (Exception e) {
            throw e;
        } finally {
            if (out != null)
                out.close();
        }
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @return String
     */
    public static String doEmpty(String str) {
        return doEmpty(str, "");
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @param defaultValue
     * @return String
     */
    public static String doEmpty(String str, String defaultValue) {
        if (str == null || str.equalsIgnoreCase("null")
                || str.trim().equals("")) {
            str = defaultValue;
        } else if (str.startsWith("null")) {
            str = str.substring(4, str.length());
        }
        return str.trim();
    }

    /**
     * String为null时转换为"",同时过滤掉前后空格
     */
    public static String filerStr(String str) {
        if (str == null)
            return "";
        return str.trim();
    }

    /**
     * 判断对象值是否为空： 若对象为字符串，判断对象值是否为null或空格; 若对象为数组，判断对象值是否为null，或数组个数是否为0;
     * 若对象为List。判断对象值是否为null，或List元素是否个数为0; 其他类型对象，只判断值是否为null.
     *
     * @param value
     * @return true-是
     */
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        } else if ((value instanceof String)
                && (((String) value).trim().length() < 1)) {
            return true;
        } else if (value.getClass().isArray()) {
            if (0 == java.lang.reflect.Array.getLength(value)) {
                return true;
            }
        } else if (value instanceof List) {
            if (((List) value).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperFirst(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder())
                    .append(Character.toUpperCase(s.charAt(0)))
                    .append(s.substring(1)).toString();
    }

    /**
     * 获取特定元素数组下标
     *
     * @param arry
     * @param element
     * @return
     */
    public static int getArraySuffix(String[] array, String element) {
        if (element == null || array == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (element.equals(array[i])) {
                return i;
            }
        }
        return -1;

    }

    /**
     * 返回与当前时间(小时)最近的数组元素
     */
    @SuppressWarnings("deprecation")
    public static String getEleByArray(String[] array, float diffsize) {
        Date d = new Date();
        float curhour = d.getHours();

        float curmin = d.getMinutes();
        float curtime = curhour + curmin / 60;
        for (int i = 0; i < array.length; i++) {
            float hour = Float.parseFloat(array[i]);
            if (Math.abs(curtime - hour) <= diffsize) {
                return array[i];
            }
        }
        return array[0];

    }

    /**
     * 验证是否为数字
     */
    public static boolean isNumber(String str) {
        if (Tool.isEmpty(str)) {
            return false;
        }
        java.util.regex.Pattern pattern = java.util.regex.Pattern
                .compile("[0-9]*");
        java.util.regex.Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

    // /**
    // * 获取空GUID
    // */
    // public static String getEmptyGuid() {
    // return "00000000-0000-0000-0000-000000000000";
    // }
    public static String getUUIDKey() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();

    }

    public static void main(String[] aragf) {

    }

    public static boolean isPhone(String phone) {
        // String telRegex =
        // "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String regex = "^1\\d*$";
        return phone.matches(regex);
    }

    /**
     * 回收IMAGEVIEW
     *
     * @param imageView
     */
    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null)
            return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
//				bitmap.recycle();
            }
        }
    }

    /**
     * 判断string非空并且去除最后的逗号
     *
     * @param str
     * @return
     */
    public static String checkEmptyAndDeleteEnd(String str) {
        if (Tool.isEmpty(str)) {
            return "";
        } else if (str.endsWith(",")) {
            return str.substring(0, str.length() - 1);
        } else {
            return str;
        }
    }

    /**
     * json的Key值转化为小写
     *
     * @param json
     * @return
     */
    public static String transformLowerCase(String json) {
        String regex = "[\\\"' ]*[^:\\\"' ]*[\\\"' ]*:";// (\{|\,)[a-zA-Z0-9_]+:

        Pattern pattern = Pattern.compile(regex);
        StringBuffer sb = new StringBuffer();
        // 方法二：正则替换
        Matcher m = pattern.matcher(json);
        while (m.find()) {
            m.appendReplacement(sb, m.group().toLowerCase());
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取当前app version name
     */
    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
    }

}
