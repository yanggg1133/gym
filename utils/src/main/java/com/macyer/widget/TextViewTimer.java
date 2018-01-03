package com.macyer.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liuxiu on 2017/4/19.
 * <p>
 * textview.setTimer(position,good.getLimitStartTime(),good.getLimitEndTime(),"#ffffff",null);
 */
public class TextViewTimer extends TextView {

    private final String DATA_FORMAT_START = "距开始:%s:%s:%s";
    private final String DATA_FORMAT_END = "距结束:%s:%s:%s";
    private final String DATA_FORMAT_DEFAULT = "已结束";
    private final String DATA_FORMAT_START_MULTI = "距开始\n%s:%s:%s";
    private final String DATA_FORMAT_END_MULTI = "距结束\n%s:%s:%s";
    private final String DATA_FORMAT_DEFAULT_MUL = "已结束\n ";

    private int TIMER_STATUS = 0;
    private final int TIMER_START = 1;//未开始
    private final int TIMER_END = 2;//开始
    private final int TIMER_DEFAULT = 3;//已过期

    private int mCountdownInterval = 1000;
    private int MSG = 1;
    /**
     * 停止时间：剩余倒计时长度+开机时间
     */
    private long mStopTimeInFuture;

    private String startTime;
    /**
     * 剩余倒计时—开始
     */
    private long startTimeL;
    private String endTime;
    /**
     * 剩余倒计时—结束
     */
    private long endTimeL;
    private String color;

    private boolean isMultiLine = false;

    private StatusListner listner;

    public interface StatusListner {
        void status(int status);
    }

    private long temp = 0;

    public void setTimer(int position, String startTime, String endTime, String color, StatusListner listner) {
        setTimer(position, startTime, endTime, color, false, listner);
    }

    public void setTimer(int position, String startTime, String endTime, String color, boolean isMultiLine, StatusListner listner) {
        if (SystemClock.elapsedRealtime() - temp > 300) {
            temp = SystemClock.elapsedRealtime();
            MSG = position + 1;
            this.startTime = startTime;
            this.endTime = endTime;
            this.color = color;
            this.isMultiLine = isMultiLine;
            this.listner = listner;
            processTime();
        }
    }

    private void processTime() {
        if (TextUtils.isEmpty(endTime) || TextUtils.isEmpty(startTime)) {
            TIMER_STATUS = TIMER_DEFAULT;
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateStartTime = sdf.parse(startTime);
                startTimeL = dateStartTime.getTime() - System.currentTimeMillis();
                Date dateEndTime = sdf.parse(endTime);
                endTimeL = dateEndTime.getTime() - System.currentTimeMillis();
                if (startTimeL > 0) {
                    TIMER_STATUS = TIMER_START;
                } else if (endTimeL > 0) {
                    TIMER_STATUS = TIMER_END;
                } else {
                    TIMER_STATUS = TIMER_DEFAULT;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        start();
    }

    private void start() {
        if (null != listner && TIMER_STATUS != 0) listner.status(TIMER_STATUS);
        mHandler.removeMessages(MSG);
        switch (TIMER_STATUS) {
            case TIMER_START:
                mStopTimeInFuture = SystemClock.elapsedRealtime() + startTimeL;
                mHandler.sendMessage(mHandler.obtainMessage(MSG));
                break;
            case TIMER_END:
                mStopTimeInFuture = SystemClock.elapsedRealtime() + endTimeL;
                mHandler.sendMessage(mHandler.obtainMessage(MSG));
                break;
            default:
                setText(isMultiLine ? DATA_FORMAT_DEFAULT_MUL : DATA_FORMAT_DEFAULT);
                break;
        }
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (TextViewTimer.this) {
                long millisLeft = mStopTimeInFuture - SystemClock.elapsedRealtime();
                if (millisLeft <= 0) {
                    switch (TIMER_STATUS) {
                        case TIMER_START:
                            TIMER_STATUS = TIMER_END;
                            mStopTimeInFuture = SystemClock.elapsedRealtime() + endTimeL - startTimeL;
                            onShow(mStopTimeInFuture - SystemClock.elapsedRealtime());
                            sendMessage(mHandler.obtainMessage(MSG));
                            break;
                        case TIMER_END:
                            TIMER_STATUS = TIMER_DEFAULT;
                            mHandler.removeMessages(MSG);
                            setText(isMultiLine ? DATA_FORMAT_DEFAULT_MUL : DATA_FORMAT_DEFAULT);
                            break;
                    }
                    if (null != listner && TIMER_STATUS != 0) listner.status(TIMER_STATUS);
                } else if (millisLeft < mCountdownInterval) {
                    sendMessageDelayed(obtainMessage(MSG), millisLeft);
                } else {
                    long lastTickStart = SystemClock.elapsedRealtime();
                    onShow(millisLeft);
                    // take into account user's onTick taking time to execute
                    long delay = lastTickStart + mCountdownInterval - SystemClock.elapsedRealtime();
                    // special case: user's onTick took more than interval to
                    // complete, skip to next interval
                    while (delay < 0) delay += mCountdownInterval;
                    sendMessageDelayed(obtainMessage(MSG), delay);
                }
            }
        }
    };

    private void onShow(long millisUntilFinished) {
        long tempMillisUntilFinished = millisUntilFinished / 1000;
//        int date = (int) (tempMillisUntilFinished / (60 * 60 * 24));
//        int tempDate = (int) (tempMillisUntilFinished % (60 * 60 * 24));
        int hour = (int) tempMillisUntilFinished / (60 * 60);
        int tempHour = (int) tempMillisUntilFinished % (60 * 60);
        int min = tempHour / 60;
        int seconds = tempHour % 60;
        switch (TIMER_STATUS) {
            case TIMER_START:
                setText(String.format(isMultiLine ? DATA_FORMAT_START_MULTI : DATA_FORMAT_START, hour, min, seconds));
                break;
            case TIMER_END:
                setText(String.format(isMultiLine ? DATA_FORMAT_END_MULTI : DATA_FORMAT_END, hour, min, seconds));
                break;
        }
        if (!TextUtils.isEmpty(color) && color.startsWith("#") && color.length() == 7) {
            setTextColor(Color.parseColor(color));
        } else {
            setTextColor(Color.GRAY);
        }
    }

    public TextViewTimer(Context context) {
        super(context);
    }

    public TextViewTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextViewTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextViewTimer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
