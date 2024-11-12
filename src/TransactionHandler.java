import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

enum TxnType {
    WITHDRAWAL,
    DEPOSIT
}

class Transaction {
    public final String transactionId;
    public final LocalDateTime time;
    public final AccountType accountType;
    public final int accountNumber;
    public final double amount;
    public final User user;
    public final TxnType txnType;

    public Transaction(User user, int accountNumber, AccountType accountType, double amount, TxnType txnType) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.amount = amount;
        this.txnType = txnType;
        this.time = LocalDateTime.now();
        this.transactionId = UUID.randomUUID().toString();
    }
}

class TransactionHandler {
    private final Map<Integer, ArrayList<Transaction>> transactions = new HashMap<>();

    public void add(Transaction txn) {
        if (!transactions.containsKey(txn.accountNumber)) {
            transactions.put(txn.accountNumber, new ArrayList<>());
        }
        transactions.get(txn.accountNumber).add(txn);
    }

    public ArrayList<Transaction> get(int accountNumber) {
        return transactions.getOrDefault(accountNumber, null);
    }
}
