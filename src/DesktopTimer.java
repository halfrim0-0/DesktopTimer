import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class DesktopTimer extends JFrame implements ActionListener {
    // ---------- GUI ----------
    private static JPanel mainPanel;
    private static JPanel[] panel;
    private static TitledBorder hourBorder, minuteBorder, secondBorder;
    private static JTextField[] hourField, minuteField, secondField;
    private static JButton[] startButton, stopButton, resetButton;

    private final static Color gray = new Color(204, 204, 204);

    // ---------- タイマー ----------
    private static int[] startTime, startHour, startMinute, startSecond;
    private static int[] currentTime, currentHour, currentMinute, currentSecond;
    private static Timer[] timer;
    final private static int n = 3;

    public static void main(String[] args) {
        init();

        DesktopTimer desktopTimer = new DesktopTimer();
        desktopTimer.setSize(400, 250);
        desktopTimer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        desktopTimer.setResizable(false);
        desktopTimer.setTitle("Desktop Timer");
        desktopTimer.setVisible(true);

        Container contentPane = desktopTimer.getContentPane();
        contentPane.add(mainPanel);
    }

    public DesktopTimer() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        hourBorder = new TitledBorder("hour");
        hourBorder.setTitleColor(gray);
        hourBorder.setTitleFont(new Font(Font.SERIF, Font.PLAIN, 12));
        minuteBorder = new TitledBorder("min");
        minuteBorder.setTitleColor(gray);
        minuteBorder.setTitleFont(new Font(Font.SERIF, Font.PLAIN, 12));
        secondBorder = new TitledBorder("sec");
        secondBorder.setTitleColor(gray);
        secondBorder.setTitleFont(new Font(Font.SERIF, Font.PLAIN, 12));

        for (int i = 0; i < n; i++) {
            panel[i] = new JPanel();
            panel[i].setBackground(new Color(0, 25, 101));

            // ---------- テキストフィールド ----------
            hourField[i] = new JTextField();
            hourField[i].setBorder(hourBorder);
            hourField[i].setPreferredSize(new Dimension(50, 50));
            hourField[i].setBackground(new Color(0, 12, 50));
            // 文字に関する設定
            hourField[i].setForeground(gray);
            hourField[i].setFont(new Font(Font.SERIF, Font.PLAIN, 30));
            hourField[i].setHorizontalAlignment(JTextField.CENTER);
            hourField[i].setCaretColor(gray);

            minuteField[i] = new JTextField();
            minuteField[i].setBorder(minuteBorder);
            minuteField[i].setPreferredSize(new Dimension(50, 50));
            minuteField[i].setBackground(new Color(0, 12, 50));
            // 文字に関する設定
            minuteField[i].setForeground(gray);
            minuteField[i].setFont(new Font(Font.SERIF, Font.PLAIN, 30));
            minuteField[i].setHorizontalAlignment(JTextField.CENTER);
            minuteField[i].setCaretColor(gray);

            secondField[i] = new JTextField();
            secondField[i].setBorder(secondBorder);
            secondField[i].setPreferredSize(new Dimension(50, 50));
            secondField[i].setBackground(new Color(0, 12, 50));
            // 文字に関する設定
            secondField[i].setForeground(gray);
            secondField[i].setFont(new Font(Font.SERIF, Font.PLAIN, 30));
            secondField[i].setHorizontalAlignment(JTextField.CENTER);
            secondField[i].setCaretColor(gray);

            // ---------- ボタン ----------
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
                currentHour[i] = startHour[i];
                currentMinute[i] = startMinute[i];
                currentSecond[i] = startSecond[i];
                currentTime[i] = startTime[i];

                hourField[i].setText(String.valueOf(currentHour[i]));
                minuteField[i].setText(String.valueOf(currentMinute[i]));
                secondField[i].setText(String.valueOf(currentSecond[i]));

                hourField[i].setEditable(true);
                minuteField[i].setEditable(true);
                secondField[i].setEditable(true);

                timer[i].stop();
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

                    alert();
                }
            }
        }
    }

    private void alert() {
        AudioInputStream ais = null;
        try {
            ais = AudioSystem.getAudioInputStream(new File("alarm.wav"));
            AudioFormat af = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, af);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(ais);
            clip.loop(0);
            clip.flush();
        } catch (UnsupportedAudioFileException ufe) {
            System.err.println(ufe);
        } catch (IOException ioe) {
            System.err.println(ioe);
        } catch (LineUnavailableException lue) {
            System.err.println(lue);
        } finally {
            try {
                ais.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}