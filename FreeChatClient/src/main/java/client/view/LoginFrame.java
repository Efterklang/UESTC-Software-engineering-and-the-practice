package client.view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * 客户端登录框架
 * 
 * 
 * BackGroundInfo(JFrame组件):
 * JMenuBar：菜单栏，可以在其中添加菜单和菜单项。
 * JToolBar：工具栏，可以在其中添加按钮和其他组件。
 * JPanel：面板，可以在其中添加其他组件，用于组织和布局其他组件。
 * JLabel：标签，用于显示文本或图像。
 * JButton：按钮，用于触发事件。
 * JTextField：文本框，用于输入和显示文本。
 * JTextArea：文本区域，用于输入和显示多行文本。
 * JScrollPane：滚动面板，用于显示带有滚动条的组件。
 * JComboBox：下拉列表框，用于选择一个选项。
 * JCheckBox：复选框，用于选择一个或多个选项。
 * JRadioButton：单选按钮，用于选择一个选项。
 * JTable：表格，用于显示和编辑表格数据。
 * JFileChooser：文件选择器，用于选择文件或目录
 */
public class LoginFrame extends JFrame {
    // 顶部logo
    private JLabel logo_North;

    // constructor
    public LoginFrame() {
        ImageIcon background_North = new ImageIcon();
        background_North.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/background.png"))
                .getScaledInstance(516, 170, Image.SCALE_DEFAULT));
        logo_North = new JLabel(background_North);

        /* 设计界面布局 */
        add(logo_North, BorderLayout.NORTH);
        setVisible(true);
        setBounds(500, 150, 500, 460);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("FreeChat_登录");
    }
}
