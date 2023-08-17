package com.bullhead.androidequalizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bullhead.equalizer.DialogEqualizerFragment;
import com.bullhead.equalizer.EnableViewModel;
import com.bullhead.equalizer.EqualizerFragment;
import com.bullhead.equalizer.Settings;
import com.bullhead.equalizer.SettingsArray;
import com.bullhead.equalizer.SimpleViewModel;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "EqualizerActivity";

    public static final int NUMBER = 3;

    //    @NonNull
    private ViewPager2 id_viewpage;

    //    private final String[] titles = {"浪漫海贝", "lenka英文", "歌谣"};
    public static final String[] titles = {"千山万水-jay", "回到过去-jay","英文歌曲", "浪漫海贝", "清洁耳朵", "溪边虫鸣", "钢琴曲7", "雨打芭蕉"};

    public static final String[] uri = {instanceUriStr(R.raw.qsws), instanceUriStr(R.raw.hdgq),instanceUriStr(R.raw.lenka), instanceUriStr(R.raw.lmhb), instanceUriStr(R.raw.qjed), instanceUriStr(R.raw.xbcm), instanceUriStr(R.raw.gqq7), instanceUriStr(R.raw.ydbj)};

    private static String instanceUriStr(int res) {
        return String.format("android.resource://%s/%d", MainApplication.getMain().getPackageName(), res);
    }

    private final int[] colors = {Color.parseColor("#014acf"), Color.parseColor("#f091a6"), Color.parseColor("#d77099")};
    @NonNull
    private ExoPlayer[] mediaPlayers;
    //    private final Observer<Integer>[] observers = new Observer[]{
//            (Observer<Integer>) audioSessionId -> addFragment(audioSessionId, colors[0], R.id.eqFrame1),
//            (Observer<Integer>) audioSessionId -> addFragment(audioSessionId, colors[1], R.id.eqFrame2),
//            (Observer<Integer>) audioSessionId -> addFragment(audioSessionId, colors[2], R.id.eqFrame3),
//    };
    @NonNull
    EnableViewModel vm;
    private SimpleViewModel viewModel;
    //    private View root;
    View root;

    String oneMusic = "android.resource://" + MainApplication.getMain().getPackageName() + "/" + R.raw.qsws;

    private ExoPlayer[] sum3() {
        return new ExoPlayer[]{
                EnableViewModel.exoPlaySImple(this, this, oneMusic)//MediaPlayer.create(this, R.raw.qsws);
//                ,EnableViewModel.exoPlaySImple(this, this, "android.resource://" + getPackageName() + "/" + R.raw.shh)
//                ,EnableViewModel.exoPlaySImple(this, this, "android.resource://" + getPackageName() + "/" + R.raw.th)
        };
    }

    View choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SimpleViewModel.class);
        setContentView(R.layout.activity_main);
        viewModel.observerMusicUri(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null) {
//                    findViewById(R.id.choose).setVisibility(View.GONE);
                }
            }
        });
        findViewById(R.id.choose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChooseRawDialogFragment(R.layout.dialog_recycle).show(getSupportFragmentManager(), "haha");
//                chooseMusic(MainActivity.this);
            }
        });
//        setContentView(R.layout.activity_main);
        root = (View) findViewById(R.id.root);

        id_viewpage = findViewById(R.id.id_viewpage);
        vm = new ViewModelProvider(this).get(EnableViewModel.class);
        loadEqualizerSettings();
        ////new ExoPlayer[]{EnableViewModel.exoPlaySImple(this, this, "android.resource://" + getPackageName() + "/" + R.raw.qsws)};//MediaPlayer.create(this, R.raw.qsws);

