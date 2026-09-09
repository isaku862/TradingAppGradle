package org.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ChecksTest {

    @Test
    void validSymbolTest() {
        // 正常：数字4桁
        assertTrue(Checks.isValidSymbol("1234"));

        // 正常：2桁目・4桁目に使用可能な大文字
        assertTrue(Checks.isValidSymbol("1A2C"));
        assertTrue(Checks.isValidSymbol("9X8Y"));
        assertTrue(Checks.isValidSymbol("1D2W"));
    }

    @Test
    void invalidLengthTest() {
        // 4文字未満
        assertFalse(Checks.isValidSymbol("123"));
        assertFalse(Checks.isValidSymbol(""));

        // 4文字より多い
        assertFalse(Checks.isValidSymbol("12345"));
    }

    @Test
    void invalidPositionTest() {
        // 1桁目は数字でなければならない
        assertFalse(Checks.isValidSymbol("A123"));

        // 3桁目は数字でなければならない
        assertFalse(Checks.isValidSymbol("12A3"));
    }

    @Test
    void lowerCaseTest() {
        // 小文字は使用不可
        assertFalse(Checks.isValidSymbol("1a2C"));
        assertFalse(Checks.isValidSymbol("1A2c"));
    }

    @Test
    void fullWidthTest() {
        // 全角数字
        assertFalse(Checks.isValidSymbol("１A2C"));
        assertFalse(Checks.isValidSymbol("1A２C"));

        // 全角英字
        assertFalse(Checks.isValidSymbol("1Ａ2C"));
        assertFalse(Checks.isValidSymbol("1A2Ｃ"));
    }

    @Test
    void forbiddenLetterTest() {
        // B E I O Q V Z は使用不可
        assertFalse(Checks.isValidSymbol("1B2A"));
        assertFalse(Checks.isValidSymbol("1E2A"));
        assertFalse(Checks.isValidSymbol("1I2A"));
        assertFalse(Checks.isValidSymbol("1O2A"));
        assertFalse(Checks.isValidSymbol("1Q2A"));
        assertFalse(Checks.isValidSymbol("1V2A"));
        assertFalse(Checks.isValidSymbol("1Z2A"));
    }

    @Test
    void symbolAndSpaceTest() {
        // 記号
        assertFalse(Checks.isValidSymbol("1@2A"));
        assertFalse(Checks.isValidSymbol("1A2!"));

        // スペース
        assertFalse(Checks.isValidSymbol("1 2A"));
        assertFalse(Checks.isValidSymbol("1A2 "));
    }


    //場中判定テスト

    @Test
    void testMorningTradingHours() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 9, 9, 10, 0);
        assertTrue(Checks.isTradingHours(dateTime));
    }

    @Test
    void testAfternoonTradingHours() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 9, 9, 14, 0);
        assertTrue(Checks.isTradingHours(dateTime));
    }

    @Test
    void testOutsideTradingHours() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 9, 9, 12, 0);
        assertFalse(Checks.isTradingHours(dateTime));
    }

    @Test
    void testSaturday() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 9, 12, 10, 0);
        assertFalse(Checks.isTradingHours(dateTime));
    }

    @Test
    void testSunday() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 9, 13, 10, 0);
        assertFalse(Checks.isTradingHours(dateTime));
    }

    @Test
    void testBoundaryTimes() {
        LocalDateTime morningStart = LocalDateTime.of(2026, 9, 9, 9, 0);
        LocalDateTime morningEnd = LocalDateTime.of(2026, 9, 9, 11, 30);
        LocalDateTime afternoonStart = LocalDateTime.of(2026, 9, 9, 12, 30);
        LocalDateTime afternoonEnd = LocalDateTime.of(2026, 9, 9, 15, 30);

        assertTrue(Checks.isTradingHours(morningStart));
        assertTrue(Checks.isTradingHours(morningEnd));
        assertTrue(Checks.isTradingHours(afternoonStart));
        assertTrue(Checks.isTradingHours(afternoonEnd));
    }
}
