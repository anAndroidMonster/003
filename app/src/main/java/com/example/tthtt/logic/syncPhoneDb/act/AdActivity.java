package com.example.tthtt.logic.syncPhoneDb.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.tthtt.R;
import com.example.tthtt.lib.utils.StringUtil;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;

import java.util.List;

/**
 * Created by book4 on 2018/4/28.
 */

public class AdActivity extends Activity {
    private NativeExpressADView nativeExpressADView;
    private Handler mHandler = new Handler();
    private FrameLayout mLayAd;
    private long mStartTime;

    public static void enterActivity(Context context){
        Intent intent = new Intent(context, AdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        mLayAd = (FrameLayout) findViewById(R.id.lay_ad);
        getNativeAd("1101152570", "7030020348049331");
    }

    private void getNativeAd(String appId, final String adId){
        NativeExpressAD nativeExpressAD = new NativeExpressAD(AdActivity.this, new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), appId, adId, new NativeExpressAD.NativeExpressADListener() {
            @Override
            public void onNoAD(AdError adError) {
                if(!StringUtil.isEmpty(adError.getErrorMsg())){
                    Toast.makeText(AdActivity.this, adError.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
                if(mHandler != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mLayAd.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onADLoaded(List<NativeExpressADView> list) {
                if (nativeExpressADView != null) {
                    nativeExpressADView.destroy();
                }
                // 3.返回数据后，SDK会返回可以用于展示NativeExpressADView列表
                nativeExpressADView = list.get(0);
                nativeExpressADView.render();
                long timeBet = System.currentTimeMillis() - mStartTime;
                timeBet = timeBet /1000;
                Toast.makeText(AdActivity.this, "加载时间" + timeBet + "秒", Toast.LENGTH_SHORT).show();
                if(mHandler != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mLayAd.addView(nativeExpressADView);
                        }
                    });
                }
            }

            @Override
            public void onRenderFail(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADExposure(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADClicked(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADClosed(NativeExpressADView nativeExpressADView) {
                if(mHandler != null){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mLayAd.setVisibility(View.GONE);
                        }
                    });
                }
            }

            @Override
            public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

            }

            @Override
            public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

            }
        });
        nativeExpressAD.setVideoOption(new VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI)
                .setAutoPlayMuted(true)
                .build());
        nativeExpressAD.loadAD(1);
        mStartTime = System.currentTimeMillis();
    }
}
