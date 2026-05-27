import java.sql.*;

public class User {
        String name;
        String password;
        int balance;
        
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

        public int getBalance() {
            return balance;
        }

        public String getName() {
            return name;
        }

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


