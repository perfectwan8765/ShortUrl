package com.jsw.app.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MemberUrlRepositoryTest {

    @Autowired
    private MemberUrlRepository memberUrlRepo;

    @Test
    void injectComponentsAreNotNull() {
        assertNotNull(memberUrlRepo);
    }
    
}
