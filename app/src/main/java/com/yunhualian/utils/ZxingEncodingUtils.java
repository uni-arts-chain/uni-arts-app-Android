package com.yunhualian.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.text.TextUtils;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.common.BitmapResult;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Synopsis     zxing 二维码生成工具类
 * Author       hbc
 * Version      1.0.0
 * Create       2020/6/26 9:14
 * Email
 */
public class ZxingEncodingUtils {

    /**
     * 创建二维码
     *
     * @param content   content
     * @param widthPix  widthPix
     * @param heightPix heightPix
     * @param logoBm    logoBm
     * @return 二维码
     */
//    public static Bitmap createQRCode(String content, int widthPix, int heightPix, Bitmap logoBm) {
//        try {
//            if (content == null || "".equals(content)) {
//                return null;
//            }
//            // 配置参数
//            Map<EncodeHintType, Object> hints = new HashMap<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//            // 容错级别
//            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//            // 图像数据转换，使用了矩阵转换
//            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, widthPix,
//                    heightPix, hints);
//            int[] pixels = new int[widthPix * heightPix];
//            // 下面这里按照二维码的算法，逐个生成二维码的图片，
//            // 两个for循环是图片横列扫描的结果
//            for (int y = 0; y < heightPix; y++) {
//                for (int x = 0; x < widthPix; x++) {
//                    if (bitMatrix.get(x, y)) {
//                        pixels[y * widthPix + x] = 0xff000000;
//                    } else {
//                        pixels[y * widthPix + x] = 0xffffffff;
//                    }
//                }
//            }
//            // 生成二维码图片的格式，使用ARGB_8888
//            Bitmap bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
//            if (logoBm != null) {
//                bitmap = addLogo(bitmap, logoBm);
//            }
//            return compressBySampleSize(bitmap, widthPix, heightPix, true);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }
        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save();
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }


    private static Bitmap compressBySampleSize(Bitmap src, int widthPix, int heightPix, boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = caculateSampleSize(options, widthPix, heightPix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) src.recycle();
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * 计算出所需要压缩的大小
     *
     * @param options
     * @param reqWidth  我们期望的图片的宽，单位px
     * @param reqHeight 我们期望的图片的高，单位px
     * @return
     */
    private static int caculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        if (picWidth > reqWidth || picHeight > reqHeight) {
            int halfPicWidth = picWidth / 2;
            int halfPicHeight = picHeight / 2;
            while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return true: 是 false: 否
     */
    private static boolean isEmptyBitmap(Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

//    public static Bitmap createQRCodeNative(String content, int w, int h, Bitmap logo) {
//
//        if (TextUtils.isEmpty(content)) {
//            return null;
//        }
//        /*偏移量*/
//        int offsetX = w / 2;
//        int offsetY = h / 2;
//
//        /*生成logo*/
//        Bitmap logoBitmap = null;
//
//        if (logo != null) {
//            Matrix matrix = new Matrix();
//            float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 / logo.getHeight());
//            matrix.postScale(scaleFactor, scaleFactor);
//            logoBitmap = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(), logo.getHeight(), matrix, true);
//        }
//
//
//        /*如果log不为null,重新计算偏移量*/
//        int logoW = 0;
//        int logoH = 0;
//        if (logoBitmap != null) {
//            logoW = logoBitmap.getWidth();
//            logoH = logoBitmap.getHeight();
//            offsetX = (w - logoW) / 2;
//            offsetY = (h - logoH) / 2;
//        }
//
//        /*指定为UTF-8*/
//        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        //容错级别
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
//        //设置空白边距的宽度
//        hints.put(EncodeHintType.MARGIN, 0);
//        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
//        BitMatrix matrix = null;
//        try {
//            matrix = new MultiFormatWriter().encode(content,
//                    BarcodeFormat.QR_CODE, w, h, hints);
//
//            // 二维矩阵转为一维像素数组,也就是一直横着排了
//            int[] pixels = new int[w * h];
//            for (int y = 0; y < h; y++) {
//                for (int x = 0; x < w; x++) {
//                    if (x >= offsetX && x < offsetX + logoW && y >= offsetY && y < offsetY + logoH) {
//                        int pixel = logoBitmap.getPixel(x - offsetX, y - offsetY);
//                        if (pixel == 0) {
//                            if (matrix.get(x, y)) {
//                                pixel = 0xff000000;
//                            } else {
//                                pixel = 0xffffffff;
//                            }
//                        }
//                        pixels[y * w + x] = pixel;
//                    } else {
//                        if (matrix.get(x, y)) {
//                            pixels[y * w + x] = 0xff000000;
//                        } else {
//                            pixels[y * w + x] = 0xffffffff;
//                        }
//                    }
//                }
//            }
//
//            Bitmap bitmap = Bitmap.createBitmap(w, h,
//                    Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
//
//            BitmapResult mBitmap = Tiny.getInstance().source(bitmap).asBitmap().compressSync();
//            return null != mBitmap && null != mBitmap.bitmap ? mBitmap.bitmap : bitmap;
//
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
