package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class DisplayTrades {

    private static final String FILE_NAME = "traded.csv";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm").withResolverStyle(ResolverStyle.STRICT);

    public static void displayTrades(Scanner scanner) {
        System.out.println("どんな取引にしますか？");
        String condition = scanner.nextLine();

        List<Trade> trades = readTrades();

        if (trades == null) {
            return;
        }

        List<Trade> filteredTrades =
                filterTrades(trades, condition);

        sortTrades(filteredTrades);

        if (filteredTrades.isEmpty()) {
            System.out.println("該当する取引がありません");
            return;
        }

        printTable(filteredTrades);
    }

    private static List<Trade> readTrades() {
        List<Trade> trades = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(FILE_NAME))) {

            // 1行目のヘッダーを読み飛ばす
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                Trade trade = createTrade(line);

                if (trade != null) {
                    trades.add(trade);
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "取引記録ファイルの読み込みに失敗しました"
            );
            return null;
        }

        return trades;
    }

    private static Trade createTrade(String line) {
        String[] data = line.split(",");

        if (data.length != 5) {
            return null;
        }

        try {
            LocalDateTime tradedDateTime =
                    LocalDateTime.parse(data[0], FORMATTER);

            String symbol = data[1];
            String side = data[2];

            long quantity =
                    Long.parseLong(data[3]);

            BigDecimal price =
                    new BigDecimal(data[4]);

            if (price.scale() > 1) {
                return null;
            }

            if (!Objects.equals(side, "B")
                    && !Objects.equals(side, "S")) {
                return null;
            }

            double unitPrice = price.doubleValue();

            return new Trade(
                    tradedDateTime,
                    symbol,
                    side,
                    quantity,
                    unitPrice
            );

        } catch (DateTimeParseException
                 | NumberFormatException e) {
            return null;
        }
    }

    private static List<Trade> filterTrades(
            List<Trade> trades,
            String condition) {

        List<Trade> filteredTrades = new ArrayList<>();

        for (Trade trade : trades) {
            if (matchesCondition(trade, condition)) {
                filteredTrades.add(trade);
            }
        }

        return filteredTrades;
    }

    private static boolean matchesCondition(
            Trade trade,
            String condition) {

        if (condition.isEmpty()
                || Objects.equals(condition, "all")) {
            return true;
        }

        if (Objects.equals(condition, "today")) {
            LocalDate today = LocalDate.now();

            return trade.getTradedDateTime()
                    .toLocalDate()
                    .equals(today);
        }

        if (Objects.equals(condition, "yesterday")) {
            LocalDate yesterday =
                    LocalDate.now().minusDays(1);

            return trade.getTradedDateTime()
                    .toLocalDate()
                    .equals(yesterday);
        }

        if (Checks.isValidSymbol(condition)) {
            return Objects.equals(
                    trade.getSymbol(),
                    condition
            );
        }

        return false;
    }

    private static void sortTrades(List<Trade> trades) {
        trades.sort(
                Comparator.comparing(
                        Trade::getTradedDateTime
                ).reversed()
        );
    }

    private static void printTable(List<Trade> trades) {
        printDoubleLine();

        System.out.printf(
                "| %-17s | %-6s | %-4s | %10s | %10s | %13s |%n",
                "Traded DateTime",
                "Symbol",
                "Side",
                "Quantity",
                "Unit Price",
                "Traded Value"
        );

        printSingleLine();

        for (Trade trade : trades) {
            printTrade(trade);
        }

        printDoubleLine();
    }

    private static void printTrade(Trade trade) {
        String sideName;

        if (Objects.equals(trade.getSide(), "B")) {
            sideName = "Buy";
        } else {
            sideName = "Sell";
        }

        double tradedValue =
                trade.getQuantity()
                        * trade.getUnitPrice();

        System.out.printf(
                Locale.ROOT,
                "| %-17s | %-6s | %-4s | %,10d | %,10.1f | %,13.1f |%n",
                trade.getTradedDateTime().format(FORMATTER),
                trade.getSymbol(),
                sideName,
                trade.getQuantity(),
                trade.getUnitPrice(),
                tradedValue
        );
    }

    private static void printDoubleLine() {
        System.out.println(
                "=============================================================================="
        );
    }

    private static void printSingleLine() {
        System.out.println(
                "+-------------------+--------+------+------------+------------+---------------+"
        );
    }

}