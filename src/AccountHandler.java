import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

enum AccountType {
    SAVINGS,
    CURRENT
}

class Account {
    public static double rateOfInterest = 0.10;
    public final AccountType type;
    public final int accountNumber;
    public final User user;
    public LocalDateTime lastInterestAdded;
    public int months;
    private double balance;

    Account(int accountNumber, User user, double initialBalance, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.user = user;
        balance = initialBalance;
        type = accountType;
        lastInterestAdded = LocalDateTime.now();
        months = 0;
    }

    public boolean deposit(double amount) {
        checkAndAddBalance();
        if (amount <= 0) {
            System.out.println("Deposit amount must be positive");
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        checkAndAddBalance();
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive");
            return false;
        }
        if (amount > balance) {
            System.out.println("Insufficient balance");
            return false;
        }
        balance -= amount;
        return true;
    }

    public double getBalance() {
        checkAndAddBalance();
        return balance;
    }

    public void checkAndAddBalance() {
        LocalDateTime now = LocalDateTime.now();
        long monthsPassed = ChronoUnit.MONTHS.between(lastInterestAdded, now);
        if (monthsPassed > 0) {
            if (type == AccountType.SAVINGS) {
                for (int i = 0; i < monthsPassed; i++) {
                    balance += balance * rateOfInterest;
                }
            }
            lastInterestAdded = lastInterestAdded.plusMonths(monthsPassed);
        }
    }
}


class AccountHandler {
    private final Map<Integer, Account> accounts = new HashMap<>();
    private final Map<User, ArrayList<Account>> userBinding = new HashMap<>();
    private final TransactionHandler txnHandler;
    private int nextAccountNumber = 1000;

    AccountHandler(TransactionHandler txnHandler) {
        this.txnHandler = txnHandler;
    }

    public void add(User user, AccountType accountType) {
        Account account = new Account(nextAccountNumber, user, 100, accountType);
        accounts.put(nextAccountNumber, account);
        if (!userBinding.containsKey(user)) {
            userBinding.put(user, new ArrayList<>());
        }
        userBinding.get(user).add(account);
        System.out.println("Created new account. Your account number is " + nextAccountNumber);
        nextAccountNumber++;
    }

    public void deposit(int accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        boolean status = account.deposit(amount);
        if (status) {
            txnHandler.add(new Transaction(account.user, account.accountNumber, account.type, amount, TxnType.DEPOSIT));
        }
    }

    public void withdraw(int accountNumber, double amount) {
        Account account = accounts.get(accountNumber);
        boolean status = account.withdraw(amount);
        if (status) {
            txnHandler.add(new Transaction(account.user, account.accountNumber, account.type, amount, TxnType.WITHDRAWAL));
        }
    }

    public ArrayList<Account> getUserAccounts(User user) {
        return userBinding.getOrDefault(user, null);
    }
}
