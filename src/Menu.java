import java.util.ArrayList;
import java.util.Map;

class Pair<T, U> {
    T first;
    U second;

    Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}

class Menu {
    ArrayList<Pair<HomeMenu, String>> homeDisplay = new ArrayList<>();
    ArrayList<Pair<MainMenu, String>> mainDisplay = new ArrayList<>();
    ArrayList<Pair<AccountMenu, String>> accountDisplay = new ArrayList<>();
    ArrayList<ArrayList<? extends Pair<?, String>>> allMenus = new ArrayList<>();
    private int current = 0;
    Menu() {
        homeDisplay.add(new Pair<>(HomeMenu.REGISTER, "Register"));
        homeDisplay.add(new Pair<>(HomeMenu.LOGIN, "Login"));

        mainDisplay.add(new Pair<>(MainMenu.ADD, "Add account"));
        mainDisplay.add(new Pair<>(MainMenu.CHOOSE, "Choose account"));

        accountDisplay.add(new Pair<>(AccountMenu.DEPOSIT, "Deposit money"));
        accountDisplay.add(new Pair<>(AccountMenu.WITHDRAW, "Withdraw money"));
        accountDisplay.add(new Pair<>(AccountMenu.BALANCE, "Check balance"));
        accountDisplay.add(new Pair<>(AccountMenu.STATEMENT, "Get account statement"));

        allMenus.add(homeDisplay);
        allMenus.add(mainDisplay);
        allMenus.add(accountDisplay);
    }

    public int show() {
        ArrayList<? extends Pair<?, String>> menuDisplay = allMenus.get(current);
        int i = 0;
        for (; i < menuDisplay.size(); i++) {
            System.out.println((i + 1) + ": " + menuDisplay.get(i).getSecond());
        }
        if (current == 0) {
            System.out.println((i + 1) + ": Exit");
        } else {
            System.out.println((i + 1) + ": Back");
        }
        return i;
    }

    public void next() {
        if (current + 1 < allMenus.size())
            current++;
    }

    public boolean back() {
        if (current > 0) {
            current--;
            return false;
        }
        return true;
    }

    public ArrayList<? extends Pair<?, String>> currentMenu() {
        return allMenus.get(current);
    }

    public Enum<?> currentEnum() {
        return (Enum<?>) allMenus.get(current).getFirst().getFirst();
    }

    enum HomeMenu {
        REGISTER,
        LOGIN
    }

    enum MainMenu {
        ADD,
        CHOOSE
    }

    enum AccountMenu {
        DEPOSIT,
        WITHDRAW,
        STATEMENT,
        BALANCE
    }
}
