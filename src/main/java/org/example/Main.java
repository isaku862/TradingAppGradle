package org.example;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("取引アプリを起動しました");

        while (true) {
            showMenu();

            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                inputTrade(scanner);
            } else if (choice.equals("2")) {
                System.out.println("取引一覧の表示が選択されました");
            } else if (choice.equals("0")) {
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

            if (side.equals("B") || side.equals("S")) {
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
                double unitPrice = Double.parseDouble(input);

                if (!Double.isFinite(unitPrice)
                        || unitPrice <= 0
                        || unitPrice > 999999999) {
                    System.out.println(
                            "単価は0より大きく999,999,999以下で入力してください"
                    );
                    continue;
                }

                return unitPrice;

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