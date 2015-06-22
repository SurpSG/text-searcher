package com.gnatiuk.searcher.ui;

import com.gnatiuk.searcher.core.Finder;
import com.gnatiuk.searcher.core.ThreadController;
import com.gnatiuk.searcher.core.filters.*;
import com.gnatiuk.searcher.core.filters.internal_file_filter.FilterFileKeywordIgnoreCase;
import com.gnatiuk.searcher.core.filters.external_file_filter.*;
import com.gnatiuk.searcher.core.utils.*;
import com.gnatiuk.searcher.ui.utils.FileSearchStatusColored;
import com.gnatiuk.searcher.ui.utils.FoundListViewPanel;
import com.gnatiuk.searcher.ui.utils.JFXFilesTreePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * Created by sgnatiuk on 5/28/15.
 */
public class MainFrame extends JFrame{

    private JButton runJButton;
    private JComboBox<String> keywordsJComboBox;
    private JFXFilesTreePanel jfxFilesTreePanel;
    private FoundListViewPanel foundListViewPanel;
    private JTextField fileFilterField;

    private JPanel leftPanel;
    private JPanel rightPanel;

    private JSplitPane splitPane;

    private String rootDir;

    public MainFrame(){
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rootDir = "/home/sgnatiuk/Downloads";
        initComponents();
    }

    private void initComponents(){

        initPanels();

        addKeywordsJComboBox();
        addFoundListView();
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

        ThreadController.getInstance().registerTaskStartedListener(new ITaskStartedListener() {
            @Override
            public void actionPerformed(TaskStartedEvent event) {
                for (String file : event.getFilesToProcess()) {
                    synchronized (jfxFilesTreePanel) {
                        jfxFilesTreePanel.setNodeStatusByPath(file, FileSearchStatusColored.IN_PROGRESS_COLOR);
                    }
                }
            }
        });

        ThreadController.getInstance().registerTaskCompleteListener(new ITaskCompleteListener() {
            @Override
            public void actionPerformed(TaskCompleteEvent event) {
                for (String file : event.getProcessedFiles()) {
                    synchronized (jfxFilesTreePanel) {
                        jfxFilesTreePanel.setNodeStatusByPath(file,
                                FileSearchStatusColored.determineFileSearchStatusColored(event.getSearchStatus()));
                    }
                }
            }
        });

        ThreadController.getInstance().registerFileFoundListener(new IFileFoundListener() {
            @Override
            public void alertFileFound(FileFoundEvent fileFoundEvent) {
                foundListViewPanel.addItem(fileFoundEvent.getFilePath());
            }
        });
    }

    private void initPanels(){
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
    }

    private void addFoundListView(){
        foundListViewPanel = new FoundListViewPanel();
        leftPanel.add(foundListViewPanel);
    }

    private void addSplitPane(){
        splitPane = new JSplitPane();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, new JScrollPane(rightPanel));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);
        add(splitPane);
    }

    private void addFilesTreePanel(){
        jfxFilesTreePanel = new JFXFilesTreePanel(rootDir);
        rightPanel.add(jfxFilesTreePanel);
    }

    private void addFileFilterField(){
        fileFilterField = new JTextField(".*");
        fileFilterField.setPreferredSize(new Dimension(100,20));
        leftPanel.add(fileFilterField);
    }

    private void addKeywordsJComboBox(){
        keywordsJComboBox = new JComboBox();
        keywordsJComboBox.addItem("private");
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

                foundListViewPanel.clear();
                String textToFind = keywordsJComboBox.getSelectedItem().toString();

                FiltersContainer filters = new FiltersContainer();
//                filters.addFilter(new FilterFileName(Arrays.asList("")));
                filters.addFilter(new FilterFileNameRegex(Arrays.asList(fileFilterField.getText())));
                filters.addFilter(new FilterFileKeywordIgnoreCase(Arrays.asList(textToFind)));

                Finder finder = new Finder(jfxFilesTreePanel.getSelectedPaths(),filters);
                Finder.t1 = System.currentTimeMillis();
                finder.start();
            }
        });
        leftPanel.add(runJButton);
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
