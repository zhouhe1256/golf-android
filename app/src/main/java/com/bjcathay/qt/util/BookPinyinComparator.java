package com.bjcathay.qt.util;

import com.bjcathay.qt.model.BookModel;
import com.bjcathay.qt.model.SortModel;

import java.util.Comparator;

/**
 * Created by dengt on 15-7-3.
 */
public class BookPinyinComparator implements Comparator<BookModel> {

    public int compare(BookModel o1, BookModel o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }}
