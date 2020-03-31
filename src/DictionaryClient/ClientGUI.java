package DictionaryClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI extends JFrame {
    private JPanel mainPanel;
    private JTextArea clientInputTextArea;
    private JButton addButton;
    private JButton searchButton;
    private JButton deleteButton;
    private JTextArea clientOutputTextArea;
    private JLabel statusConnectivityLabel;

    public ClientGUI(String appName){
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();

        this.setLocationRelativeTo(null);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // testing just up-casing all letters
                clientOutputTextArea.setText(clientOutputTextArea.getText().toUpperCase());
            }
        });
    }

    public static void main(String[] args) {
        JFrame clientGUI = new ClientGUI("Multi-Threading Dictionary Client");
        clientGUI.setVisible(true);
    }

}
