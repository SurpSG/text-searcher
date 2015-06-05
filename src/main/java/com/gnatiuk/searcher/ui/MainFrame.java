package com.gnatiuk.searcher.ui;

import com.gnatiuk.searcher.core.Finder;
import com.gnatiuk.searcher.core.ThreadController;
import com.gnatiuk.searcher.core.utils.*;
import com.gnatiuk.searcher.ui.utils.FileJTreePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created by sgnatiuk on 5/28/15.
 */
public class MainFrame extends JFrame{

    private JButton runJButton;
    private JComboBox<String> keywordsJComboBox;
    private JTextArea keywordsTextArea;
    private FileJTreePanel fileJTreePanel;
    private JTextField fileFilterField;

    private JPanel leftPanel;
    private JPanel rightPanel;

    private JSplitPane splitPane;

    public MainFrame(){
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents(){

        initPanels();

        addKeywordsJComboBox();
        addKeywordsTextArea();
        addFilesTreePanel();
        addFileFilterField();
        addRunButton();

        addSplitPane();

        ThreadController.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                System.out.println("done!!!!!");
            }
        });

        ThreadController.getInstance().addTaskStartedListener(new ITaskStartedListener() {
            @Override
            public void actionPerformed(TaskStartedEvent event) {
                System.out.println("Thread started[id: "+event.getTaskId()+", files: "+event.getFilesToProcess()+"]");

            }
        });
    }

    private void initPanels(){
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
    }

    private void addSplitPane(){
        splitPane = new JSplitPane();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, new JScrollPane(rightPanel));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        add(splitPane);
    }

    private void addFilesTreePanel(){
//        fileJTreePanel = new FileJTreePanel("/home/sgnatiuk");
//        fileJTreePanel = new FileJTreePanel("/cryptfs/builds/shr-26h/root_pac/build_mips");
//        fileJTreePanel = new FileJTreePanel("C:/");
        fileJTreePanel = new FileJTreePanel("/home/sgnatiuk");
        rightPanel.add(new JScrollPane(fileJTreePanel));
    }

    private void addFileFilterField(){
        fileFilterField = new JTextField(".*java$");
        fileFilterField.setPreferredSize(new Dimension(100,20));
        leftPanel.add(fileFilterField);
    }

    private void addKeywordsJComboBox(){
        keywordsJComboBox = new JComboBox();
        keywordsJComboBox.addItem("");
        keywordsJComboBox.setEditable(true);

        keywordsJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("comboBoxEdited")) {
                    keywordsJComboBox.addItem(keywordsJComboBox.getSelectedItem().toString());
                }
            }
        });
        leftPanel.add(keywordsJComboBox);
    }

    private void addRunButton(){
        runJButton = new JButton("start");
        runJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String textToFind = keywordsJComboBox.getSelectedItem().toString();
                Finder finder = Finder.build(FinderType.DEFAULT,
                        textToFind,
                        fileJTreePanel.getSelectedFilePaths(),
                        Arrays.asList(fileFilterField.getText()));
                finder.start();
            }
        });
        leftPanel.add(runJButton);
    }

    private void addKeywordsTextArea(){
        keywordsTextArea = new JTextArea();
        keywordsTextArea.setPreferredSize(/**/new Dimension(300,200));
        keywordsTextArea.setEditable(false);
        leftPanel.add(keywordsTextArea);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}
