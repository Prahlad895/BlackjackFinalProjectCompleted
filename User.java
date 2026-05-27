import java.sql.*;

/**
 * Represents a user of the Blackjack application.
 * Handles authentication, balance management, and user creation in the database.
 */
public class User {
        String name;
        String password;
        int balance;
        
        /**
         * Validates the user's credentials against the database.
         *
         * @param name The username to validate.
         * @param password The password to validate.
         * @return true if credentials are valid, false otherwise.
         * @precondition Database is accessible and users table exists.
         * @postcondition User fields are populated if credentials are valid.
         */
        public boolean validateUser(String name, String password){
            final String DB_URL = "jdbc:mysql://localhost:3306/blackjack";
            final String USERNAME = "root";
            final String PASSWORD = "";
    
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
    
                if(resultSet.next()){
                    this.name = resultSet.getString("name");
                    this.password = resultSet.getString("password");
                    this.balance = resultSet.getInt("Balance");
                } else {
                    resultSet.close();
                    preparedStatement.close();
                    conn.close();
                    return false;
                }
                resultSet.close();
                preparedStatement.close();
                conn.close();
            }catch(Exception e){
                System.out.println("Database connection error: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
            return true;
        }

        /**
         * Gets the user's current balance.
         *
         * @return The user's balance.
         * @precondition User object is initialized.
         * @postcondition No change to user state.
         */
        public int getBalance() {
            return balance;
        }

        /**
         * Gets the user's name.
         *
         * @return The user's name.
         * @precondition User object is initialized.
         * @postcondition No change to user state.
         */
        public String getName() {
            return name;
        }

        /**
         * Updates the user's balance in the database.
         *
         * @param newBalance The new balance to set.
         * @return true if update is successful, false otherwise.
         * @precondition Database is accessible and user is authenticated.
         * @postcondition User's balance is updated in the database and in the object.
         */
        public boolean updateBalance(int newBalance) {
            final String DB_URL = "jdbc:mysql://localhost:3306/blackjack";
            final String USERNAME = "root";
            final String PASSWORD = "";
    
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                String sql = "UPDATE users SET Balance = ? WHERE name = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, newBalance);
                preparedStatement.setString(2, name);
                preparedStatement.executeUpdate();
                this.balance = newBalance;
                preparedStatement.close();
                conn.close();
            }catch(Exception e){
                System.out.println("Database update failed: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
            return true;
        }

        /**
         * Creates a new user in the database with the given name and password.
         *
         * @param name The username for the new user.
         * @param password The password for the new user.
         * @return true if user is created successfully, false otherwise.
         * @precondition Database is accessible and users table exists.
         * @postcondition New user is added to the database with a starting balance.
         */
        public boolean createUser(String name, String password){
            final String DB_URL = "jdbc:mysql://localhost:3306/blackjack";
            final String USERNAME = "root";
            final String PASSWORD = "";
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                String sql = "INSERT INTO users (name, password, Balance) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, password);
                preparedStatement.setInt(3, 1000);
                preparedStatement.executeUpdate();
                preparedStatement.close();
                conn.close();
            }catch(Exception e){
                System.out.println("User Creation Failed: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
            LoginForm loginForm = new LoginForm();
            loginForm.initialize();
            return true;
        }
}


