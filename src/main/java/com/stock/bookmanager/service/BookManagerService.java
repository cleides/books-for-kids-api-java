package com.stock.bookmanager.service;

import com.stock.bookmanager.model.Book;

import java.util.List;

public interface BookManagerService {

    List<Book> getAllBooks();
    Book insertBook(Book book);
    Book getBookById(Long id);

    //User Story 4 - Update Book By Id Solution
    void updateBookById(Long id, Book book);

    //User Story 5 - Delete Book By Id Solution
    void deleteBookById(Long id);
}
