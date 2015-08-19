package com.gnatiuk.searcher.ui;

import com.gnatiuk.searcher.core.Finder;
import com.gnatiuk.searcher.core.ThreadController;
import com.gnatiuk.searcher.core.filters.FiltersContainer;
import com.gnatiuk.searcher.core.utils.*;
import com.gnatiuk.searcher.ui.utils.FileSearchStatusColored;
import com.gnatiuk.searcher.ui.utils.FloatPanelWrapper;
import com.gnatiuk.searcher.ui.utils.FoundTreePanel;
import com.gnatiuk.searcher.ui.utils.JFXFilesTreePanel;
import com.gnatiuk.searcher.ui.utils.filters.components.SearchFiltersContainerComponent;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Created by sgnatiuk on 5/28/15.
 */
public class MainFrame extends JFrame{

    private JButton runJButton;
    private JFXFilesTreePanel jfxFilesTreePanel;
    private FoundTreePanel foundListViewPanel;
//    private FiltersPanel filtersPanel;

    private JPanel leftPanel;
    private JPanel rightPanel;
    private FloatPanelWrapper foundFloatPanel;
    private JLayeredPane layeredPane;

    private JSplitPane splitPane;


    private SearchFiltersContainerComponent filtersContainer;


    public MainFrame(){
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents(){

        layeredPane = this.getLayeredPane();
        initPanels();

        addFoundListPanel();
        addFilesTreePanel();
        addFiltersListPanel();
        addRunButton();

        addSplitPane();

        ThreadController.getInstance().addWorkCompleteListener(new IWorkCompleteListener() {
            @Override
            public void actionPerformed(WorkCompleteEvent event) {
                JOptionPane.showMessageDialog(null,"Done");
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
//                for (String file : event.getProcessedFiles()) {
//                    synchronized (jfxFilesTreePanel) {
//                        jfxFilesTreePanel.setNodeStatusByPath(file,
//                                FileSearchStatusColored.determineFileSearchStatusColored(event.getSearchStatus()));
//                    }
//                }
            }
        });

        ThreadController.getInstance().registerFileFoundListener(new IFileFoundListener() {
            @Override
            public void alertFileFound(FileSearchEvent fileSearchEvent) {
                foundListViewPanel.addItem(fileSearchEvent);
                if (foundFloatPanel.isVisible()) {
                    layeredPane.add(foundFloatPanel.getFloatPanel(), Integer.valueOf(2));
                }
            }
        });
    }

    private void initPanels(){
        leftPanel = new JPanel(new BorderLayout());
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
    }

    private void addFoundListPanel(){
        foundListViewPanel = new FoundTreePanel();
        foundFloatPanel = new FloatPanelWrapper(foundListViewPanel, getSize());
//        layeredPane.add(foundFloatPanel.getFloatPanel(), Integer.valueOf(2));
    }

    private void addSplitPane(){
        splitPane = new JSplitPane();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, new JScrollPane(rightPanel));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(600);
        splitPane.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.setLayout(new BorderLayout());
        layeredPane.add(splitPane, BorderLayout.CENTER, Integer.valueOf(1));
    }

    private void addFilesTreePanel(){
        jfxFilesTreePanel = new JFXFilesTreePanel();
        rightPanel.add(jfxFilesTreePanel);
    }

    private void addFiltersListPanel(){
        filtersContainer = new SearchFiltersContainerComponent();

        JFXPanel jfxPanel = new JFXPanel();
        Platform.runLater(() -> {
                    StackPane root = new StackPane();
                    root.getChildren().add(filtersContainer.getSearchCriteriaComponentsPane());
                    Scene scene = new Scene(root);
                    jfxPanel.setScene(scene);
                }
        );

        leftPanel.add(jfxPanel, BorderLayout.CENTER);
    }


    private void addRunButton(){
        runJButton = new JButton("start");
        runJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                foundListViewPanel.clear();
                FiltersContainer filters = new FiltersContainer();
                filters.addFilter(filtersContainer.getFilter());

                java.util.List<String> paths = jfxFilesTreePanel.getSelectedPaths();
                paths = Arrays.asList("/home/sgnatiuk/Desktop/logs");
                Finder finder = new Finder(paths,filters);
                Finder.t1 = System.currentTimeMillis();
                finder.start();
            }
        });
        leftPanel.add(runJButton, BorderLayout.SOUTH);
    }

    private void addSearchHandlerButtons() {
        addRunButton();

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                ThreadController.getInstance().stop();
            }
        });


        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                ThreadController.getInstance().pause();
            }
        });

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                ThreadController.getInstance().resume();
            }
        });

//        leftPanel.add(stopButton);
//        leftPanel.add(pauseButton);
//        leftPanel.add(resumeButton);
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
