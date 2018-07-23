package com.billy.textbroadcast;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.billy.textbroadcast.databinding.ActivityXunFeiBinding;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;


/**
 * Created by Billy_Cui on 2018/7/23
 * Describe:  通过科大讯飞 实现 文字转语音
 */

public class XunFeiActivity extends AppCompatActivity implements View.OnClickListener, InitListener {

    private ActivityXunFeiBinding mBinding;
    //语音合成对象
    private SpeechSynthesizer mSynthesizer;
    // 默认本地发音人
    public static String voicerLocal="xiaoyan";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_xun_fei);
        mBinding.setOnClick(this);
        //初始化科大讯飞
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=5b558975");
        mSynthesizer = SpeechSynthesizer.createSynthesizer(this, this);
    }

    private void speak() {
        mSynthesizer.startSpeaking(mBinding.editTextXf.getText().toString(),mSynthesizerListener);
    }

    /*
        合成回调
     */
   private SynthesizerListener mSynthesizerListener = new SynthesizerListener() {
       @Override
       public void onSpeakBegin() {
           //开始播放
       }

       @Override
       public void onBufferProgress(int i, int i1, int i2, String s) {
            //合成进度
       }

       @Override
       public void onSpeakPaused() {
           //暂停播放

       }

       @Override
       public void onSpeakResumed() {
            //继续播放
       }

       @Override
       public void onSpeakProgress(int i, int i1, int i2) {
            //播放进度
       }

       @Override
       public void onCompleted(SpeechError speechError) {
            if (speechError == null){//播放完成

            }else {

            }
       }

       @Override
       public void onEvent(int i, int i1, int i2, Bundle bundle) {
           // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
           // 若使用本地能力，会话id为null
       }
   };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_xf) {
            speak();
        }
    }

    @Override
    public void onInit(int i) {
        if (i != ErrorCode.SUCCESS){
            return;//初始化失败
        }
        mSynthesizer.setParameter(SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_MSC);
        mSynthesizer.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        mSynthesizer.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
        mSynthesizer.setParameter(SpeechConstant.VOICE_NAME, voicerLocal);
    }


    /**
     * 获取本地assets文件夹下的发音人资源路径
     */
    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //合成通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(getApplicationContext(), ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
        tempBuffer.append(";");
        //发音人资源
        tempBuffer.append(ResourceUtil.generateResourcePath(getApplicationContext(), ResourceUtil.RESOURCE_TYPE.assets, "tts/"+ voicerLocal +".jet"));
//        fo|/data/app/com.billy.textbroadcast-2/base.apk|15314892|4307649;fo|/data/app/com.billy.textbroadcast-2/base.apk|20956372|4104654
        return tempBuffer.toString();
    }

    @Override
    protected void onDestroy() {
        mSynthesizer.destroy();
        super.onDestroy();
    }
}
