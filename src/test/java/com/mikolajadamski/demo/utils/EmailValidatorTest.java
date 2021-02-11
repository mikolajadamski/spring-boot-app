package com.mikolajadamski.demo.utils;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailValidatorTest {

    private final EmailValidator underTest = new EmailValidator();

    @Test
    void itShouldValidateCorrectEmail() {
        assertThat(underTest.test("test@gmail.com")).isTrue();
    }

    @Test
    void itShouldValidateInorrectEmail() {
        assertThat(underTest.test("testgmail.com")).isFalse();
    }

    @Test
    void itShouldValidateEmailWithoudDotAtTheEnd() {
        assertThat(underTest.test("test@gmailcom")).isFalse();
    }
}