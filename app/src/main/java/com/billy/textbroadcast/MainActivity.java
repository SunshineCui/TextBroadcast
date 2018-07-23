package com.billy.textbroadcast;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.billy.textbroadcast.databinding.ActivityMainBinding;

import java.util.Locale;

/**
 * 通过 TextToSpeech 实现文字转语音
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    private TextToSpeech mTextToSpeech;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setOnClick(this);
        mTextToSpeech = new TextToSpeech(this,this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_main){
            speakOut();
        }else if (v.getId() == R.id.button_skip){
            Intent intent = new Intent(this,XunFeiActivity.class);
            startActivity(intent);
        }
    }

    private void speakOut() {
        if (mTextToSpeech!= null && !mTextToSpeech.isSpeaking()){
//            Bundle bundle = new Bundle();
//            bundle.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME,1.0f);
            //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
//            mTextToSpeech.speak(mBinding.editText.getText().toString(),TextToSpeech.QUEUE_FLUSH,null);
            mTextToSpeech.speak(mBinding.editText.getText().toString(),TextToSpeech.QUEUE_FLUSH,null,"");
        }
    }

    /**
     * 初始化语音播放
     * @param status
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            // 设置音调,1.0是常规
            mTextToSpeech.setPitch(1.0f);
            //设定语速 ，默认1.0正常语速
            mTextToSpeech.setSpeechRate(1.0f);
            int result = mTextToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this,"数据丢失或不支持",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onDestroy() {
        if (mTextToSpeech != null){
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();//关闭,释放资源
            mTextToSpeech = null;
        }
        super.onDestroy();
    }
}
