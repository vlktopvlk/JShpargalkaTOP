package me.a3d.jshpargalkatop.ui;


import java.util.Scanner;

public class Menu {
    public static void showMainMenu() {
        Scanner scanner = new Scanner(System.in);

        boolean isRunning = true;

        while (isRunning){
            IO.println("\n=== JShpargalkaTOP: Главное меню ===");
            IO.println("\n1. Показать инструкции");
            IO.println("0. Выход");
            IO.print("\nВведите нужный номер пункта");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                IO.println("Вы выбрали первый пункт меню");
            } else {
                IO.println("Программа завершает свою работу!");
                isRunning = false;
            }

        }

        scanner.close();
    }
}
