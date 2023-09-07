package com.bullhead.androidequalizer;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtil {

    public static double avg(ArrayList<Double> allVolume) {
        if (allVolume == null || allVolume.isEmpty()) {
            return 0;
        } else {
            return sum(allVolume) / allVolume.size();
        }
    }

    public static double sum(List<Double> allVolume) {
        double total = 0;
        for (Double aDouble : allVolume) {
            total += aDouble;
        }
        return total;
    }

    public static ArrayList<Double> sub(List<Double> allVolume, int num) {
        ArrayList<Double> dous = new ArrayList<>();
        if (allVolume.size() < num) {
            for (Double aDouble : allVolume) {
                dous.add(aDouble);
            }
        } else {
            int index = allVolume.size() - num;
            for (int i = 0; i < num; i++) {
                dous.add(allVolume.get(index + i));
            }
        }
        return dous;
    }
}
