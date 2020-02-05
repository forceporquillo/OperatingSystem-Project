package com.project.cpuscheduling;


import Adapter.InterfaceSchedulingAdapter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class InterfaceHolder extends KeyAdapter implements InterfaceSchedulingAdapter {
    // <editor-fold defaultstate="collapsed" desc="variables"
    private JFrame frame;
    private JPanel[] panel;
    private JLabel algorithm;
    private JLabel timeQuantum;
    private JLabel arrivalTime;
    private JLabel burstTime;
    private JLabel averageTurnAroundTime;
    private JLabel averageWaitingTime;
    private JLabel ganttChart;
    private JLabel awtShowTime;
    private JLabel atatShowTime;
    private JButton addButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton calculateButton;
    private JTextField quantumTimeTextField;
    private JTextField arrivalTimeTextField;
    private JTextField burstTimeTextField;
    private JComboBox <? extends String> comboBox;
    private DefaultTableModel model;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JTable table;

    private List <Integer> List1 = new ArrayList<>();
    private List <Integer> List2 = new ArrayList<>();

    private int[] temp2;
    private int[] temp3;
    private int[] process;
    private int[] completionTime;
    // </editor-fold>

    // <editor-fold> defaultstate ="collase" desc="constructor init UI call updated @force" >
    public InterfaceHolder() {
        frame = new JFrame("CPU Scheduling");

        panel = new JPanel[8];

        for (int i = 0; i < 8; i++) {
            panel[i] = new JPanel();
        }

        panel[0].setLayout(new GridLayout(3, 1, 10, 10));
        panel[1].setLayout(new GridLayout(1, 3, 10, 0));
        panel[2].setLayout(new BorderLayout());
        panel[3].setLayout(new BorderLayout(0, 10));
        panel[4].setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel[5].setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        panel[6].setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel[7].setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        algorithm = new JLabel("Algorithm:"); // 1
        timeQuantum = new JLabel("Time Quantum:"); // 2
        arrivalTime = new JLabel("Arrival Time:"); // 3
        burstTime = new JLabel("Burst Time: "); // 4
        averageTurnAroundTime = new JLabel("Average Turnaround Time (ATAT):");
        averageWaitingTime = new JLabel("Average Waiting TIme:");
        ganttChart = new JLabel("Gantt Chart"); // 7
        awtShowTime = new JLabel("0.0 ms"); // 8
        atatShowTime = new JLabel("0.0 ms"); // 9

        addButton = new JButton("ADD");
        deleteButton = new JButton("DELETE");
        clearButton = new JButton("CLEAR");
        calculateButton = new JButton("CALCULATE");

        quantumTimeTextField = new JTextField();
        arrivalTimeTextField = new JTextField();
        burstTimeTextField = new JTextField();

        quantumTimeTextField.setEnabled(false);
        quantumTimeTextField.setBackground(Color.LIGHT_GRAY);

        String[] schedulerOptions = new String[]{"FCFS", "SJF", "Priority", "Round Robin"};
        comboBox = new JComboBox<>(schedulerOptions);

        Object[] processOptions = new String[]{"Process", "Arrival Time", "Burst Time", "Completion Time", "Turnaround Time", "Waiting Time"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(processOptions);

        table = new JTable();
        table.setModel(model);
        table.getTableHeader().setReorderingAllowed(false);

        scrollPane1 = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane2 = new JScrollPane(panel[7], JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        frame.add(panel[0]);

        panel[0].add(panel[1]);
        panel[0].add(panel[2]);
        panel[0].add(panel[3]);

        panel[1].add(panel[4]);
        panel[1].add(panel[5]);
        panel[1].add(panel[6]);

        panel[4].add(algorithm);
        panel[4].add(comboBox);
        panel[4].add(timeQuantum);
        panel[4].add(quantumTimeTextField);

        panel[5].add(arrivalTime);
        panel[5].add(arrivalTimeTextField);
        panel[5].add(burstTime);
        panel[5].add(burstTimeTextField);

        panel[5].add(addButton);
        panel[5].add(deleteButton);
        panel[5].add(clearButton);
        panel[5].add(calculateButton);

        panel[6].add(averageTurnAroundTime);
        panel[6].add(atatShowTime);

        panel[6].add(averageWaitingTime);
        panel[6].add(awtShowTime);

        panel[2].add(scrollPane1, BorderLayout.CENTER);
        panel[3].add(ganttChart, BorderLayout.NORTH);
        panel[3].add(scrollPane2, BorderLayout.CENTER);

        awtShowTime.setPreferredSize(new Dimension(180, 20));
        atatShowTime.setPreferredSize(new Dimension(180, 20));

        quantumTimeTextField.setPreferredSize(new Dimension(180, 30));
        arrivalTimeTextField.setPreferredSize(new Dimension(110, 30));
        burstTimeTextField.setPreferredSize(new Dimension(110, 30));

        addButton.setPreferredSize(new Dimension(105, 30));
        deleteButton.setPreferredSize(new Dimension(105, 30));
        clearButton.setPreferredSize(new Dimension(105, 30));
        calculateButton.setPreferredSize(new Dimension(105, 30));
        comboBox.setPreferredSize(new Dimension(180, 30));

        panel[0].setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Border border = BorderFactory.createLineBorder(Color.GRAY, 1); //sets color border

        panel[4].setBorder(border);
        panel[5].setBorder(border);
        panel[6].setBorder(border);

        awtShowTime.setFont(new Font("", Font.BOLD, 18));
        atatShowTime.setFont(new Font("", Font.BOLD, 18));
        awtShowTime.setForeground(Color.RED);
        atatShowTime.setForeground(Color.RED);

        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        actionButtons();
        disableKeyCharacters();
        comboBoxDisabler();
    }

    // </editor-fold>

    private Object[] row = new Object[6];
    private int rowCounter = 0;
    private double averageTat;
    private double averageWt;

    //<editor-fold> defaultstate="collapse" desc="updated source buttons @force update">
    private void actionButtons() {
        addButton.addActionListener(e -> { // add row buttons
            try {
                if (Objects.equals(comboBox.getSelectedItem(), "Round Robin")) {
                    if(burstTimeTextField.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please enter burst time", "Error Message", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    row[1] = 0;
                } else {
                    if (arrivalTimeTextField.getText().equals("") || burstTimeTextField.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Please fill out all the forms.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    row[1] = Integer.parseInt(arrivalTimeTextField.getText());
                }

                rowCounter++;
                row[0] = rowCounter;
                row[2] = Integer.parseInt(burstTimeTextField.getText());
                model.addRow(row);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteButton.addActionListener(e -> { // delete button
            int i = table.getRowCount() - 1;

            if (i >= 0) {
                model.removeRow(i);
                rowCounter--;
            } else {
                JOptionPane.showMessageDialog(null, "The table is empty", "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearButton.addActionListener(e -> { // clear buttons
            arrivalTimeTextField.setText("");
            burstTimeTextField.setText("");

            awtShowTime.setText("0.00 ms");
            atatShowTime.setText("0.00 ms");

            model.setRowCount(0);
            panel[7].removeAll();
            panel[7].revalidate();
            panel[7].repaint();
            rowCounter = 0;
        });

        calculateButton.addActionListener(e -> {
            averageTat = 0.0;
            averageWt = 0.0;

            List1.removeAll(List1);
            List2.removeAll(List2);

            temp2 = new int[rowCounter];
            temp3 = new int[rowCounter];
            process = new int[rowCounter];
            completionTime = new int[rowCounter];

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "The table is empty.", "Error Message", JOptionPane.ERROR_MESSAGE);
            } else {
                if (Objects.equals(comboBox.getSelectedItem(), "FCFS")) {
                    firstComeFirstServe();;
                } else if (Objects.equals(comboBox.getSelectedItem(), "SJF")) {
                    ShortestJobFirst();
                } else if (Objects.equals(comboBox.getSelectedItem(), "Priority")) {
                    Priority();
                } else {
                    if(quantumTimeTextField.getText().equals("")){
                        JOptionPane.showMessageDialog(null, "Please enter time quantum", "Error Message", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                   RoundRobin();
                }
                getCompletionTime();
                createGanttChart();
                setTableValues();
                getTotalAverage();
            }
        });
    }
    // </editor-fold>

    // <editor-fold> defaultstate ="collapse" desc="combobox disabler update version @royce"
    private void comboBoxDisabler() {
        comboBox.addActionListener(e -> {
            if (Objects.equals(comboBox.getSelectedItem(), "Priority")) {
                arrivalTime.setText("Priority:         ");
                table.getColumnModel().getColumn(1).setHeaderValue("Priority");
                table.getTableHeader().resizeAndRepaint();
                quantumTimeTextField.setEnabled(false);
                quantumTimeTextField.setBackground(Color.LIGHT_GRAY);
                arrivalTimeTextField.setEnabled(true);
                arrivalTimeTextField.setBackground(Color.WHITE);
            } else if (Objects.equals(comboBox.getSelectedItem(), "Round Robin")) {
                arrivalTime.setText("Arrival Time:");
                arrivalTimeTextField.setEnabled(false);
                arrivalTimeTextField.setBackground(Color.LIGHT_GRAY);
                table.getColumnModel().getColumn(1).setHeaderValue("Arrival Time");
                table.getTableHeader().resizeAndRepaint();
                quantumTimeTextField.setEnabled(true);
                quantumTimeTextField.setBackground(Color.WHITE);
                arrivalTimeTextField.setEnabled(false);
                arrivalTimeTextField.setBackground(Color.LIGHT_GRAY);
            } else {
                arrivalTime.setText("Arrival Time:");
                table.getColumnModel().getColumn(1).setHeaderValue("Arrival Time");
                table.getTableHeader().resizeAndRepaint();
                quantumTimeTextField.setEnabled(false);
                quantumTimeTextField.setBackground(Color.LIGHT_GRAY);
                arrivalTimeTextField.setEnabled(true);
                arrivalTimeTextField.setBackground(Color.WHITE);
            }
        });
    }
    // </editor-fold> -

    // <editor-fold defaultstate="collapsed" desc="disable key characters @royce" >
    private void disableKeyCharacters() { // disables character inputs
        arrivalTimeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if ((((e.getKeyChar() < '0') || (e.getKeyChar() > '9')) && (e.getKeyChar() != KeyEvent.VK_BACK_SPACE))) {
                    e.consume();
                }
            }
        });

        burstTimeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if ((((e.getKeyChar() < '0') || (e.getKeyChar() > '9')) && (e.getKeyChar() != KeyEvent.VK_BACK_SPACE))) {
                    e.consume();
                }
            }
        });

        quantumTimeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if ((((e.getKeyChar() < '0') || (e.getKeyChar() > '9')) && (e.getKeyChar() != KeyEvent.VK_BACK_SPACE))) {
                    e.consume();
                }
            }
        });
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc = "update tables - @royce"
    private void setTableValues(){
        for(int i = 0 ; i < rowCounter; i++) {
            model.setValueAt(completionTime[i], i, 3);

            int tat = (int) model.getValueAt(i, 3) - (int) model.getValueAt(i,1);
            averageTat += tat;
            model.setValueAt(tat, i, 4);

            int wt = (int) model.getValueAt(i, 4) - (int) model.getValueAt(i, 2);
            averageWt += wt;
            model.setValueAt(wt, i, 5);
        }
    }


    private void getCompletionTime(){
        for(int i = 0; i < List1.size(); i++){
            completionTime[List1.get(i) - 1] = List2.get(i);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="updated first come first serve algorithm" >
    @Override
    public void firstComeFirstServe() {
        int tempValue = 0;

        for(int i = 0; i < rowCounter; i++){
           temp2[i] = (int) model.getValueAt(i, 2);
           process[i] = (int) model.getValueAt(i, 0);
           tempValue += temp2[i];
           List1.add(process[i]);
           List2.add(tempValue);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="shorted job first royce update" >
    @Override
    public void ShortestJobFirst() {
        for (int i = 0; i < rowCounter; i++) {
            temp2[i] = (int) model.getValueAt(i, 2);
            temp3[i] = (int) model.getValueAt(i, 1);
        }

        int total_time = 0;

        for (int i = 0; i < rowCounter; i++)
            total_time += temp2[i];

        int[] time_chart = new int[total_time];

        for (int i = 0; i < total_time; i++) {
            int sel_process = 0;
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < rowCounter; j++) {
                if (temp3[j] <= i) {
                    if (temp2[j] < min && temp2[j] != 0) {
                        min = temp2[j];
                        sel_process = j;
                    }
                }
            }

            time_chart[i] = sel_process;
            temp2[sel_process]--;

            if (i != 0) {
                if (sel_process != time_chart[i-1]) {
                    List1.add(sel_process + 1);
                    List2.add(i);
                }
            }
            else
                List1.add(sel_process + 1);
            if (i == total_time - 1)
                List2.add(i+1);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="priority royce update" >
    @Override
    public void Priority() {
        for (int i = 0; i < rowCounter; i++) {
            temp2[i] = (int) model.getValueAt(i, 2);
            temp3[i] = (int) model.getValueAt(i, 1);
            process[i] = (int) model.getValueAt(i, 0);
        }
        int swap = 0;
        for (int i = 0; i < rowCounter-1; i++) {
            for (int j = 0; j < rowCounter-i-1; j++) {
                if (temp3[j] > temp3[j+1]) {
                    swap = temp3[j];
                    temp3[j] = temp3[j+1];
                    temp3[j+1] = swap;

                    swap = temp2[j];
                    temp2[j] = temp2[j+1];
                    temp2[j+1] = swap;

                    swap = process[j];
                    process[j] = process[j+1];
                    process[j+1] = swap;
                }
            }
        }

        int tempValue = 0;
        for (int i = 0; i < rowCounter; i++) {
            tempValue += temp2[i];
            List1.add(process[i]);
            List2.add(tempValue);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="round robin algorithm - @force update @royce done"
    @Override
    public void RoundRobin() {
        int quantumTime = Integer.parseInt(quantumTimeTextField.getText());
        int time = 0;

        int[] remBurstTime = new int[rowCounter];

        for(int i = 0; i < rowCounter; i++){
            remBurstTime[i] = (int) model.getValueAt(i, 2);
        }

        while(true) {
            boolean isDone = true;
            for (int i = 0; i < rowCounter; i++) {
                if(remBurstTime[i] > 0){
                    isDone = false;
                    if (remBurstTime[i] > quantumTime) {
                        remBurstTime[i] -= quantumTime;
                        time += quantumTime;
                        List1.add(i+1);
                        List2.add(time);
                    } else {
                        time += remBurstTime[i];
                        List1.add(i+1);
                        List2.add(time);
                        remBurstTime[i] = 0;
                    }
                }
            }
            if(isDone){
                break;
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="updated gantt chart @royce re update"
    @Override
    public void createGanttChart(){
        panel[7].removeAll();
        panel[7].revalidate();
        panel[7].repaint();

        int panelSize = 0;
        Border blackBorder = BorderFactory.createLineBorder(Color.BLACK, 1);

        JLabel fill = new JLabel("");

        JLabel zero = new JLabel("0", JLabel.LEFT);
        zero.setFont(new Font("", Font.BOLD, 16));

        JLabel[] p = new JLabel[List1.size()];
        JLabel[] n = new JLabel[List1.size()];

        for (int i = 0; i < List1.size(); i++) {
            p[i] = new JLabel("P" + List1.get(i), JLabel.CENTER);
            p[i].setOpaque(true);
            p[i].setBorder(blackBorder);
            p[i].setBackground(Color.YELLOW);

            if (i == 0) {
                p[i].setPreferredSize(new Dimension((List2.get(i))*30, 100));
                panelSize += (List2.get(i))*30;
            }
            else {
                p[i].setPreferredSize(new Dimension((List2.get(i) - List2.get(i-1))*30, 100));
                panelSize += ((List2.get(i) - List2.get(i-1)))*30;
            }
            panel[7].add(p[i]);
        }
        if (panelSize < 770) {
            fill.setPreferredSize(new Dimension((770-panelSize), 30));
            panel[7].add(fill);
        }
        for (int i = 0; i < List1.size(); i++) {
            n[i] = new JLabel("" + List2.get(i), JLabel.RIGHT);
            n[i].setFont(new Font("", Font.BOLD, 16));
            if (i == 0) {
                n[i].setPreferredSize(new Dimension(((List2.get(i))*30)/2, 30));
                zero.setPreferredSize(new Dimension(((List2.get(i))*30)/2, 30));
                panel[7].add(zero);
            }
            else
                n[i].setPreferredSize(new Dimension((List2.get(i) - List2.get(i-1))*30, 30));
            panel[7].add(n[i]);
        }
        panel[7].setPreferredSize(new Dimension(panelSize, 300));
    }
    // </editor-fold>


    // <editor-fold defaultstate="collapsed" desc="get total average - royce final"
    private void getTotalAverage () {
        averageTat /= rowCounter;
        averageWt /= rowCounter;

        DecimalFormat dec = new DecimalFormat("#0.00");

        atatShowTime.setText(dec.format(averageTat) + " ms");
        awtShowTime.setText(dec.format(averageWt) + " ms");
    }
    // </editor-fold>
}
