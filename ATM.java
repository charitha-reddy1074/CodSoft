import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public boolean deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            return true;
        }
        return false;
    }

    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double getBalance() {
        return balance;
    }
}

class ATM {
    private BankAccount account;
    private JFrame frame;
    private JTextField amountField;
    private JTextArea displayArea;

    public ATM(BankAccount account) {
        this.account = account;
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("ATM");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        JButton checkBalanceButton = new JButton("Check Balance");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton exitButton = new JButton("Exit");

        amountField = new JTextField();
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        panel.add(checkBalanceButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(exitButton);

        frame.add(panel, BorderLayout.SOUTH);

        checkBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkBalance();
            }
        });

        depositButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = getAmount();
                if (amount != -1) {
                    deposit(amount);
                }
            }
        });

        withdrawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double amount = getAmount();
                if (amount != -1) {
                    withdraw(amount);
                }
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private double getAmount() {
        try {
            return Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            displayMessage("Invalid amount. Please enter a valid number.");
            return -1;
        }
    }

    private void withdraw(double amount) {
        if (account.withdraw(amount)) {
            displayMessage("Withdrawal of $" + amount + " successful.");
        } else {
            displayMessage("Error: Insufficient funds or invalid amount.");
        }
    }

    private void deposit(double amount) {
        if (account.deposit(amount)) {
            displayMessage("Deposit of $" + amount + " successful.");
        } else {
            displayMessage("Error: Invalid deposit amount.");
        }
    }

    private void checkBalance() {
        double balance = account.getBalance();
        displayMessage("Current balance: $" + balance);
    }

    private void displayMessage(String message) {
        displayArea.append(message + "\n");
    }

    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000); // Initial balance of $1000
        new ATM(account);
    }
}
