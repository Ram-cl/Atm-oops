import java.util.Scanner;
import java.util.Random;

public class Main {

    // ===================== SCREEN CLEAR =====================
    public static void clearScreen() {
        try {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }

    // ===================== GENERATE 6-DIGIT ALPHANUMERIC OTP =====================
    public static String generateAlphaNumericOTP() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder otp = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 6; i++) {
            otp.append(chars.charAt(r.nextInt(chars.length())));
        }
        return otp.toString();
    }

    // ===================== BASE ACCOUNT CLASS =====================
    static class BankAccount {
        protected String accountType;
        protected String accountName;
        protected long accountNumber;
        protected double balance;

        public BankAccount(String type, String name, long number) {
            this.accountType = type;
            this.accountName = name;
            this.accountNumber = number;
            this.balance = 0.0;
        }

        public long getAccountNumber() { return accountNumber; }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
                System.out.println("Deposited ₹" + amount);
            } else System.out.println("Invalid deposit amount!");
        }

        public boolean withdraw(double amount) {
            if (amount <= 0) {
                System.out.println("Invalid withdrawal amount!");
                return false;
            }
            if (amount > balance) {
                System.out.println("Insufficient balance!");
                return false;
            }
            balance -= amount;
            return true;
        }

        public double getBalance() { return balance; }

        public void displayAccount() {
            System.out.println("\n----- ACCOUNT DETAILS -----");
            System.out.println("Account Type    : " + accountType);
            System.out.println("Account Name    : " + accountName);
            System.out.println("Account Number  : " + accountNumber);
            System.out.println("Balance         : ₹" + balance);
            System.out.println("---------------------------");
        }
    }

    // Savings & Current Account
    static class SavingsAccount extends BankAccount {
        public SavingsAccount(String name, long num) {
            super("Savings Account", name, num);
        }
    }
    static class CurrentAccount extends BankAccount {
        public CurrentAccount(String name, long num) {
            super("Current Account", name, num);
        }
    }

    // ===================== USER CLASS =====================
    static class User {
        private String email, username, password;
        private int age;

        private BankAccount[] accounts = new BankAccount[50];
        private int accCount = 0;

        public User(String email, String username, String password, int age) {
            this.email = email;
            this.username = username;
            this.password = password;
            this.age = age;
        }

        public boolean checkPassword(String pass) { return pass.equals(password); }
        public String getUsername() { return username; }

        public void addAccount(BankAccount acc) { accounts[accCount++] = acc; }
        public int getAccCount() { return accCount; }
        public BankAccount[] getAccounts() { return accounts; }

        public BankAccount getAccountByNumber(long num) {
            for (int j = 0; j < accCount; j++)
                if (accounts[j].getAccountNumber() == num) return accounts[j];
            return null;
        }
    }

    // ===================== AUTH SERVICE =====================
    static class AuthService {
        User[] users = new User[100];
        int userCount = 0;

        // SIGNUP PROCESS
        public void signUp(Scanner sc) {

            clearScreen();
            System.out.println("----- SIGN UP -----");

            System.out.print("Enter Age: ");
            int age = sc.nextInt(); sc.nextLine();

            if (age < 18) {
                System.out.println("Age must be 18 or above!");
                return;
            }

            // EMAIL VALIDATION
            String email = "";
            while (true) {
                System.out.print("Enter Email (lowercase + @gmail.com): ");
                email = sc.nextLine();
                if (email.matches("[a-z0-9]+@gmail\\.com")) break;
                System.out.println("Invalid Email Format!");
            }

            // ===== ALPHANUMERIC OTP VERIFICATION (3 ATTEMPTS) CASE-SENSITIVE =====
            int otpAttempts = 0;

            while (otpAttempts < 3) {

                String otp = Main.generateAlphaNumericOTP();
                System.out.println("Your OTP: " + otp);

                System.out.print("Enter OTP: ");
                String enteredOtp = sc.nextLine();  // case-sensitive

                if (otp.equals(enteredOtp)) {
                    System.out.println("OTP Verified Successfully!");
                    break;
                } else {
                    otpAttempts++;
                    System.out.println("Incorrect OTP!");
                    System.out.println("Attempts left: " + (3 - otpAttempts));

                    if (otpAttempts == 3) {
                        System.out.println("OTP attempts completed! Redirecting to main menu...");
                        return;
                    }
                }
            }

            // USERNAME VALIDATION
            String uname = "";
            int uAttempts = 0;

            while (uAttempts < 3) {
                System.out.print("Enter Username (letters & digits only): ");
                uname = sc.nextLine();

                if (uname.matches("[A-Za-z0-9]+")) break;

                uAttempts++;
                System.out.println("Invalid Username! Attempts left: " + (3 - uAttempts));
                if (uAttempts == 3) return;
            }

            // PASSWORD VALIDATION
            String pass = "", confirm = "";
            int pAttempts = 0;

            while (pAttempts < 3) {

                System.out.print("Enter Password (letters, digits, special chars): ");
                pass = sc.nextLine();

                if (!pass.matches("(?=.[A-Za-z])(?=.[0-9])(?=.*[@#$%^&+=!]).+")) {
                    pAttempts++;
                    System.out.println("Invalid Password! Attempts left: " + (3 - pAttempts));
                    if (pAttempts == 3) return;
                    continue;
                }

                System.out.print("Confirm Password: ");
                confirm = sc.nextLine();

                if (pass.equals(confirm)) break;

                pAttempts++;
                System.out.println("Passwords do not match! Attempts left: " + (3 - pAttempts));
                if (pAttempts == 3) return;
            }

            // CREATE USER
            users[userCount++] = new User(email, uname, pass, age);
            System.out.println("Signup Successful! Please login.");
        }

        // ADDING BANK ACCOUNT  
        public void createBankAccount(Scanner sc, User user) {

            clearScreen();
            System.out.println("----- ADD BANK ACCOUNT -----");

            System.out.println("1. Savings");
            System.out.println("2. Current");
            System.out.print("Choose type: ");
            int type = sc.nextInt(); sc.nextLine();

            // NICKNAME VALIDATION
            String accName = "";
            boolean nicknameOK = false;

            while (!nicknameOK) {
                System.out.print("Enter Account Nickname: ");
                accName = sc.nextLine();

                boolean nicknameExists = false;

                for (int i = 0; i < user.getAccCount(); i++) {
                    if (user.getAccounts()[i].accountName.equalsIgnoreCase(accName)) {
                        nicknameExists = true;
                        break;
                    }
                }

                if (nicknameExists) {
                    System.out.println("Nickname already exists! Enter a different nickname.");
                } else nicknameOK = true;
            }

            // ACCOUNT NUMBER VALIDATION + RECONFIRMATION
            long accNum = 0;
            boolean accountOK = false;

            while (!accountOK) {

                System.out.print("Enter Account Number: ");
                long firstEntry = sc.nextLong(); sc.nextLine();

                System.out.print("Re-enter Account Number: ");
                long secondEntry = sc.nextLong(); sc.nextLine();

                if (firstEntry != secondEntry) {
                    System.out.println("Account numbers do NOT match! Enter again.");
                    continue;
                }

                accNum = firstEntry;  // confirmed

                boolean numberExists = false;
                boolean exactMatch = false;

                for (int i = 0; i < user.getAccCount(); i++) {
                    BankAccount a = user.getAccounts()[i];

                    if (a.getAccountNumber() == accNum) {

                        if (a.accountName.equalsIgnoreCase(accName))
                            exactMatch = true; // SAME nickname + SAME number

                        numberExists = true;  // account number exists
                    }
                }

                if (exactMatch) {
                    System.out.println("This account already exists! (Same nickname + number)");
                }
                else if (numberExists) {
                    System.out.println("Account number already exists! Enter a different account number.");
                }
                else accountOK = true;
            }

            // PHONE VERIFICATION
            String phone = "";
            while (true) {
                System.out.print("Enter Phone Number (10 digits starting 6-9): ");
                phone = sc.nextLine();
                if (phone.matches("[6-9][0-9]{9}")) break;
                System.out.println("Invalid Phone Number!");
            }

            // OTP Verification (5 attempts)
            Random r = new Random();
            int attempts = 0;

            while (attempts < 5) {
                int otp = r.nextInt(9000) + 1000;
                System.out.println("Phone OTP: " + otp);

                System.out.print("Enter OTP: ");
                int entered = sc.nextInt(); sc.nextLine();

                if (entered == otp) {
                    System.out.println("OTP Verified!");
                    break;
                } else {
                    attempts++;
                    System.out.println("Wrong OTP! Attempts left: " + (5 - attempts));
                    if (attempts == 5) {
                        System.out.println("OTP Failed. Account Not Created.");
                        return;
                    }
                }
            }

            // CREATE ACCOUNT OBJECT
            BankAccount acc;
            if (type == 1) acc = new SavingsAccount(accName, accNum);
            else if (type == 2) acc = new CurrentAccount(accName, accNum);
            else {
                System.out.println("Invalid type!");
                return;
            }

            // RANDOM INITIAL BALANCE (NO PRINTING)
            Random rand = new Random();
            acc.balance = 500 + rand.nextInt(9501);

            // ADD ACCOUNT
            user.addAccount(acc);

            System.out.println("Account Created Successfully!");
        }

        // FIND GLOBAL ACCOUNT
        public BankAccount findAny(long num) {
            for (int i = 0; i < userCount; i++) {
                BankAccount acc = users[i].getAccountByNumber(num);
                if (acc != null) return acc;
            }
            return null;
        }

        // LOGIN
        public User login(Scanner sc) {
            clearScreen();
            System.out.println("----- LOGIN -----");

            if (userCount == 0) {
                System.out.println("No Users Found! Sign Up first.");
                return null;
            }

            System.out.print("Enter Username: ");
            String u = sc.nextLine();

            System.out.print("Enter Password: ");
            String p = sc.nextLine();

            for (int i = 0; i < userCount; i++) {
                if (users[i].getUsername().equals(u) &&
                    users[i].checkPassword(p)) {
                    System.out.println("Login Successful!");
                    return users[i];
                }
            }

            System.out.println("Invalid Credentials!");
            return null;
        }
    }

    // ===================== MAIN MENU =====================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        int choice;

        do {
            clearScreen();
            System.out.println("------- MAIN MENU -------");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt(); sc.nextLine();

            if (choice == 1) auth.signUp(sc);
            else if (choice == 2) {
                User u = auth.login(sc);
                if (u != null) userMenu(sc, auth, u);
            }
            else if (choice == 3) System.out.println("Goodbye!");
            else System.out.println("Invalid Choice!");

        } while (choice != 3);
    }

    // ===================== USER MENU =====================
    public static void userMenu(Scanner sc, AuthService auth, User user) {
        int ch;

        do {
            clearScreen();
            System.out.println("------ USER BANK MENU ------");
            System.out.println("1. View All Accounts");
            System.out.println("2. Select Account & Operate");
            System.out.println("3. Add New Bank Account");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            ch = sc.nextInt(); sc.nextLine();

            if ((ch == 1 || ch == 2) && user.getAccCount() == 0) {
                System.out.println("No accounts found! Add one first.");
                sc.nextLine();
                continue;
            }

            if (ch == 1) {
                for (int i = 0; i < user.getAccCount(); i++)
                    user.getAccounts()[i].displayAccount();
                sc.nextLine();
            }

            else if (ch == 2) {
                System.out.print("Enter Account Number: ");
                long num = sc.nextLong(); sc.nextLine();

                BankAccount acc = user.getAccountByNumber(num);
                if (acc == null)
                    System.out.println("Account not found!");
                else
                    accountOperations(sc, auth, acc);

                sc.nextLine();
            }

            else if (ch == 3)
                auth.createBankAccount(sc, user);

            else if (ch == 4)
                System.out.println("Logged out!");

            else
                System.out.println("Invalid choice!");

        } while (ch != 4);
    }

    // ===================== ACCOUNT OPERATIONS =====================
    public static void accountOperations(Scanner sc, AuthService auth, BankAccount acc) {
        int ch;

        do {
            clearScreen();
            System.out.println("---- ACCOUNT OPERATIONS ----");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. View Account Details");
            System.out.println("5. Net Banking (Transfer)");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");
            ch = sc.nextInt();

            if (ch == 1) {
                System.out.print("Enter amount: ");
                acc.deposit(sc.nextDouble());
            }
            else if (ch == 2) {
                System.out.print("Enter amount: ");
                double amount = sc.nextDouble();

                if (acc.withdraw(amount)) {
                    System.out.println("Amount Successfully Withdrawn!");
                    System.out.println("Remaining Balance: ₹" + acc.getBalance());
                }
            }
            else if (ch == 3) {
                System.out.println("Balance: ₹" + acc.getBalance());
            }
            else if (ch == 4) {
                acc.displayAccount();
            }
            else if (ch == 5) {
                System.out.print("Enter Receiver Account Number: ");
                long recv = sc.nextLong();

                BankAccount rcvAcc = auth.findAny(recv);

                if (rcvAcc == null)
                    System.out.println("Receiver not found!");
                else {
                    System.out.print("Enter amount: ");
                    double amt = sc.nextDouble();

                    if (acc.withdraw(amt)) {
                        rcvAcc.deposit(amt);
                        System.out.println("Transfer Successful!");
                    }
                }
            }

            else if (ch == 6) break;
            else System.out.println("Invalid choice!");

            sc.nextLine();

        } while (ch != 6);
    }
}