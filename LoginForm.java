import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
// Add this import for MainFrame class
import javax.swing.border.Border;

/**
 * Login form for the Blackjack application.
 * Handles user input for login and navigation to account creation or game start.
 */
public class LoginForm extends JFrame{
    final private Font mainFont = new Font("Inter", Font.PLAIN, 18);
    JTextField tfName;
    JPasswordField pfPassword;
    static User user1;

    /**
     * Initializes and displays the login form UI.
     *
     * @precondition None.
     * @postcondition Login form is visible and ready for user interaction.
     */
    public void initialize(){
        JLabel lbLoginForm = new JLabel("Login Form", SwingConstants.CENTER);
        lbLoginForm.setFont(mainFont);

        JLabel lbName = new JLabel("Name:");
        lbName.setFont(mainFont);

        tfName = new JTextField();
        tfName.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0,1,10,10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(lbLoginForm);
        formPanel.add(lbName);
        formPanel.add(tfName);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);
        btnLogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                String name = tfName.getText();
                String password = new String(pfPassword.getPassword());

                User user = new User();
                boolean isValid = user.validateUser(name, password);

                if(isValid){
                    user1 = user;
                    Blackjackgame game = new Blackjackgame(user1);
                    game.start();
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(LoginForm.this, "Invalid email or password", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

            JButton btnCancel = new JButton("Cancel");
            btnCancel.setFont(mainFont);
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });


            JButton btnCreate = new JButton("Create");
            btnCreate.setFont(mainFont);
            btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                  CreateForm createForm = new CreateForm();
                  createForm.initialize();
                  dispose();
            }
            });

            
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1,2,10,0));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 55, 30, 55));
            buttonPanel.add(btnLogin);
            buttonPanel.add(btnCancel);
            buttonPanel.add(btnCreate);

            add(formPanel, BorderLayout.NORTH);
            add(buttonPanel, BorderLayout.SOUTH);

            setTitle("Login Form");
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            setSize(400,500);
            setMinimumSize(new Dimension(350,450));
            setLocationRelativeTo(null);
            setVisible(true);

            
        }
       

    /**
     * Main entry point for the login form.
     *
     * @param args Command-line arguments (not used).
     * @precondition None.
     * @postcondition Login form is displayed.
     */
    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm();
        loginForm.initialize();
    }
    
}
