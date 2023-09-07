package com.bullhead.androidequalizer;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.bullhead.androidequalizer.base.BaseActivity;
import com.bullhead.androidequalizer.waveview.Spectrogram;

import java.io.IOException;
import java.io.InputStream;

public class SpectMainActivity extends BaseActivity {
    private static final String TAG = "SpectMainActivity";
    Toolbar toolbar;
    private static final int HANDLER_SPECTROGRAM = 0;
    private WaveFileReader reader = null;
    private int[] data = null;
    private boolean isOpenThisActivity = false;
    //采样率
    private double samplerate = 0;
    //频谱
    private Spectrogram mSpectrogram;
    private Thread thread = null;
    private TextView mTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spect_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        mTitle = (TextView) findViewById(R.id.title);
        mTitle.setText("频谱图");
        mSpectrogram = (Spectrogram) findViewById(R.id.spectrogram);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                mSpectrogram.changeShowType();
                if (mTitle.getText().toString().equals("频谱图")) {
                    mTitle.setText("波形图");
                } else {
                    mTitle.setText("频谱图");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWaveData() {
        try {
            if (data == null) {
                AssetManager am = getAssets();
                InputStream inputStream = am.open("default.wav");
                reader = new WaveFileReader();
                data = reader.initReader(inputStream)[0]; // 获取第一声道
                Log.i(TAG, "initWaveData: data.size = "+data.length);
                //获取采样率
                samplerate = reader.getSampleRate();
                mSpectrogram.setBitspersample(reader.getBitPerSample());//设置采样点的编码长度
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        initWaveData();
        if (!isOpenThisActivity && thread == null) {
            thread = new Thread(specRun);
            isOpenThisActivity = true;
            thread.start();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        isOpenThisActivity = false;
        thread.interrupt();
        thread = null;
        super.onStop();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_SPECTROGRAM:
                    mSpectrogram.ShowSpectrogram((int[]) msg.obj, false, samplerate);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private Runnable specRun = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (isOpenThisActivity) {
                long a;
                long T;
                int[] buf;
                int offset = 0;
                if (data != null && reader.isSuccess()) {
                    while (offset < (data.length - Spectrogram.SAMPLING_TOTAL)) {
                        T = System.nanoTime() / 1000000;
                        buf = new int[Spectrogram.SAMPLING_TOTAL];
                        for (int i = 0; i < Spectrogram.SAMPLING_TOTAL; i++) {
                            buf[i] = data[offset + i];
                        }
                        handler.sendMessage(handler.obtainMessage(
                                HANDLER_SPECTROGRAM, buf));
                        offset += (Spectrogram.SAMPLING_TOTAL * 10) / 17;
                        while (true) {
                            a = System.nanoTime() / 1000000;
                            if ((a - T) >= 100)
                                break;
                        }
                        if (!isOpenThisActivity) {
                            return;
                        }
                    }
                }
            }
        }
    };
}