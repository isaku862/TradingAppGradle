package org.example;
import java.util.Scanner;

public class Main {

    void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("取引アプリを起動しました");

        while(true) {

            System.out.println("1 : 取引の入力");
            System.out.println("2 : 取引一覧の表示");
            System.out.println("0 : アプリの終了");

            System.out.println("メニューを選択してください : ");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                System.out.println("取引の入力が選択されました");
                break;
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