//        if(viewModel.getMusicUri() == null){
//
//        }
//        setMediaPlayers();
        id_viewpage.setUserInputEnabled(false);

        viewModel.observerMusicUri(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                if (uri != null) {

//                    mediaPlayers[0].pause();
                    if (mediaPlayers != null && mediaPlayers.length > 0) {
                        mediaPlayers[0].stop();
                        mediaPlayers[0].release();
                    }
                    oneMusic = uri.toString();//EnableViewModel.exoPlaySImple(MainActivity.this, MainActivity.this, uri.toString());
                    setMediaPlayers();
                }
            }
        });

        viewModel.observerChoosePosition(this, new Observer<int[]>() {
            @Override
            public void onChanged(int[] ints) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.setMusicUri(Uri.parse(uri[ints[0]]));
                    }
                });
            }
        });
        DefaultLifecycleObserver[] defaultLifecycleObservers = {new EnvironmentalReverbLifecycle(vm)};


        vm.getSeesionId()[0].observe(this, integer -> {
            for (DefaultLifecycleObserver df : defaultLifecycleObservers) {
                getLifecycle().removeObserver(df);
                getLifecycle().addObserver(df);
            }
        });
    }

    private void setMediaPlayers() {
        mediaPlayers = sum3();
        foreach((exoPlayer, index) -> {
            if (exoPlayer != null) {
                Log.i(TAG, "foreach: " + exoPlayer);
                exoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
                vm.getSeesionId()[index].setValue(exoPlayer.getAudioSessionId());
                if (index + 1 == mediaPlayers.length) {
                    initPage();
                }
            }
            return null;
        });

    }

    private void initPage() {
        id_viewpage.setOffscreenPageLimit(100);
//        id_viewpage.requestDisallowInterceptTouchEvent();
        id_viewpage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        id_viewpage.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return EqualizerFragment.newBuilder()
                        .setAccentColor(colors[position])
                        .setIndex(position)
                        .setTitle(titles[position])
                        .setAudioSessionId(mediaPlayers[position].getAudioSessionId())
                        .build();
            }

            @Override
            public int getItemCount() {
                return mediaPlayers.length;
            }
        });
    }

    private void foreach(Function2<ExoPlayer, Integer, Unit> function0) {
        for (int i = 0; i < mediaPlayers.length; i++) {
            function0.invoke(mediaPlayers[i], i);
        }
    }

    public void addFragment(Integer audioSessionId, int color, int id) {
        if (audioSessionId != null && audioSessionId > 0) {
            EqualizerFragment equalizerFragment = EqualizerFragment.newBuilder()
                    .setAccentColor(color)
                    .setAudioSessionId(audioSessionId)
                    .build();
            getSupportFragmentManager().beginTransaction()
                    .replace(id, equalizerFragment)
                    .commit();

//        getLifecycle().addObserver(new HandlerVirtualizerLifecycle(vm));
//        getLifecycle().addObserver(new HandlerEnvironmentalReverbLifecycle(vm));
        }
    }

    private void showInDialog() {
        Integer sessionId = vm.getSeesionId()[0].getValue();
        if (sessionId != null && sessionId > 0) {
            DialogEqualizerFragment fragment = DialogEqualizerFragment.newBuilder()
                    .setAudioSessionId(sessionId)
                    .title(R.string.app_name)
                    .themeColor(ContextCompat.getColor(this, R.color.primaryColor))
                    .textColor(ContextCompat.getColor(this, R.color.textColor))
                    .accentAlpha(ContextCompat.getColor(this, R.color.playingCardColor))
                    .darkColor(ContextCompat.getColor(this, R.color.primaryDarkColor))
                    .setAccentColor(ContextCompat.getColor(this, R.color.secondaryColor))
                    .build();
            fragment.show(getSupportFragmentManager(), "eq");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveEqualizerSettings();
    }

    @Override
    protected void onPause() {
        try {
            foreach((exoPlayer, integer) -> {
                if (exoPlayer != null && exoPlayer.isPlaying()) {
                    exoPlayer.pause();
                }
                return null;
            });
        } catch (Exception ex) {
            //ignore
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ExoPlayer mediaPlayer : mediaPlayers) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            foreach((exoPlayer, integer) -> {
                if (exoPlayer != null && !exoPlayer.isPlaying()) {
                    root.postDelayed(exoPlayer::play, 1000);
                }
                return null;
            });
        } catch (Exception ex) {
            //ignore
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemEqDialog) {
            showInDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveEqualizerSettings() {
        if (Settings.settingsArray != null) {
//            EqualizerSettings settings = new EqualizerSettings();
//            settings.bassStrength = Settings.equalizerModel.getBassStrength();
//            settings.presetPos = Settings.equalizerModel.getPresetPos();
//            settings.reverbPreset = Settings.equalizerModel.getReverbPreset();
//            settings.seekbarpos = Settings.equalizerModel.getSeekbarpos();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

            Gson gson = new Gson();
            preferences.edit()
                    .putString(PREF_KEY, gson.toJson(Settings.settingsArray))
                    .apply();
        }
    }

    private void loadEqualizerSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        Gson gson = new Gson();
        Settings.settingsArray = gson.fromJson(preferences.getString(PREF_KEY, "{}"), SettingsArray.class);

        if (Settings.settingsArray.getList() == null) {
            Settings.settingsArray.setList(new ArrayList<>());
            List<Settings> settings = new ArrayList<>();
            for (int i = 0; i < NUMBER; i++) {
                settings.add(new Settings());
            }
            Settings.settingsArray.setList(settings);
        }
//        EqualizerModel model = new EqualizerModel();
//        model.setBassStrength(settings.bassStrength);
//        model.setPresetPos(settings.presetPos);
//        model.setReverbPreset(settings.reverbPreset);
//        model.setSeekbarpos(settings.seekbarpos);
//
//        Settings.isEqualizerEnabled = true;
//        Settings.isEqualizerReloaded = true;
//        Settings.bassStrength = settings.bassStrength;
//        Settings.presetPos = settings.presetPos;
//        Settings.reverbPreset = settings.reverbPreset;
//        Settings.seekbarpos = settings.seekbarpos;
//        Settings.equalizerModel = model;
    }

    public static final String PREF_KEY = "equalizer";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EqualizerFragment.PICK_MUSIC_REQUEST && resultCode == RESULT_OK) {
            // 获取选择的音乐的URI
            Uri selectedMusicUri = data.getData();
            // 处理选择的音乐
            // ...
            viewModel.setMusicUri(selectedMusicUri);
        }
    }

}
