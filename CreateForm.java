import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CreateForm extends JFrame {
    final private Font mainFont = new Font("Inter", Font.PLAIN, 18);
    JTextField tfName;
    JPasswordField pfPassword;
    JPasswordField pfPassword2;
    static User user1;

    public void initialize(){JLabel lbCreateForm = new JLabel("Create Account", SwingConstants.CENTER);
        lbCreateForm.setFont(mainFont);

        JLabel lbName = new JLabel("Name:");
        lbName.setFont(mainFont);

        tfName = new JTextField();
        tfName.setFont(mainFont);

        JLabel lbPassword = new JLabel("Password");
        lbPassword.setFont(mainFont);

        JLabel lbPassword2 = new JLabel("Confirm Password");
        lbPassword2.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);

        pfPassword2 = new JPasswordField();
        pfPassword2.setFont(mainFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0,1,10,10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        formPanel.add(lbCreateForm);
        formPanel.add(lbName);
        formPanel.add(tfName);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);
        formPanel.add(lbPassword2);
        formPanel.add(pfPassword2);

        JButton btnCreate = new JButton("Create Account");
        btnCreate.setFont(mainFont);
        btnCreate.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                String name = tfName.getText();
                String password = new String(pfPassword.getPassword());
                String password2 = new String(pfPassword2.getPassword());

                if(!password.equals(password2)){
                    JOptionPane.showMessageDialog(CreateForm.this, "Passwords do not match", "Create Account Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User user = new User();
                boolean isCreated = user.createUser(name, password);
                if(isCreated){
                    JOptionPane.showMessageDialog(CreateForm.this, "Account created successfully! Please login with your new credentials.", "Account Created", JOptionPane.INFORMATION_MESSAGE);
                    LoginForm loginForm = new LoginForm();
                    loginForm.initialize();
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(CreateForm.this, "Account creation failed. Please try again.", "Create Account Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2,10,0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 55, 30, 55));
        buttonPanel.add(btnCreate);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Create Account Form");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400,500);
        setMinimumSize(new Dimension(350,450));
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
