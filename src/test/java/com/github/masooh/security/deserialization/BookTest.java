package com.github.masooh.security.deserialization;

import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    private static final String BOOK_SER = "book.ser";
    private static final String BOOK_WITH_AUTHOR_SER = "book-with-author.ser";

    @BeforeAll
    static void setUp() throws IOException {
        Book book = new Book("Clean Code", 333);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(BOOK_SER));
        objectOutputStream.writeObject(book);
        objectOutputStream.close();

        book.setAuthor(new Author("Robert C. Martin"));

        objectOutputStream = new ObjectOutputStream(new FileOutputStream(BOOK_WITH_AUTHOR_SER));
        objectOutputStream.writeObject(book);
        objectOutputStream.close();
    }

    @AfterAll
    static void cleanup() throws IOException {
        new File(BOOK_SER).deleteOnExit();
        new File(BOOK_WITH_AUTHOR_SER).deleteOnExit();
    }


    @Test
    void bookCanBeDeserialized() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(BOOK_SER));
        Book book = (Book) objectInputStream.readObject();

        assertEquals("Clean Code", book.getTitle());
        assertEquals(333, book.getPages());
    }

    @Test
    void CommonsCollections4ExploitCreatesFile()  {
        File file = new File("evil.txt");
        assertFalse(file.exists());

        assertThrows(Exception.class, () -> {
            InputStream resourceAsStream = BookTest.class.getResourceAsStream("/CommonsCollections4.ser");
            ObjectInputStream objectInputStream = new ObjectInputStream(resourceAsStream);
            objectInputStream.readObject();

        });

        assertTrue(file.exists());

        file.deleteOnExit();
    }

    @Test
    void whitelistDeserializationForBook() throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new WhitelistObjectInputStream(new FileInputStream(BOOK_SER), Book.class.getName());
        Book book = (Book) objectInputStream.readObject();

        assertEquals("Clean Code", book.getTitle());
        assertEquals(333, book.getPages());
    }

    @Test
    void whitelistCommonsCollections4Exploit() {
        assertThrows(InvalidClassException.class, () -> {
            InputStream resourceAsStream = BookTest.class.getResourceAsStream("/CommonsCollections4.ser");
            ObjectInputStream objectInputStream = new WhitelistObjectInputStream(resourceAsStream, Book.class.getName());
            objectInputStream.readObject();
        });
    }

    @Test
    void whitelistDeserializationForBookWithAuthor() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(BOOK_WITH_AUTHOR_SER);
        ObjectInputStream objectInputStream =
                new WhitelistObjectInputStream(fileInputStream, Book.class.getName(), Author.class.getName());
        Book book = (Book) objectInputStream.readObject();

        assertEquals("Clean Code", book.getTitle());
        assertEquals(333, book.getPages());
        assertEquals("Robert C. Martin", book.getAuthor().getName());
    }
}