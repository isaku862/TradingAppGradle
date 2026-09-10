package org.example;

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

public class InputTradedInfo {
    static void inputTrade(Scanner scanner) {
        System.out.println("約定した取引の入力を開始します");

        String symbol = inputSymbol(scanner);
        LocalDateTime tradedDateTime = inputTradedDateTime(scanner);
        String side = inputSide(scanner);
        long quantity = inputQuantity(scanner);
        double unitPrice = inputUnitPrice(scanner);

        Trade trade = new Trade(
                tradedDateTime,
                symbol,
                side,
                quantity,
                unitPrice
        );

        writeCsv(trade);
    }

    static String inputSymbol(Scanner scanner) {
        while (true) {
            System.out.println("銘柄コードを入力してください : ");
            String symbol = scanner.nextLine();

            if (Checks.isValidSymbol(symbol)) {
                return symbol;
            }

            System.out.println("銘柄コードが正しくありません");
        }
    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);

    static LocalDateTime inputTradedDateTime(Scanner scanner) {
        while (true) {
            System.out.println(
                    "取引日時を入力してください（例：2026-06-05 09:06） : "
            );

            String input = scanner.nextLine();

            try {
                LocalDateTime tradedDateTime =
                        LocalDateTime.parse(input, FORMATTER);

                if (!Checks.isTradingHours(tradedDateTime)) {
                    System.out.println("取引日時が場中ではありません");
                    continue;
                }

                if (!tradedDateTime.isBefore(LocalDateTime.now())) {
                    System.out.println(
                            "現在時刻より過去の日時を入力してください"
                    );
                    continue;
                }

                return tradedDateTime;

            } catch (DateTimeParseException e) {
                System.out.println(
                        "取引日時の形式が正しくありません"
                );
            }
        }
    }

    static String inputSide(Scanner scanner) {
        while (true) {
            System.out.println(
                    "売買区分を入力してください（買い：B、売り：S） : "
            );

            String side = scanner.nextLine();

            if (Objects.equals(side, "B") || Objects.equals(side, "S")) {
                return side;
            }

            System.out.println(
                    "売買区分はBまたはSを入力してください"
            );
        }
    }

    static long inputQuantity(Scanner scanner) {
        while (true) {
            System.out.println("数量を入力してください : ");
            String input = scanner.nextLine();

            try {
                long quantity = Long.parseLong(input);

                if (quantity < 1 || quantity > 999999999999L) {
                    System.out.println(
                            "数量は1以上999,999,999,999以下で入力してください"
                    );
                    continue;
                }

                if (quantity % 100 != 0) {
                    System.out.println(
                            "数量は100株単位で入力してください"
                    );
                    continue;
                }

                return quantity;

            } catch (NumberFormatException e) {
                System.out.println(
                        "数量は整数で入力してください"
                );
            }
        }
    }

    static double inputUnitPrice(Scanner scanner) {
        while (true) {
            System.out.println("単価を入力してください : ");
            String input = scanner.nextLine();

            try {
                BigDecimal unitPrice = new BigDecimal(input);

                if (unitPrice.scale() > 1) {

                    System.out.println(
                            "単価は小数点以下1桁までで入力してください"
                    );
                    continue;
                }

                if (unitPrice.compareTo(BigDecimal.ZERO) <= 0
                        || unitPrice.compareTo(
                        new BigDecimal("999999999")
                ) > 0) {
                    System.out.println(
                            "単価は0より大きく999,999,999以下で入力してください"
                    );

                    continue;

                }

                return unitPrice.doubleValue();

            } catch (NumberFormatException e) {
                System.out.println(
                        "単価は数値で入力してください"
                );
            }
        }
    }

    static void writeCsv(Trade trade) {
        File file = new File("traded.csv");
        boolean newFile = !file.exists();

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file, true))) {

            if (newFile) {
                writer.write(
                        "traded_datetime,symbol,side,quantity,unit_price"
                );
                writer.newLine();
            }

            String unitPrice = String.format(
                    Locale.ROOT,
                    "%.1f",
                    trade.getUnitPrice()
            );

            writer.write(
                    trade.getTradedDateTime().format(FORMATTER)
                            + ","
                            + trade.getSymbol()
                            + ","
                            + trade.getSide()
                            + ","
                            + trade.getQuantity()
                            + ","
                            + unitPrice
            );

            writer.newLine();

            System.out.println("取引を登録しました");

        } catch (IOException e) {
            System.out.println(
                    "取引記録ファイルへの書き込みに失敗しました"
            );
        }
    }
}
