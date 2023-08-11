package com.bullhead.equalizer;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.bullhead.equalizer.view.AxisController;
import com.bullhead.equalizer.view.ChartView;
import com.bullhead.equalizer.view.LineChartView;
import com.bullhead.equalizer.view.LineSet;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EqualizerFragment extends Fragment {

    private static final String TAG = "EqualizerFragment";

    public static final String ARG_AUDIO_SESSIOIN_ID = "audio_session_id";
    public static final int PICK_MUSIC_REQUEST = 0x98;
    int themeColor2 = Color.parseColor("#B24242");
    static int themeColor = Color.parseColor("#B24242");
    public Equalizer mEqualizer;
    SwitchCompat equalizerSwitch;
    public BassBoost bassBoost;
    LineChartView chart;
    public PresetReverb presetReverb;
    ImageView backBtn;

    int y = 0;

    ImageView spinnerDropDownIcon;
    TextView fragTitle;
    LinearLayout mLinearLayout;

    SeekBar[] seekBarFinal = new SeekBar[5];

    AnalogController bassController, reverbController;

    Spinner presetSpinner;

    FrameLayout equalizerBlocker;


    Context ctx;
    private String title = "Equalizer";

    public EqualizerFragment() {
        // Required empty public constructor
    }

    LineSet dataset;
    Paint paint;
    float[] points;
    short numberOfFrequencyBands;
    private int audioSesionId;
    static boolean showBackButton = true;

    int index;

    public static EqualizerFragment newInstance(String title, int audioSessionId, int themColor, int index) {

        Bundle args = new Bundle();
        args.putInt(ARG_AUDIO_SESSIOIN_ID, audioSessionId);
        args.putInt("color", themColor);
        args.putInt("index", index);
        args.putString("title", title == null ? "Equalizer" : title);
        EqualizerFragment fragment = new EqualizerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = getArguments().getInt("index", index);
        Settings.indexBy(index).isEditing = true;

        if (getArguments() != null && getArguments().containsKey(ARG_AUDIO_SESSIOIN_ID)) {
            audioSesionId = getArguments().getInt(ARG_AUDIO_SESSIOIN_ID);
            themeColor = getArguments().getInt("color", Color.parseColor("#B24242"));
            title = getArguments().getString("title");
        }

        if (Settings.indexBy(index).equalizerModel == null) {
            Settings.indexBy(index).equalizerModel = new EqualizerModel();
            Settings.indexBy(index).equalizerModel.setReverbPreset(PresetReverb.PRESET_NONE);
            Settings.indexBy(index).equalizerModel.setBassStrength((short) (1000 / 19));
        }

        Log.i(TAG, "onCreate: audioSesionId = " + audioSesionId);
        mEqualizer = new Equalizer(0, audioSesionId);
        bassBoost = new BassBoost(0, audioSesionId);
        bassBoost.setEnabled(Settings.indexBy(index).isEqualizerEnabled);
        BassBoost.Settings bassBoostSettingTemp = bassBoost.getProperties();
        BassBoost.Settings bassBoostSetting = new BassBoost.Settings(bassBoostSettingTemp.toString());
        bassBoostSetting.strength = Settings.indexBy(index).equalizerModel.getBassStrength();
        bassBoost.setProperties(bassBoostSetting);

        presetReverb = new PresetReverb(0, audioSesionId);
        presetReverb.setPreset(Settings.indexBy(index).equalizerModel.getReverbPreset());
        presetReverb.setEnabled(Settings.indexBy(index).isEqualizerEnabled);

        mEqualizer.setEnabled(Settings.indexBy(index).isEqualizerEnabled);

        if (Settings.indexBy(index).presetPos == 0) {
            for (short bandIdx = 0; bandIdx < mEqualizer.getNumberOfBands(); bandIdx++) {
                mEqualizer.setBandLevel(bandIdx, (short) Settings.indexBy(index).seekbarpos[bandIdx]);
            }
        } else {
            mEqualizer.usePreset((short) Settings.indexBy(index).presetPos);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equalizer, container, false);
    }


    public static void chooseMusic(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
// 设置Intent的类型为音乐类型
        intent.setType("audio/*");
// 启动音乐选择器
        activity.startActivityForResult(intent, PICK_MUSIC_REQUEST);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backBtn = view.findViewById(R.id.equalizer_back_btn);
        backBtn.setVisibility(showBackButton ? View.VISIBLE : View.GONE);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        fragTitle = view.findViewById(R.id.equalizer_fragment_title);
//        fragTitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chooseMusic(requireActivity());
//            }
//        });
        fragTitle.setVisibility(View.GONE);
        fragTitle.setText(title);

        equalizerSwitch = view.findViewById(R.id.equalizer_switch);
        equalizerSwitch.setChecked(Settings.indexBy(index).isEqualizerEnabled);
        equalizerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                new ViewModelProvider(requireActivity()).get(EnableViewModel.class).getEnable().setValue(isChecked);
                mEqualizer.setEnabled(isChecked);
                bassBoost.setEnabled(isChecked);
                presetReverb.setEnabled(isChecked);
                Settings.indexBy(index).isEqualizerEnabled = isChecked;
                Settings.indexBy(index).equalizerModel.setEqualizerEnabled(isChecked);
            }
        });

        spinnerDropDownIcon = view.findViewById(R.id.spinner_dropdown_icon);
        spinnerDropDownIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presetSpinner.performClick();
            }
        });

        presetSpinner = view.findViewById(R.id.equalizer_preset_spinner);

        equalizerBlocker = view.findViewById(R.id.equalizerBlocker);


        chart = view.findViewById(R.id.lineChart);
        paint = new Paint();
        dataset = new LineSet();

        bassController = view.findViewById(R.id.controllerBass);
        reverbController = view.findViewById(R.id.controller3D);

        bassController.setLabel("BASS");
        reverbController.setLabel("3D");

        bassController.circlePaint2.setColor(themeColor);
        bassController.linePaint.setColor(themeColor);
        bassController.invalidate();
        reverbController.circlePaint2.setColor(themeColor);
        bassController.linePaint.setColor(themeColor);
        reverbController.invalidate();

        if (!Settings.indexBy(index).isEqualizerReloaded) {
            int x = 0;
            if (bassBoost != null) {
                try {
                    x = ((bassBoost.getRoundedStrength() * 19) / 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (presetReverb != null) {
                try {
                    y = (presetReverb.getPreset() * 19) / 6;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (x == 0) {
                bassController.setProgress(1);
            } else {
                bassController.setProgress(x);
            }

            if (y == 0) {
                reverbController.setProgress(1);
            } else {
                reverbController.setProgress(y);
            }
        } else {
            int x = ((Settings.indexBy(index).bassStrength * 19) / 1000);
            y = (Settings.indexBy(index).reverbPreset * 19) / 6;
            if (x == 0) {
                bassController.setProgress(1);
            } else {
                bassController.setProgress(x);
            }

            if (y == 0) {
                reverbController.setProgress(1);
            } else {
                reverbController.setProgress(y);
            }
        }

        bassController.setOnProgressChangedListener(new AnalogController.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                Settings.indexBy(index).bassStrength = (short) (((float) 1000 / 19) * (progress));
                try {
                    bassBoost.setStrength(Settings.indexBy(index).bassStrength);
                    Settings.indexBy(index).equalizerModel.setBassStrength(Settings.indexBy(index).bassStrength);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        reverbController.setOnProgressChangedListener(new AnalogController.onProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress) {
                Settings.indexBy(index).reverbPreset = (short) ((progress * 6) / 19);
                Settings.indexBy(index).equalizerModel.setReverbPreset(Settings.indexBy(index).reverbPreset);
                try {
                    presetReverb.setPreset(Settings.indexBy(index).reverbPreset);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                y = progress;
            }
        });

        mLinearLayout = view.findViewById(R.id.equalizerContainer);

        TextView equalizerHeading = new TextView(getContext());
        equalizerHeading.setText(R.string.eq);
        equalizerHeading.setTextSize(20);
        equalizerHeading.setGravity(Gravity.CENTER_HORIZONTAL);

        numberOfFrequencyBands = 5;

        points = new float[numberOfFrequencyBands];

        final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];
        final short upperEqualizerBandLevel = mEqualizer.getBandLevelRange()[1];

        for (short i = 0; i < numberOfFrequencyBands; i++) {
            final short equalizerBandIndex = i;
            final TextView frequencyHeaderTextView = new TextView(getContext());
            frequencyHeaderTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            frequencyHeaderTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            frequencyHeaderTextView.setTextColor(Color.parseColor("#FFFFFF"));
            frequencyHeaderTextView.setText((mEqualizer.getCenterFreq(equalizerBandIndex) / 1000) + "Hz");

            LinearLayout seekBarRowLayout = new LinearLayout(getContext());
            seekBarRowLayout.setOrientation(LinearLayout.VERTICAL);

            TextView lowerEqualizerBandLevelTextView = new TextView(getContext());
            lowerEqualizerBandLevelTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            lowerEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"));
            lowerEqualizerBandLevelTextView.setText((lowerEqualizerBandLevel / 100) + "dB");

            TextView upperEqualizerBandLevelTextView = new TextView(getContext());
            lowerEqualizerBandLevelTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            upperEqualizerBandLevelTextView.setTextColor(Color.parseColor("#FFFFFF"));
            upperEqualizerBandLevelTextView.setText((upperEqualizerBandLevel / 100) + "dB");

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            layoutParams.weight = 1;

            SeekBar seekBar = new SeekBar(getContext());
            TextView textView = new TextView(getContext());
            switch (i) {
                case 0:
                    seekBar = view.findViewById(R.id.seekBar1);
                    textView = view.findViewById(R.id.textView1);
                    break;
                case 1:
                    seekBar = view.findViewById(R.id.seekBar2);
                    textView = view.findViewById(R.id.textView2);
                    break;
                case 2:
                    seekBar = view.findViewById(R.id.seekBar3);
                    textView = view.findViewById(R.id.textView3);
                    break;
                case 3:
                    seekBar = view.findViewById(R.id.seekBar4);
                    textView = view.findViewById(R.id.textView4);
                    break;
                case 4:
                    seekBar = view.findViewById(R.id.seekBar5);
                    textView = view.findViewById(R.id.textView5);
                    break;
            }
            seekBarFinal[i] = seekBar;
            seekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_IN));
            seekBar.getThumb().setColorFilter(new PorterDuffColorFilter(themeColor, PorterDuff.Mode.SRC_IN));
            seekBar.setId(i);
//            seekBar.setLayoutParams(layoutParams);
            seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);

            textView.setText(frequencyHeaderTextView.getText());
            textView.setTextColor(Color.WHITE);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            if (Settings.indexBy(index).isEqualizerReloaded) {
                points[i] = Settings.indexBy(index).seekbarpos[i] - lowerEqualizerBandLevel;
                dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                seekBar.setProgress(Settings.indexBy(index).seekbarpos[i] - lowerEqualizerBandLevel);
            } else {
                points[i] = mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
                dataset.addPoint(frequencyHeaderTextView.getText().toString(), points[i]);
                seekBar.setProgress(mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel);
                Settings.indexBy(index).seekbarpos[i] = mEqualizer.getBandLevel(equalizerBandIndex);
                Settings.indexBy(index).isEqualizerReloaded = true;
            }

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mEqualizer.setBandLevel(equalizerBandIndex, (short) (progress + lowerEqualizerBandLevel));
                    points[seekBar.getId()] = mEqualizer.getBandLevel(equalizerBandIndex) - lowerEqualizerBandLevel;
                    Settings.indexBy(index).seekbarpos[seekBar.getId()] = (progress + lowerEqualizerBandLevel);
                    Settings.indexBy(index).equalizerModel.getSeekbarpos()[seekBar.getId()] = (progress + lowerEqualizerBandLevel);
                    dataset.updateValues(points);
                    chart.notifyDataUpdate();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    presetSpinner.setSelection(0);
                    Settings.indexBy(index).presetPos = 0;
                    Settings.indexBy(index).equalizerModel.setPresetPos(0);
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        equalizeSound();

        paint.setColor(Color.parseColor("#555555"));
        paint.setStrokeWidth((float) (1.10 * Settings.indexBy(index).ratio));

        dataset.setColor(themeColor);
        dataset.setSmooth(true);
        dataset.setThickness(5);

        chart.setXAxis(false);
        chart.setYAxis(false);

        chart.setYLabels(AxisController.LabelPosition.NONE);
        chart.setXLabels(AxisController.LabelPosition.NONE);
        chart.setGrid(ChartView.GridType.NONE, 7, 10, paint);

        chart.setAxisBorderValues(-300, 3300);

        chart.addData(dataset);
        chart.show();

        Button mEndButton = new Button(getContext());
        mEndButton.setBackgroundColor(themeColor);
        mEndButton.setTextColor(Color.WHITE);


    }

    public void equalizeSound() {
        ArrayList<String> equalizerPresetNames = new ArrayList<>();
        ArrayAdapter<String> equalizerPresetSpinnerAdapter = new ArrayAdapter<>(ctx,
                R.layout.spinner_item,
                equalizerPresetNames);
        equalizerPresetSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        equalizerPresetNames.add("Custom");

        for (short i = 0; i < mEqualizer.getNumberOfPresets(); i++) {
            equalizerPresetNames.add(mEqualizer.getPresetName(i));
        }

        presetSpinner.setAdapter(equalizerPresetSpinnerAdapter);
        //presetSpinner.setDropDownWidth((Settings.indexBy(index).screen_width * 3) / 4);
        if (Settings.indexBy(index).isEqualizerReloaded && Settings.indexBy(index).presetPos != 0) {
//            correctPosition = false;
            presetSpinner.setSelection(Settings.indexBy(index).presetPos);
        }

        presetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position != 0) {
                        mEqualizer.usePreset((short) (position - 1));
                        Settings.indexBy(index).presetPos = position;
                        short numberOfFreqBands = 5;

                        final short lowerEqualizerBandLevel = mEqualizer.getBandLevelRange()[0];

                        for (short i = 0; i < numberOfFreqBands; i++) {
                            seekBarFinal[i].setProgress(mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel);
                            points[i] = mEqualizer.getBandLevel(i) - lowerEqualizerBandLevel;
                            Settings.indexBy(index).seekbarpos[i] = mEqualizer.getBandLevel(i);
                            Settings.indexBy(index).equalizerModel.getSeekbarpos()[i] = mEqualizer.getBandLevel(i);
                        }
                        dataset.updateValues(points);
                        chart.notifyDataUpdate();
                    }
                } catch (Exception e) {
                    Toast.makeText(ctx, "Error while updating Equalizer", Toast.LENGTH_SHORT).show();
                }
                Settings.indexBy(index).equalizerModel.setPresetPos(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mEqualizer != null) {
            mEqualizer.release();
        }

        if (bassBoost != null) {
            bassBoost.release();
        }

        if (presetReverb != null) {
            presetReverb.release();
        }

        Settings.indexBy(index).isEditing = false;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private int id = -1;
        private int themeColor;
        private int index;

        private String title;

        public Builder setAudioSessionId(int id) {
            this.id = id;
            return this;
        }

        public Builder setAccentColor(int color) {
            themeColor = color;
            return this;
        }

        public Builder setIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder setShowBackButton(boolean show) {
            showBackButton = show;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public EqualizerFragment build() {
            return EqualizerFragment.newInstance(title, id, themeColor, index);
        }

    }


}
