package client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.server.UserClientService;
@SuppressWarnings("all")
public class OnlineFriendsListFrame extends JFrame {
    private JButton getListButton, startChatButton;
    private JTextArea showcase;
    private JTextField inputText;
    private JLabel userLabel;
    private UserClientService clientService;

    public OnlineFriendsListFrame(String userId, UserClientService clientService) {
        this.clientService = clientService;
        
    }
    /* getter setter */
    public JTextArea getShowcase() {
        return showcase;
    }
    public void setShowcase(JTextArea showcase) {
        this.showcase = showcase;
    }
    public JTextField getInputText() {
        return inputText;
    }
    public void setInputText(JTextField inputText) {
        this.inputText = inputText;
    }
}
