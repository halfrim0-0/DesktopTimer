import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DesktopTimer extends JFrame implements ActionListener {
    // ---------- GUI ----------
    private static JPanel panel;
    private static TitledBorder hourBorder, minuteBorder, secondBorder;
    private static JTextField hourField, minuteField, secondField;
    private static JButton startButton, stopButton, resetButton;

    // ---------- タイマー ----------
    private static int startTime, startHour, startMinute, startSecond;
    private static int currentTime, currentHour, currentMinute, currentSecond;
    private static boolean isStopped = false;
    private static Timer timer;

    public static void main(String[] args) {
        panel = new JPanel();
        DesktopTimer desktopTimer = new DesktopTimer();
        desktopTimer.setSize(600, 300);
        desktopTimer.setVisible(true);
        desktopTimer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = desktopTimer.getContentPane();
        contentPane.add(panel);
    }

    public DesktopTimer() {
        hourField = new JTextField();
        hourBorder = new TitledBorder("hour");
        hourField.setBorder(hourBorder);
        hourField.setPreferredSize(new Dimension(50, 50));

        minuteField = new JTextField();
        minuteBorder = new TitledBorder("min");
        minuteField.setBorder(minuteBorder);
        minuteField.setPreferredSize(new Dimension(50, 50));

        secondField = new JTextField();
        secondBorder = new TitledBorder("sec");
        secondField.setBorder(secondBorder);
        secondField.setPreferredSize(new Dimension(50, 50));

        startButton = new JButton("start");
        startButton.addActionListener(this);
        stopButton = new JButton("stop");
        stopButton.addActionListener(this);
        resetButton = new JButton("reset");
        resetButton.addActionListener(this);

        panel.add(hourField);
        panel.add(minuteField);
        panel.add(secondField);
        panel.add(startButton);
        panel.add(stopButton);
        panel.add(resetButton);

        timer = new Timer(1000, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object pushedButton = e.getSource();
        if (pushedButton == startButton) {
            startHour = Integer.parseInt(hourField.getText());
            startMinute = Integer.parseInt(minuteField.getText());
            startSecond = Integer.parseInt(secondField.getText());
            startTime = startHour * 3600 + startMinute * 60 + startSecond;

            currentHour = startHour;
            currentMinute = startMinute;
            currentSecond = startSecond;
            currentTime = startTime;

            hourField.setEditable(false);
            minuteField.setEditable(false);
            secondField.setEditable(false);

            timer.start();
        } else if (pushedButton == stopButton) {
            hourField.setEditable(true);
            minuteField.setEditable(true);
            secondField.setEditable(true);

            timer.stop();
        } else if (pushedButton == resetButton) {
            currentTime = startTime;
            currentHour = startHour;
            currentMinute = startMinute;
            currentSecond = startSecond;

            hourField.setText(String.valueOf(currentHour));
            minuteField.setText(String.valueOf(currentMinute));
            secondField.setText(String.valueOf(currentSecond));

            hourField.setEditable(true);
            minuteField.setEditable(true);
            secondField.setEditable(true);
        } else {
            // Timer の処理
            currentTime --;
            currentHour = currentTime / 3600;
            currentTime %= 3600;
            currentMinute = currentTime / 60;
            currentTime %= 60;
            currentSecond = currentTime;
            currentTime = currentHour * 3600 + currentMinute * 60 + currentSecond;

            hourField.setText(String.valueOf(currentHour));
            minuteField.setText(String.valueOf(currentMinute));
            secondField.setText(String.valueOf(currentSecond));
        }
    }
}