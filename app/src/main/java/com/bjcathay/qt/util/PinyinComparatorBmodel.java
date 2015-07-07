package com.bjcathay.qt.util;

import com.bjcathay.qt.model.BModel;

import java.util.Comparator;

/**
 * Created by dengt on 15-7-7.
 */
public class PinyinComparatorBmodel implements Comparator<BModel> {

    public int compare(BModel o1, BModel o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
