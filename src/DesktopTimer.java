import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DesktopTimer extends JFrame implements ActionListener {
    // ---------- GUI ----------
    private static JPanel mainPanel;
    private static JPanel[] panel;
    private static TitledBorder hourBorder, minuteBorder, secondBorder;
    private static JTextField[] hourField, minuteField, secondField;
    private static JButton[] startButton, stopButton, resetButton;

    // ---------- タイマー ----------
    private static int[] startTime, startHour, startMinute, startSecond;
    private static int[] currentTime, currentHour, currentMinute, currentSecond;
    private static Timer[] timer;
    final private static int n = 3;

    public static void main(String[] args) {
        init();

        DesktopTimer desktopTimer = new DesktopTimer();
        desktopTimer.setSize(400, 300);
        desktopTimer.setVisible(true);
        desktopTimer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktopTimer.setResizable(false);

        Container contentPane = desktopTimer.getContentPane();
        contentPane.add(mainPanel);
    }

    public DesktopTimer() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        hourBorder = new TitledBorder("hour");
        minuteBorder = new TitledBorder("min");
        secondBorder = new TitledBorder("sec");

        for (int i = 0; i < n; i++) {
            panel[i] = new JPanel();

            hourField[i] = new JTextField();
            hourField[i].setBorder(hourBorder);
            hourField[i].setPreferredSize(new Dimension(50, 50));

            minuteField[i] = new JTextField();
            minuteField[i].setBorder(minuteBorder);
            minuteField[i].setPreferredSize(new Dimension(50, 50));

            secondField[i] = new JTextField();
            secondField[i].setBorder(secondBorder);
            secondField[i].setPreferredSize(new Dimension(50, 50));

            startButton[i] = new JButton("start");
            startButton[i].addActionListener(this);
            stopButton[i] = new JButton("stop");
            stopButton[i].addActionListener(this);
            resetButton[i] = new JButton("reset");
            resetButton[i].addActionListener(this);

            panel[i].add(hourField[i]);
            panel[i].add(minuteField[i]);
            panel[i].add(secondField[i]);
            panel[i].add(startButton[i]);
            panel[i].add(stopButton[i]);
            panel[i].add(resetButton[i]);

            timer[i] = new Timer(1000, this);

            mainPanel.add(panel[i]);
        }
    }

    private static void init() {
        panel = new JPanel[n];

        hourField = new JTextField[n];
        minuteField = new JTextField[n];
        secondField = new JTextField[n];

        startButton = new JButton[n];
        stopButton = new JButton[n];
        resetButton = new JButton[n];

        startTime = new int[n];
        startHour = new int[n];
        startMinute = new int[n];
        startSecond = new int[n];

        currentTime = new int[n];
        currentHour = new int[n];
        currentMinute = new int[n];
        currentSecond = new int[n];

        timer = new Timer[n];
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object pushedButton = e.getSource();
        for (int i = 0; i < n; i++) {
            if (pushedButton == startButton[i]) {
                startHour[i] = Integer.parseInt(hourField[i].getText());
                startMinute[i] = Integer.parseInt(minuteField[i].getText());
                startSecond[i] = Integer.parseInt(secondField[i].getText());
                startTime[i] = startHour[i] * 3600 + startMinute[i] * 60 + startSecond[i];

                currentHour[i] = startHour[i];
                currentMinute[i] = startMinute[i];
                currentSecond[i] = startSecond[i];
                currentTime[i] = startTime[i];

                hourField[i].setEditable(false);
                minuteField[i].setEditable(false);
                secondField[i].setEditable(false);

                timer[i].start();
            } else if (pushedButton == stopButton[i]) {
                hourField[i].setEditable(true);
                minuteField[i].setEditable(true);
                secondField[i].setEditable(true);

                timer[i].stop();
            } else if (pushedButton == resetButton[i]) {
                currentTime[i] = startTime[i];
                currentHour[i] = startHour[i];
                currentMinute[i] = startMinute[i];
                currentSecond[i] = startSecond[i];

                hourField[i].setText(String.valueOf(currentHour[i]));
                minuteField[i].setText(String.valueOf(currentMinute[i]));
                secondField[i].setText(String.valueOf(currentSecond[i]));

                hourField[i].setEditable(true);
                minuteField[i].setEditable(true);
                secondField[i].setEditable(true);
            } else if (e.getSource() == timer[i]) {
                currentTime[i]--;
                currentHour[i] = currentTime[i] / 3600;
                currentTime[i] %= 3600;
                currentMinute[i] = currentTime[i] / 60;
                currentTime[i] %= 60;
                currentSecond[i] = currentTime[i];
                currentTime[i] = currentHour[i] * 3600 + currentMinute[i] * 60 + currentSecond[i];

                hourField[i].setText(String.valueOf(currentHour[i]));
                minuteField[i].setText(String.valueOf(currentMinute[i]));
                secondField[i].setText(String.valueOf(currentSecond[i]));

                if (currentTime[i] == 0) {
                    timer[i].stop();
                    hourField[i].setEditable(true);
                    minuteField[i].setEditable(true);
                    secondField[i].setEditable(true);
                }
            }
        }
    }
}