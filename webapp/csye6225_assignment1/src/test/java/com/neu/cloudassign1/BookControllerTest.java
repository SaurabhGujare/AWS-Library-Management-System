package com.neu.cloudassign1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;


import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.UUID;
import com.neu.cloudassign1.dao.BookDAO;
import com.neu.cloudassign1.controller.BookController;
import com.neu.cloudassign1.model.Book;



@RunWith(SpringRunner.class)
@WebMvcTest(value = BookController.class, secure = false)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookDAO bookDAO;

    @Test
    public void testCreateBook() throws Exception{

        Book mockBook = new Book();
        mockBook.setId(UUID.randomUUID());
        mockBook.setAuthor("author1");
        mockBook.setTitle("book1");

        String inputInJson = this.mapToJson(mockBook);

        String URI = "/book";

//        Mockito.when(bookDAO.saveBook(Mockito.any(Book.class)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URI)
                .accept(MediaType.APPLICATION_JSON).content(inputInJson)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String outputInJson = response.getContentAsString();

        Map<String,String> bookMap = new HashMap<>();
        bookMap.put("successMessage","Book registered successfully");


        assertThat(outputInJson).isEqualTo(new JSONObject(bookMap).toString());
        assertEquals(HttpStatus.OK.value(), response.getStatus());


    }

    @Test
    public void testShowBooks() throws Exception{

        Book mockBook1 = new Book();
        mockBook1.setId(UUID.randomUUID());
        mockBook1.setAuthor("author1");
        mockBook1.setTitle("book1");
        mockBook1.setIsbn("isbn1");
        mockBook1.setQuantity(100);


        Book mockBook2 = new Book();
        mockBook2.setId(UUID.randomUUID());
        mockBook2.setAuthor("author2");
        mockBook2.setTitle("book2");
        mockBook2.setIsbn("isbn2");
        mockBook2.setQuantity(200);

        List<Book> bookList = new ArrayList<Book>();
        bookList.add(mockBook1);
        bookList.add(mockBook2);

        Mockito.when(bookDAO.showBooks()).thenReturn(bookList);

        String URI = "/book";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .accept(MediaType.APPLICATION_JSON) ;

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expectedJson = this.mapToJson(bookList);
        String outputInJson = result.getResponse().getContentAsString();
        assertThat(outputInJson).isEqualTo(expectedJson);

    }
//
//
//    @Test
//    public void testShowBook() throws Exception{
//
////        UUID uid = UUID.randomUUID();
//        Book mockBook = new Book();
//        mockBook.setId(abc);
//        mockBook.setAuthor("author1");
//        mockBook.setTitle("book1");
//
//        Mockito.when(bookDAO.getBookById(Mockito.anyString())).thenReturn(mockBook);
//
//        String URI = "/book/abc";
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
//                URI).accept(
//                MediaType.APPLICATION_JSON);
//
//        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
//        String expectedJson = this.mapToJson(mockBook);
//        String outputInJson = result.getResponse().getContentAsString();
//        assertThat(outputInJson).isEqualTo(expectedJson);
//
//    }





    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
