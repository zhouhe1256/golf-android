package com.bjcathay.qt.model;

import com.bjcathay.android.json.annotation.JSONCollection;

import java.util.List;

/**
 * Created by bjcathay on 15-5-18.
 */
public class BooksModel {

    @JSONCollection(type = BookModel.class)
    private List<BookModel> books;

    public List<BookModel> getBooks() {
        return books;
    }

    public void setBooks(List<BookModel> books) {
        this.books = books;
    }
}
