package com.zrq.videodemo.view;

import static xyz.doikki.videoplayer.util.PlayerUtils.dp2px;
import static xyz.doikki.videoplayer.util.PlayerUtils.stringForTime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zrq.videodemo.R;
import com.zrq.videodemo.utils.OtherUtils;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import xyz.doikki.videoplayer.controller.ControlWrapper;
import xyz.doikki.videoplayer.controller.IControlComponent;
import xyz.doikki.videoplayer.player.VideoView;
import xyz.doikki.videoplayer.util.PlayerUtils;

/**
 * 点播底部控制栏
 */
public class MyVodControlView extends FrameLayout implements IControlComponent, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "MyVodControlView";
    protected ControlWrapper mControlWrapper;

    private TextView mTotalTime;
    private TextView mCurrTime;
    private ImageView mHorizontalFullScreen;
    private LinearLayout mBottomContainer;
    private SeekBar mVideoProgress;
    private ProgressBar mBottomProgress;
    private ImageView mPlayButton;
    private TextView mTv5, mTv75, mTv1, mTv125, mTv15, mTv175, mTv2;
    private Activity mActivity = PlayerUtils.scanForActivity(getContext());

    private boolean mIsDragging;

    private boolean mIsShowBottomProgress = true;

    private Function2<Integer, Boolean, Unit> onProgressChange;
    private int mItemHeight;

    public void setOnProgressChange(Function2<Integer, Boolean, Unit> onProgressChange) {
        this.onProgressChange = onProgressChange;
    }

    public MyVodControlView(@NonNull Context context) {
        this(context, null);
    }

    public MyVodControlView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVodControlView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private LinearLayout mLlSpeed;

    private TextView mTvSpeed;

    private void init(Context context) {
        int windowWidth = OtherUtils.INSTANCE.getWindowWidth(mActivity);
        mItemHeight = (windowWidth - dp2px(context, 50f)) / 7;
        Log.d(TAG, "mItemHeight: " + mItemHeight);
        setVisibility(GONE);
        LayoutInflater.from(getContext()).inflate(getLayoutId(), this, true);
        mHorizontalFullScreen = findViewById(R.id.horizontalFullscreen);
        mHorizontalFullScreen.setOnClickListener(this);
        mBottomContainer = findViewById(R.id.bottom_container);
        mVideoProgress = findViewById(R.id.seekBar);
        mVideoProgress.setOnSeekBarChangeListener(this);
        mTotalTime = findViewById(R.id.total_time);
        mCurrTime = findViewById(R.id.curr_time);
        mPlayButton = findViewById(R.id.iv_play);
        mPlayButton.setOnClickListener(this);
        mBottomProgress = findViewById(R.id.bottom_progress);
        mLlSpeed = findViewById(R.id.llSpeed);
        mTvSpeed = findViewById(R.id.tvSpeed);
        mTvSpeed.setOnClickListener(this);
        mTv5 = findViewById(R.id.tv5);
        mTv75 = findViewById(R.id.tv75);
        mTv1 = findViewById(R.id.tv1);
        mTv125 = findViewById(R.id.tv125);
        mTv15 = findViewById(R.id.tv15);
        mTv175 = findViewById(R.id.tv175);
        mTv2 = findViewById(R.id.tv2);
        mTv5.setOnClickListener(this);
        mTv75.setOnClickListener(this);
        mTv1.setOnClickListener(this);
        mTv125.setOnClickListener(this);
        mTv15.setOnClickListener(this);
        mTv175.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        Log.d(TAG, "instance initializer: " + mItemHeight);
        mTv5.setHeight(mItemHeight);
        mTv75.setHeight(mItemHeight);
        mTv1.setHeight(mItemHeight);
        mTv125.setHeight(mItemHeight);
        mTv15.setHeight(mItemHeight);
        mTv175.setHeight(mItemHeight);
        mTv2.setHeight(mItemHeight);

        //5.1以下系统SeekBar高度需要设置成WRAP_CONTENT

    }

    protected int getLayoutId() {
        return R.layout.my_video_bottom_bar;
    }

    /**
     * 是否显示底部进度条，默认显示
     */
    public void showBottomProgress(boolean isShow) {
        mIsShowBottomProgress = isShow;
    }

    @Override
    public void attach(@NonNull ControlWrapper controlWrapper) {
        mControlWrapper = controlWrapper;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void onVisibilityChanged(boolean isVisible, Animation anim) {
        if (isVisible) {
            mBottomContainer.setVisibility(VISIBLE);
            if (anim != null) {
                mBottomContainer.startAnimation(anim);
            }
            if (mIsShowBottomProgress) {
                mBottomProgress.setVisibility(GONE);
            }
        } else {
            mBottomContainer.setVisibility(GONE);
            if (anim != null) {
                mBottomContainer.startAnimation(anim);
            }
            if (mIsShowBottomProgress) {
                mBottomProgress.setVisibility(VISIBLE);
                AlphaAnimation animation = new AlphaAnimation(0f, 1f);
                animation.setDuration(300);
                mBottomProgress.startAnimation(animation);
            }
        }
    }

    @Override
    public void onPlayStateChanged(int playState) {
        switch (playState) {
            case VideoView.STATE_IDLE:
            case VideoView.STATE_PLAYBACK_COMPLETED:
                setVisibility(GONE);
                mBottomProgress.setProgress(0);
                mBottomProgress.setSecondaryProgress(0);
                mVideoProgress.setProgress(0);
                mVideoProgress.setSecondaryProgress(0);
                break;
            case VideoView.STATE_START_ABORT:
            case VideoView.STATE_PREPARING:
            case VideoView.STATE_PREPARED:
            case VideoView.STATE_ERROR:
                setVisibility(GONE);
                break;
            case VideoView.STATE_PLAYING:
                mPlayButton.setSelected(true);
                if (mIsShowBottomProgress) {
                    if (mControlWrapper.isShowing()) {
                        mBottomProgress.setVisibility(GONE);
                        mBottomContainer.setVisibility(VISIBLE);
                    } else {
                        mBottomContainer.setVisibility(GONE);
                        mBottomProgress.setVisibility(VISIBLE);
                    }
                } else {
                    mBottomContainer.setVisibility(GONE);
                }
                setVisibility(VISIBLE);
                //开始刷新进度
                mControlWrapper.startProgress();
                break;
            case VideoView.STATE_PAUSED:
                mPlayButton.setSelected(false);
                break;
            case VideoView.STATE_BUFFERING:
                mPlayButton.setSelected(mControlWrapper.isPlaying());
                // 停止刷新进度
                mControlWrapper.stopProgress();
                break;
            case VideoView.STATE_BUFFERED:
                mPlayButton.setSelected(mControlWrapper.isPlaying());
                //开始刷新进度
                mControlWrapper.startProgress();
                break;
        }
    }

    @Override
    public void onPlayerStateChanged(int playerState) {
        switch (playerState) {
            case VideoView.PLAYER_NORMAL:
                Log.d(TAG, "onPlayerStateChanged: PLAYER_NORMAL");
                mHorizontalFullScreen.setSelected(false);
                mHorizontalFullScreen.setVisibility(VISIBLE);
                mLlSpeed.setVisibility(GONE);
                mTvSpeed.setVisibility(GONE);
                break;
            case VideoView.PLAYER_FULL_SCREEN:
                mHorizontalFullScreen.setSelected(true);
                mTvSpeed.setVisibility(VISIBLE);
                break;
        }

        Activity activity = PlayerUtils.scanForActivity(getContext());
        if (activity != null && mControlWrapper.hasCutout()) {
            int orientation = activity.getRequestedOrientation();
            int cutoutHeight = mControlWrapper.getCutoutHeight();
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                mBottomContainer.setPadding(0, 0, 0, 0);
                mBottomProgress.setPadding(0, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                mBottomContainer.setPadding(cutoutHeight, 0, 0, 0);
                mBottomProgress.setPadding(cutoutHeight, 0, 0, 0);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                mBottomContainer.setPadding(0, 0, cutoutHeight, 0);
                mBottomProgress.setPadding(0, 0, cutoutHeight, 0);
            }
        }
    }

    @Override
    public void setProgress(int duration, int position) {
        Log.d(TAG, "setProgress: " + position);
        if (mIsDragging) {
            return;
        }

        if (mVideoProgress != null) {
            if (duration > 0) {
                mVideoProgress.setEnabled(true);
                int pos = (int) (position * 1.0 / duration * mVideoProgress.getMax());
                mVideoProgress.setProgress(pos);
                mBottomProgress.setProgress(pos);
            } else {
                mVideoProgress.setEnabled(false);
            }
            int percent = mControlWrapper.getBufferedPercentage();
            if (percent >= 95) { //解决缓冲进度不能100%问题
                mVideoProgress.setSecondaryProgress(mVideoProgress.getMax());
                mBottomProgress.setSecondaryProgress(mBottomProgress.getMax());
            } else {
                mVideoProgress.setSecondaryProgress(percent * 10);
                mBottomProgress.setSecondaryProgress(percent * 10);
            }
        }

        if (mTotalTime != null)
            mTotalTime.setText(stringForTime(duration));
        if (mCurrTime != null)
            mCurrTime.setText(stringForTime(position));
    }

    @Override
    public void onLockStateChanged(boolean isLocked) {
        onVisibilityChanged(!isLocked, null);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.horizontalFullscreen) {
            horizontalFullScreen();
        } else if (id == R.id.iv_play) {
            mControlWrapper.togglePlay();
        } else if (id == R.id.tvSpeed) {
            mLlSpeed.setVisibility(mLlSpeed.getVisibility() == VISIBLE ? GONE : VISIBLE);
        } else if (id == R.id.tv5) {
            mControlWrapper.setSpeed(0.5f);
            mLlSpeed.setVisibility(GONE);
            mTvSpeed.setText("0.5X");
        } else if (id == R.id.tv75) {
            mControlWrapper.setSpeed(0.75f);
            mLlSpeed.setVisibility(GONE);
            mTvSpeed.setText("0.75X");
        } else if (id == R.id.tv1) {
            mControlWrapper.setSpeed(1f);
            mLlSpeed.setVisibility(GONE);
            mTvSpeed.setText("倍速");
        } else if (id == R.id.tv125) {
            mControlWrapper.setSpeed(1.25f);
            mLlSpeed.setVisibility(GONE);
            mTvSpeed.setText("1.25X");
        } else if (id == R.id.tv15) {
            mControlWrapper.setSpeed(1.5f);
            mLlSpeed.setVisibility(GONE);
            mTvSpeed.setText("1.5X");
        } else if (id == R.id.tv175) {
            mControlWrapper.setSpeed(1.75f);
            mLlSpeed.setVisibility(GONE);
            mTvSpeed.setText("1.75X");
        } else if (id == R.id.tv2) {
            mControlWrapper.setSpeed(2f);
            mLlSpeed.setVisibility(GONE);
            mTvSpeed.setText("2X");
        }
    }

    /**
     * 横屏全屏切换
     */
    private void horizontalFullScreen() {
        mControlWrapper.toggleFullScreen(mActivity);
        Log.d(TAG, "horizontalFullScreen: " + mControlWrapper.isFullScreen());
    }

    /**
     * 竖屏全屏切换
     */
    private void verticalFullScreen() {
        mControlWrapper.toggleFullScreen();
        Log.d(TAG, "verticalFullScreen: " + mControlWrapper.isFullScreen());
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mIsDragging = true;
        mControlWrapper.stopProgress();
        mControlWrapper.stopFadeOut();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        long duration = mControlWrapper.getDuration();
        long newPosition = (duration * seekBar.getProgress()) / mVideoProgress.getMax();
        mControlWrapper.seekTo((int) newPosition);
        mIsDragging = false;
        mControlWrapper.startProgress();
        mControlWrapper.startFadeOut();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d(TAG, "progress: " + progress + "fromUser: " + fromUser);
        if (onProgressChange != null)
            onProgressChange.invoke(progress * 100, fromUser);
        if (!fromUser) {
            return;
        }

        long duration = mControlWrapper.getDuration();
        long newPosition = (duration * progress) / mVideoProgress.getMax();
        if (mCurrTime != null)
            mCurrTime.setText(stringForTime((int) newPosition));
    }

}
