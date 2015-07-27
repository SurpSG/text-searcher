package com.gnatiuk.searcher.ui;

import com.gnatiuk.searcher.core.Finder;
import com.gnatiuk.searcher.core.ThreadController;
import com.gnatiuk.searcher.core.filters.*;
import com.gnatiuk.searcher.core.utils.*;
import com.gnatiuk.searcher.ui.utils.*;
import com.gnatiuk.searcher.ui.utils.filters.components.FileNameExcludeFilterComponent;
import com.gnatiuk.searcher.ui.utils.filters.components.FileNameFilterComponent;
import com.gnatiuk.searcher.ui.utils.filters.components.KeywordFilterComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Created by sgnatiuk on 5/28/15.
 */
public class MainFrame extends JFrame{

    private JButton runJButton;
    private JFXFilesTreePanel jfxFilesTreePanel;
    private FoundTreePanel foundListViewPanel;
    private FiltersListPanel filtersListPanel;

    private KeywordFilterComponent keywordFilterComponent;
    private FileNameFilterComponent fileNameFilterComponent;
    private FileNameExcludeFilterComponent fileNameExcludeFilterComponent;

    private JPanel leftPanel;
    private JPanel rightPanel;
    private FloatPanelWrapper foundFloatPanel;
    private JLayeredPane layeredPane;

    private JSplitPane splitPane;




    public MainFrame(){
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }

    private void initComponents(){

        layeredPane = this.getLayeredPane();
        initPanels();
        keywordFilterComponent = new KeywordFilterComponent();
        leftPanel.add(keywordFilterComponent.getSearchCriteriaComponentsPanel());
        fileNameFilterComponent = new FileNameFilterComponent();
        leftPanel.add(fileNameFilterComponent.getSearchCriteriaComponentsPanel());
        fileNameExcludeFilterComponent = new FileNameExcludeFilterComponent();
        leftPanel.add(fileNameExcludeFilterComponent.getSearchCriteriaComponentsPanel());

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
                if(foundFloatPanel.isVisible()){
                    layeredPane.add(foundFloatPanel.getFloatPanel(), Integer.valueOf(2));
                }
            }
        });
    }

    private void initPanels(){
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
    }

    private void addFoundListPanel(){
        foundListViewPanel = new FoundTreePanel();
        foundFloatPanel = new FloatPanelWrapper(foundListViewPanel, getSize());
        layeredPane.add(foundFloatPanel.getFloatPanel(), Integer.valueOf(2));
    }

    private void addSplitPane(){
        splitPane = new JSplitPane();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, new JScrollPane(rightPanel));
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(400);
        splitPane.setBounds(0,0,getWidth(),getHeight());
        layeredPane.add(splitPane, Integer.valueOf(1));
    }

    private void addFilesTreePanel(){
        jfxFilesTreePanel = new JFXFilesTreePanel();
        rightPanel.add(jfxFilesTreePanel);
    }

    private void addFiltersListPanel(){
        filtersListPanel = new FiltersListPanel();
        leftPanel.add(filtersListPanel);
    }


    private void addRunButton(){
        runJButton = new JButton("start");
        runJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                foundListViewPanel.clear();
                FiltersContainer filters = new FiltersContainer();
                filters.addFilter(fileNameFilterComponent.buildFilter())
                        .addFilter(fileNameExcludeFilterComponent.buildFilter())
                        .addFilter(keywordFilterComponent.buildFilter());
                filtersListPanel.clearFilters();
                filtersListPanel.addFilter(filters);

                java.util.List<String> paths = jfxFilesTreePanel.getSelectedPaths();
                paths = Arrays.asList("/home/sgnatiuk/Desktop/logs");
                Finder finder = new Finder(paths,filters);
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
