package com.example.testit.service;

import com.example.testit.adapter.mail.MailService;
import com.example.testit.model.Book;
import com.example.testit.model.Member;
import com.example.testit.model.Status;
import com.example.testit.repository.BookRepository;
import com.example.testit.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static com.example.testit.model.Status.EMPRUNTE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookServiceTest {
    BookRepository bookRepository;
    MemberRepository memberRepository;
    MailService mailService;
    BookService service;

    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        memberRepository = mock(MemberRepository.class);
        mailService = mock(MailService.class);
        service = new BookService(bookRepository, memberRepository, mailService);

        book = new Book("My Book", "John Doe");
        book.setId(1L);

        var referent = new Member("referent");
        referent.setId(100L);

        member = new Member("username");
        member.setId(10L);
        member.setReferent(referent);
    }

    @Test
    @DisplayName("Emprunte le livre car Dispo")
    public void testEmpruntLivreDispo() {
        // Given
        Optional<Book> optionalBook = Optional.of(book);
        Optional<Member> optionalMember = Optional.of(member);

        when(bookRepository.findById(1L)).thenReturn(optionalBook);
        when(memberRepository.findById(10L)).thenReturn(optionalMember);

        service.borrowBook(1L,10L);

        Mockito.verify(bookRepository).save(book);
        assertEquals(EMPRUNTE,book.getStatus());
    }

    @Test
    @DisplayName("Emprunte pas le livre car déjà emprunter")
    public void testEmpruntLivreIndispo() {
        // Given
        book.setStatus(EMPRUNTE);

        Optional<Book> optionalBook = Optional.of(book);
        Optional<Member> optionalMember = Optional.of(member);

        when(bookRepository.findById(1L)).thenReturn(optionalBook);
        when(memberRepository.findById(10L)).thenReturn(optionalMember);

        //When
        assertThrows(IllegalStateException.class, () -> service.borrowBook(1L,10L));

        //Then
        Mockito.verify(bookRepository,never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Livre inexistant")
    public void TestLivreInexistant() {
        // Given

        Optional<Member> optionalMember = Optional.of(member);
        when(memberRepository.findById(10L)).thenReturn(optionalMember);

        //When
        assertThrows(IllegalArgumentException.class, () -> service.borrowBook(2L,10L));

        //Then
        Mockito.verify(memberRepository,never()).save(any(Member.class));
    }

    @Test
    @DisplayName("Membre inexistant")
    public void TestMembreInexistant() {
        // Given

        Optional<Book> optionalBook = Optional.of(book);
        when(bookRepository.findById(1L)).thenReturn(optionalBook);

        //When
        assertThrows(IllegalArgumentException.class, () -> service.borrowBook(1L,20L));

        //Then
        Mockito.verify(bookRepository,never()).save(any(Book.class));
    }
}