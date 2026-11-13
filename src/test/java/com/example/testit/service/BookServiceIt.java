package com.example.testit.service;

import com.example.testit.model.Member;
import com.example.testit.model.Status;
import com.example.testit.repository.BookRepository;
import com.example.testit.repository.MemberRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookServiceIt {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Emprunter puis rendre un livre, et relire le résultat")
    void emprunterPuisRendreUnLivre() {
        //given
        var membre = memberRepository.save(new Member());
        var livre = bookService.createBook("livre1", "auteur1");

        //when
        bookService.borrowBook(livre.getId(), membre.getId());
        var livreEmprunte = bookRepository.findById(livre.getId()).orElseThrow();

        bookService.returnBook(livre.getId(), membre.getId());
        var livreRendu = bookRepository.findById(livre.getId()).orElseThrow();

        //then
        assertThat(livreEmprunte.getStatus()).isEqualTo(Status.EMPRUNTE);
        assertThat(livreRendu.getStatus()).isEqualTo(Status.RENDU);
    }
}