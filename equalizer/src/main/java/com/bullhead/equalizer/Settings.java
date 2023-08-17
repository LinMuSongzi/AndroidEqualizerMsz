package com.bullhead.equalizer;

import java.util.ArrayList;

public class Settings {
    public  boolean        isEqualizerEnabled  = false;
    public  boolean        isEqualizerReloaded = true;
    public  int[]          seekbarpos          = new int[5];
    public  int            presetPos;
    public  short          reverbPreset        = -1;
    public  short          bassStrength        = -1;
    public  EqualizerModel equalizerModel;
    public  double         ratio               = 1.0;
    public  boolean        isEditing           = false;


    public static SettingsArray settingsArray = null;


    public static Settings indexBy(int index){
        return settingsArray.getList().get(index);
    }


}
