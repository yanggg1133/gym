package fitnessroom.hxs.com.codescan;

import android.hardware.Camera;

/**
 * @className: CommonUtil
 * @classDescription: 通用工具类
 * @author: miao
 * @createTime: 2017年2月10日
 */
public class CameraUtil
{
    public static final int auto_focus = 100;
    public static final int decode = 200;
    public static final int decode_failed = 300;
    public static final int decode_succeeded = 400;
    public static final int encode_failed = 500;
    public static final int encode_succeeded = 600;
    public static final int launch_product_query = 700;
    public static final int quit = 800;
    public static final int restart_preview = 900;
    public static final int return_scan_result = 1000;
    public static final int search_book_contents_failed = 1100;
    public static final int search_book_contents_succeeded = 1200;

    /**
     * @author miao
     * @createTime 2017年2月10日
     * @lastModify 2017年2月10日
     * @param
     * @return
     */
    public static boolean isCameraCanUse() {
            boolean canUse = true;
            Camera mCamera = null;
            try {
                mCamera = Camera.open();
            } catch (Exception e) {
                canUse = false;
            }
            if (canUse) {
                if (mCamera != null)
                    mCamera.release();
                mCamera = null;
            }
            return canUse;
    }
}
