package com.example.testit.repository;


import com.example.testit.model.Book;
import com.example.testit.model.Member;
import com.example.testit.model.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookRepositoryIT {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("findByMemberAndStatus avec deux membres et 2 livres")
    void testFindByMemberAndStatus() {
        //given
        var membre1 = memberRepository.save(new Member());
        var membre2 = memberRepository.save(new Member());

        var livre1 = new Book();
        livre1.setTitle("livre1");
        livre1.setAuthor("auteur1");
        livre1.setStatus(Status.EMPRUNTE);
        livre1.setBorrower(membre1);
        livre1 = bookRepository.save(livre1);

        var livre2 = new Book();
        livre2.setTitle("livre2");
        livre2.setAuthor("auteur2");
        livre2.setStatus(Status.EMPRUNTE);
        livre2.setBorrower(membre2);
        livre2 = bookRepository.save(livre2);

        //when
        var livres = bookRepository.findByMemberAndStatus(membre1.getId(), Status.EMPRUNTE);

        //then
        assertThat(livres).extracting(Book::getId).contains(livre1.getId()).doesNotContain(livre2.getId());
    }
}