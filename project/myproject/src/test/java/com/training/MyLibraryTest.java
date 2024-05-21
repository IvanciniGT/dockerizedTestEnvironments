package com.training;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class MyLibraryTest {
    
    @Test
    @DisplayName("Sum 2 positive numbers")
    void sum2PositiveNumbersTest() {
        // GIVEN
        double number1 = 5;
        double number2 = 7;
        double expectedResult= 12;
        // WHEN
        double actualResult =  MyLibrary.sum(number1, number2);
        // THEN
        Assertions.assertEquals(expectedResult, actualResult, 0.00001);
    }
    
    @Test
    @DisplayName("Sum 2 negative numbers")
    void sum2NegativeNumbersTest() {
        // GIVEN
        double number1 = -5;
        double number2 = -7;
        double expectedResult= -12;
        // WHEN
        double actualResult =  MyLibrary.sum(number1, number2);
        // THEN
        Assertions.assertEquals(expectedResult, actualResult, 0.00001);
    }
    
    @Test
    @DisplayName("Sum positive number and zero")
    void sumZeroTest() {
        // GIVEN
        double number1 = 5;
        double number2 = 0;
        double expectedResult= 5;
        // WHEN
        double actualResult =  MyLibrary.sum(number1, number2);
        // THEN
        Assertions.assertEquals(expectedResult, actualResult, 0.00001);
    }
    
    @Test
    @DisplayName("Double positive number ")
    void doublePositiveNumberTest() {
        // GIVEN
        double number = 5;
        double expectedResult= 10;
        // WHEN
        double actualResult =  MyLibrary.doubleNumber(number);
        // THEN
        Assertions.assertEquals(expectedResult, actualResult, 0.00001);
    }
    
    @Test
    @DisplayName("Double zero ")
    void doubleZeroTest() {
        // GIVEN
        double number = 0;
        double expectedResult= 0;
        // WHEN
        double actualResult =  MyLibrary.doubleNumber(number);
        // THEN
        Assertions.assertEquals(expectedResult, actualResult, 0.00001);
    }
    
}