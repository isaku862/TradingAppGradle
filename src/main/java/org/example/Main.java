package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("取引アプリを起動しました");

        while (true) {

            System.out.println("1 : 取引の入力");
            System.out.println("2 : 取引一覧の表示");
            System.out.println("0 : アプリの終了");

            System.out.println("メニューを選択してください : ");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                System.out.println("約定した取引の入力を開始します");

                String symbol;

                while (true) {
                    System.out.println("銘柄コードを入力してください : ");
                    symbol = scanner.nextLine();

                    if (Checks.isValidSymbol(symbol)) {
                        break;
                    }

                    System.out.println("銘柄コードが正しくありません");
                }

                //取引日時の処理
                LocalDateTime tradedDateTime;
                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                while (true) {
                    System.out.println("取引日時を入力してください（例：2026-06-05 09:06） : ");
                    String input = scanner.nextLine();

                    try {
                        tradedDateTime = LocalDateTime.parse(input, formatter);

                        if (!Checks.isTradingHours(tradedDateTime)) {
                            System.out.println("取引日時が場中ではありません");
                            continue;
                        }

                        if (!tradedDateTime.isBefore(LocalDateTime.now())) {
                            System.out.println("現在時刻より過去の日時を入力してください");
                            continue;
                        }

                        break;

                    } catch (DateTimeParseException e) {
                        System.out.println("取引日時の形式が正しくありません");
                    }
                }

                //売買区分
                String side;

                while (true) {
                    System.out.println("売買区分を入力してください（買い：B、売り：S） : ");
                    side = scanner.nextLine();

                    if (side.equals("B") || side.equals("S")) {
                        break;
                    }

                    System.out.println("売買区分はBまたはSを入力してください");
                }

                //数量
                long quantity;

                while (true) {
                    System.out.println("数量を入力してください : ");
                    String input = scanner.nextLine();

                    try {
                        quantity = Long.parseLong(input);

                        if (quantity < 1 || quantity > 999999999999L) {
                            System.out.println("数量は1以上999,999,999,999以下で入力してください");
                            continue;
                        }

                        if (quantity % 100 != 0) {
                            System.out.println("数量は100株単位で入力してください");
                            continue;
                        }

                        break;

                    } catch (NumberFormatException e) {
                        System.out.println("数量は整数で入力してください");
                    }
                }

                //単価
                double unitPrice;

                while (true) {
                    System.out.println("単価を入力してください : ");
                    String input = scanner.nextLine();

                    try {
                        unitPrice = Double.parseDouble(input);

                        if (unitPrice <= 0 || unitPrice > 999999999) {
                            System.out.println("単価は0より大きく999,999,999以下で入力してください");
                            continue;
                        }

                        break;

                    } catch (NumberFormatException e) {
                        System.out.println("単価は数値で入力してください");
                    }
                }

                //インスタンス1つが取引１件を表すクラスを作成
                Trade trade = new Trade(
                        tradedDateTime,
                        symbol,
                        side,
                        quantity,
                        unitPrice
                );
                //CSVへの書き込み
                File file = new File("traded.csv");

                try {
                    boolean newFile = !file.exists();

                    BufferedWriter writer = new BufferedWriter(
                            new FileWriter(file, true)
                    );

                    if (newFile) {
                        writer.write("traded_datetime,symbol,side,quantity,unit_price");
                        writer.newLine();
                    }

                    writer.write(
                            trade.getTradedDateTime().format(formatter)
                                    + ","
                                    + trade.getSymbol()
                                    + ","
                                    + trade.getSide()
                                    + ","
                                    + trade.getQuantity()
                                    + ","
                                    + String.format("%.1f", trade.getUnitPrice())
                    );

                    writer.newLine();
                    writer.close();

                    System.out.println("取引を登録しました");

                } catch (IOException e) {
                    System.out.println("取引記録ファイルへの書き込みに失敗しました");
                }

            } else if (choice.equals("2")) {
                System.out.println("取引一覧の表示が選択されました");
                break;
            } else if (choice.equals("0")) {
                System.out.println("アプリの終了が選択されました");
                break;
            } else {
                System.out.println("該当するメニューがありません");
            }

        }
    }
}
