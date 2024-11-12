import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static Pair<Boolean, Integer> getInt(Scanner sc) {
        boolean status = false;
        int input = 0;
        try {
            input = sc.nextInt();
            sc.nextLine();
            status = true;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            sc.nextLine();
        }
        return new Pair<>(status, input);
    }

    static Pair<Boolean, String> getLine(Scanner sc) {
        boolean status = false;
        String input = "";
        try {
            input = sc.nextLine();
            status = true;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            sc.nextLine();
        }
        return new Pair<>(status, input);
    }

    static Pair<Boolean, Double> getDouble(Scanner sc) {
        boolean status = false;
        double input = 0;
        try {
            input = sc.nextInt();
            sc.nextLine();
            status = true;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input");
            sc.nextLine();
        }
        return new Pair<>(status, input);
    }

    public static void main(String[] args) {
        Scanner sysScanner = new Scanner(System.in);
        Menu menu = new Menu();
        UserHandler userHandler = new UserHandler();
        TransactionHandler transactionHandler = new TransactionHandler();
        AccountHandler accountHandler = new AccountHandler(transactionHandler);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[EEE] yyyy-MM-dd HH:mm:ss");

        boolean run = true;
        User user = null;
        Account account = null;

        while (run) {
            if (user == null) {
                System.out.println("Logged in as: None (Guest)");
            } else {
                System.out.println("Logged in as: " + user.name);
            }

            int totalEntries = menu.show();
            Pair<Boolean, Integer> chosenEntryPair = Main.getInt(sysScanner);
            if (!chosenEntryPair.getFirst()) continue;
            int chosenEntry = chosenEntryPair.getSecond() - 1;

            if (chosenEntry < 0 || chosenEntry > totalEntries) {
                System.out.println("Invalid option");
                continue;
            }
            if (chosenEntry == totalEntries) {
                boolean toExit = menu.back();
                run = !toExit;
                continue;
            }

            Enum<?> chosenAction = (Enum<?>) menu.currentMenu().get(chosenEntry).getFirst();

            if (chosenAction instanceof Menu.HomeMenu) {
                switch ((Menu.HomeMenu) chosenAction) {
                    case LOGIN -> {
                        System.out.print("Username: ");
                        Pair<Boolean, String> unamePair = Main.getLine(sysScanner);
                        if (!unamePair.getFirst()) continue;
                        String uname = unamePair.getSecond();
                        System.out.print("Password: ");
                        Pair<Boolean, String> passwdPair = Main.getLine(sysScanner);
                        if (!passwdPair.getFirst()) continue;
                        String passwd = passwdPair.getSecond();
                        boolean status = userHandler.login(uname, passwd);
                        if (status) {
                            user = userHandler.get(uname);
                            menu.next();
                        }
                    }

                    case REGISTER -> {
                        System.out.print("Username: ");
                        Pair<Boolean, String> unamePair = Main.getLine(sysScanner);
                        if (!unamePair.getFirst()) continue;
                        String uname = unamePair.getSecond();
                        System.out.print("Password: ");
                        Pair<Boolean, String> passwdPair = Main.getLine(sysScanner);
                        if (!passwdPair.getFirst()) continue;
                        String passwd = passwdPair.getSecond();
                        userHandler.register(uname, passwd);
                    }

                    default -> System.out.println("Invalid action");
                }
            } else if (chosenAction instanceof Menu.MainMenu) {
                switch ((Menu.MainMenu) chosenAction) {
                    case ADD -> {
                        System.out.println("Account type:\n\t1. Savings\n\t2.Current");
                        Pair<Boolean, Integer> typePair = Main.getInt(sysScanner);
                        if (!typePair.getFirst()) continue;
                        int type = typePair.getSecond();
                        AccountType accType;
                        if (type == 1) {
                            accType = AccountType.SAVINGS;
                        } else if (type == 2) {
                            accType = AccountType.CURRENT;
                        } else {
                            System.out.println("Invalid account type");
                            continue;
                        }
                        accountHandler.add(user, accType);
                    }

                    case CHOOSE -> {
                        ArrayList<Account> accounts = accountHandler.getUserAccounts(user);
                        if (accounts == null) {
                            System.out.println("User has no accounts");
                            continue;
                        } else {
                            for (int i = 0; i < accounts.size(); i++) {
                                System.out.println((i + 1) + ". " + accounts.get(i).accountNumber);
                            }
                        }
                        Pair<Boolean, Integer> chosenAccountPair = Main.getInt(sysScanner);
                        if (!chosenAccountPair.getFirst()) continue;
                        int chosenAccount = chosenAccountPair.getSecond() - 1;
                        if (chosenAccount >= accounts.size() || chosenAccount < 0) {
                            System.out.println("Chosen account is invalid");
                            continue;
                        }
                        account = accounts.get(chosenAccount);
                        menu.next();
                    }

                    default -> System.out.println("Invalid action");
                }
            } else if (chosenAction instanceof Menu.AccountMenu) {
                switch ((Menu.AccountMenu) chosenAction) {
                    case DEPOSIT -> {
                        System.out.print("Amount: ");
                        Pair<Boolean, Double> amountPair = Main.getDouble(sysScanner);
                        if (!amountPair.getFirst()) continue;
                        double amount = amountPair.getSecond();
                        accountHandler.deposit(account.accountNumber, amount);
                    }

                    case WITHDRAW -> {
                        System.out.print("Amount: ");
                        Pair<Boolean, Double> amountPair = Main.getDouble(sysScanner);
                        if (!amountPair.getFirst()) continue;
                        double amount = amountPair.getSecond();
                        accountHandler.withdraw(account.accountNumber, amount);
                    }

                    case BALANCE -> {
                        System.out.println("Current balance: " + account.getBalance());
                    }

                    case STATEMENT -> {
                        System.out.println("---- Statement Starts ----");
                        ArrayList<Transaction> transactions = transactionHandler.get(account.accountNumber);
                        if (transactions != null) {
                            for (Transaction transaction : transactions) {
                                System.out.printf("[%s] %s %d [%s] %s %.2f [%s]%n", transaction.transactionId, transaction.time.format(formatter), transaction.accountNumber, transaction.accountType == AccountType.SAVINGS ? "Savings" : "Current", transaction.user.name, transaction.amount, transaction.txnType == TxnType.DEPOSIT ? "Deposit" : "Withdraw");
                            }
                        }
                        System.out.println("---- Statement Ends ----");
                    }

                    default -> System.out.println("Invalid action");
                }
            } else {
                System.out.println("Invalid option");
            }
        }
        sysScanner.close();
    }
}