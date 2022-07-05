package com.stock.bookmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.bookmanager.model.Book;
import com.stock.bookmanager.model.Genre;
import com.stock.bookmanager.service.BookManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest
public class BookManagerControllerTests {

    @Mock
    private BookManagerServiceImpl mockBookManagerServiceImpl;

    @InjectMocks
    private BookManagerController bookManagerController;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        mockMvcController = MockMvcBuilders.standaloneSetup(bookManagerController).build();
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetAllBooksReturnsBooks() throws Exception {

        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book One", "This is the description for Book One", "Person One", Genre.Education));
        books.add(new Book(2L, "Book Two", "This is the description for Book Two", "Person Two", Genre.Education));
        books.add(new Book(3L, "Book Three", "This is the description for Book Three", "Person Three", Genre.Education));

        when(mockBookManagerServiceImpl.getAllBooks()).thenReturn(books);

        this.mockMvcController.perform(
            MockMvcRequestBuilders.get("/api/v1/book/"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Book One"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Book Two"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3))
            .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("Book Three"));
    }

    @Test
    public void testGetMappingGetBookById() throws Exception {

        Book book = new Book(4L, "Book Four", "This is the description for Book Four", "Person Four", Genre.Fantasy);

        when(mockBookManagerServiceImpl.getBookById(book.getId())).thenReturn(book);

        this.mockMvcController.perform(
            MockMvcRequestBuilders.get("/api/v1/book/" + book.getId()))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(4))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book Four"));
    }

    @Test
    public void testPostMappingAddABook() throws Exception {

        Book book = new Book(4L, "Book Four", "This is the description for Book Four", "Person Four", Genre.Fantasy);

        when(mockBookManagerServiceImpl.insertBook(book)).thenReturn(book);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/v1/book/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(mockBookManagerServiceImpl, times(1)).insertBook(book);
    }

    //User Story 4 - Update Book By Id Solution
    @Test
    public void testPutMappingUpdateABook() throws Exception {

        Book book = new Book(4L, "Fabulous Four", "This is the description for the Fabulous Four", "Person Four", Genre.Fantasy);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.put("/api/v1/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mockBookManagerServiceImpl, times(1)).updateBookById(book.getId(), book);
    }

    //User Story 5 - Delete Book By Id Solution
    @Test
    public void testDeleteMappingDeleteBookById() throws Exception {

        Book book = new Book(4L, "Book Four", "This is the description for Book Four", "Person Four", Genre.Fantasy);

        when(mockBookManagerServiceImpl.getBookById(book.getId())).thenReturn(book);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.delete("/api/v1/book/" + book.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mockBookManagerServiceImpl, times(1)).deleteBookById(book.getId());
    }

}
