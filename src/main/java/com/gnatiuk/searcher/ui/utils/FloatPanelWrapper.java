package com.gnatiuk.searcher.ui.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by sgnatiuk on 7/27/15.
 */
public class FloatPanelWrapper {

    private JComponent foundContainer;

    private JPanel toolPanel;
    private JPanel floatPanel;
    private JButton hideButton;

    public FloatPanelWrapper(JComponent foundComponent, Dimension floatPanelContainerDimension){

        this.foundContainer = foundComponent;

        initFloatPanel(floatPanelContainerDimension);
        initToolPanel();

        floatPanel.add(this.foundContainer, BorderLayout.CENTER);
        foundContainer.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                hide();
            }
        });
    }

    private void initFloatPanel(Dimension containerSize){
        floatPanel = new JPanel();
        floatPanel.setLayout(new BorderLayout());
        floatPanel.setBounds(0, 2 * containerSize.height / 3, containerSize.width, containerSize.height / 3);
    }

    private void initToolPanel(){
        toolPanel = new JPanel();
        toolPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        toolPanel.setPreferredSize(new Dimension(floatPanel.getWidth(), 20));
        toolPanel.setBackground(Color.blue);

        initHideButton();

        toolPanel.addMouseMotionListener(new MouseMotionAdapter() {

            private static final int START_Y = Integer.MIN_VALUE;
            private int currentY = START_Y;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (currentY == START_Y) {
                    currentY = e.getY();
                }
                int diff = e.getY() - currentY;
                floatPanel.setBounds(floatPanel.getX(), floatPanel.getY() + diff,
                        floatPanel.getWidth(), floatPanel.getHeight() - diff);
                floatPanel.updateUI();
            }
        });

        toolPanel.add(hideButton);

        floatPanel.add(toolPanel, BorderLayout.NORTH);
    }

    private void initHideButton(){
        hideButton = new JButton("Hide");
        hideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hide();
            }
        });
    }

    public JPanel getFloatPanel() {
        return floatPanel;
    }

    public void addFocusListener(FocusListener focusListener){
        floatPanel.addFocusListener(focusListener);
    }

    public boolean isVisible(){
        return floatPanel.getParent() == null;
    }

    public void hide(){
        Container container = floatPanel.getParent();
        container.remove(floatPanel);
        container.repaint();
    }
}
