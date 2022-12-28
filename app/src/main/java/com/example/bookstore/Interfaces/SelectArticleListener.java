package com.example.bookstore.Interfaces;

import com.example.bookstore.Classes.Book;
import com.example.bookstore.Classes.Comic;

public interface SelectArticleListener
{
    void onBookClicked(Book book);

    void onComicClicked(Comic comic);

}
