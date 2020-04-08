package DictionaryClient;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This class is responsible for client side GUI.
 */
public class ClientGUI extends JFrame {
    private JPanel mainPanel;
    private JTextField clientInputTextFiled;
    private JButton addButton;
    private JButton searchButton;
    private JButton deleteButton;

    private JTextArea clientOutputTextArea;

    private JLabel statusConnectivityLabel;
    private JTextArea serverResponse;
    private ClientController clientController;

    String patternString = "[A-Za-z0-9 _.,!\"'/$]*";
    Pattern pattern = Pattern.compile(patternString);


    public ClientGUI(String appName, ClientController clientController) {
        super(appName);
        this.clientController = clientController;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setLocationRelativeTo(null);

        setAddButtonLogic();
        setDeleteButtonLogic();
        setSearchButtonLogic();
    }

    /**
     * Set the add button logic
     */
    public void setAddButtonLogic() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // the input message should not contain the preserved keyword SEPARATOR
                String word = clientInputTextFiled.getText();
                String meaning = clientOutputTextArea.getText();
                Matcher wordMatcher = pattern.matcher(word);
                Matcher meaningMatcher = pattern.matcher(meaning);
                if (!meaningMatcher.matches() || !wordMatcher.matches()) {
                    serverResponse.setForeground(new Color(255, 0, 0));
                    serverResponse.setText("The input is not valid, contains invalid chars. The valid pattern is \"[A-Za-z0-9 _.,!\\\"'/$]*\"");

                } else {
                    clientController.add(word, meaning);
                }
            }
        });
    }

    /**
     * Set the delete button logic
     */
    public void setDeleteButtonLogic() {
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = clientInputTextFiled.getText();
                Matcher wordMatcher = pattern.matcher(word);
                if (!wordMatcher.matches()) {
                    serverResponse.setForeground(new Color(255, 0, 0));
                    serverResponse.setText("The input is not valid, contains invalid chars. The valid pattern is \"[A-Za-z0-9 _.,!\\\"'/$]*\"");

                } else {
                    clientController.delete(word);
                }
            }
        });
    }

    /**
     * Set the search button logic
     */
    public void setSearchButtonLogic() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = clientInputTextFiled.getText();
                Matcher wordMatcher = pattern.matcher(word);
                if (!wordMatcher.matches()) {
                    serverResponse.setForeground(new Color(255, 0, 0));
                    serverResponse.setText("The input is not valid, contains invalid chars. The valid pattern is \"[A-Za-z0-9 _.,!\\\"'/$]*\"");

                } else {
                    clientController.search(word);
                }
            }
        });
    }


    public JTextArea getClientOutputTextArea() {
        return clientOutputTextArea;
    }


    public JTextArea getServerResponse() {
        return serverResponse;
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(15, 10, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setAutoscrolls(false);
        mainPanel.setPreferredSize(new Dimension(492, 458));
        mainPanel.setBorder(BorderFactory.createTitledBorder(""));
        clientInputTextFiled = new JTextField();
        clientInputTextFiled.setMargin(new Insets(2, 6, 2, 6));
        mainPanel.add(clientInputTextFiled, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 7, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, 41), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Word:");
        mainPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(80, 25), new Dimension(80, 25), new Dimension(80, 25), 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Meaning: ");
        mainPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 12, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(80, 25), new Dimension(80, 25), new Dimension(80, 25), 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        scrollPane1.setAutoscrolls(false);
        mainPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 12, 7, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        clientOutputTextArea = new JTextArea();
        clientOutputTextArea.setLineWrap(true);
        clientOutputTextArea.setText("");
        scrollPane1.setViewportView(clientOutputTextArea);
        searchButton = new JButton();
        searchButton.setText("Search");
        mainPanel.add(searchButton, new com.intellij.uiDesigner.core.GridConstraints(0, 9, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Delete ");
        mainPanel.add(deleteButton, new com.intellij.uiDesigner.core.GridConstraints(4, 9, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addButton = new JButton();
        addButton.setActionCommand("Add");
        addButton.setText(" Add ");
        mainPanel.add(addButton, new com.intellij.uiDesigner.core.GridConstraints(2, 9, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        statusConnectivityLabel = new JLabel();
        statusConnectivityLabel.setText("System message:");
        mainPanel.add(statusConnectivityLabel, new com.intellij.uiDesigner.core.GridConstraints(14, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(120, 25), new Dimension(120, 25), new Dimension(120, 25), 0, false));
        serverResponse = new JTextArea();
        serverResponse.setAutoscrolls(false);
        serverResponse.setBackground(new Color(-1644826));
        serverResponse.setCaretColor(new Color(-4473925));
        serverResponse.setDisabledTextColor(new Color(-1733928));
        serverResponse.setEditable(false);
        serverResponse.setFocusable(false);
        serverResponse.setForeground(new Color(-15374363));
        serverResponse.setLineWrap(true);
        serverResponse.setMaximumSize(new Dimension(354, 50));
        serverResponse.setMinimumSize(new Dimension(354, 50));
        serverResponse.setPreferredSize(new Dimension(354, 50));
        serverResponse.setText("");
        mainPanel.add(serverResponse, new com.intellij.uiDesigner.core.GridConstraints(14, 2, 1, 8, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(300, 50), new Dimension(300, 50), new Dimension(300, 50), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
