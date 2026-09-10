package org.example;

import org.example.InputTradedInfo;
import java.math.BigDecimal;
import java.time.format.ResolverStyle;
import java.util.Objects;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

import static org.example.InputTradedInfo.inputTrade;

public class Main {

    void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("取引アプリを起動しました");

        while (true) {
            showMenu();

            String choice = scanner.nextLine();

            if (Objects.equals(choice, "1")) {
                inputTrade(scanner);
            } else if (Objects.equals(choice, "2")) {
                DisplayTrades.displayTrades(scanner);
            } else if (Objects.equals(choice, "0")) {
                System.out.println("アプリの終了が選択されました");
                break;
            } else {
                System.out.println("該当するメニューがありません");
            }
        }
    }

    static void showMenu() {
        System.out.println("1 : 取引の入力");
        System.out.println("2 : 取引一覧の表示");
        System.out.println("0 : アプリの終了");
        System.out.println("メニューを選択してください : ");
    }


}