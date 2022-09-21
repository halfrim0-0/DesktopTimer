import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

public class DesktopTimer extends JFrame implements ActionListener {
    // ---------- GUI ----------
    private static JPanel mainPanel;
    private static JPanel[] panel;
    private static DefaultComboBoxModel[] model;
    private static JComboBox<String>[] comboBox;
    private static Map<String, Pair<Integer, Integer>> enseiMap;
    private static TitledBorder hourBorder, minuteBorder, secondBorder;
    private static JTextField[] hourField, minuteField, secondField;
    private static JButton[] selectButton, startButton, stopButton, resetButton;

    private final static Color gray = new Color(204, 204, 204);
    private final static Color blue = new Color(0, 25, 101);
    private final static Color deepBlue = new Color(0, 12, 50);

    // ---------- タイマー ----------
    private static int[] startTime, startHour, startMinute, startSecond;
    private static int[] currentTime, currentHour, currentMinute, currentSecond;
    private static boolean[] hasStopped;
    private static Timer[] timer;
    final private static int n = 3;

    public static void main(String[] args) {
        init();
        DesktopTimer desktopTimer = new DesktopTimer();
        desktopTimer.decorateFrame();
    }

    public DesktopTimer() {
        mainPanel = new JPanel();
        decorateMainPanel();
        createEnseiMap();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object pushedButton = e.getSource();
        for (int i = 0; i < n; i++) {
            if (pushedButton == selectButton[i]) {
                select(i);
            } else if (pushedButton == startButton[i]) {
                start(i);
            } else if (pushedButton == stopButton[i]) {
                stop(i);
            } else if (pushedButton == resetButton[i]) {
                reset(i);
            } else if (e.getSource() == timer[i]) {
                run(i);
            }
        }
    }

    private static void init() {
        panel = new JPanel[n];

        model = new DefaultComboBoxModel[n];
        comboBox = new JComboBox[n];
        enseiMap = new HashMap<String, Pair<Integer, Integer>>();

        hourField = new JTextField[n];
        minuteField = new JTextField[n];
        secondField = new JTextField[n];

        selectButton = new JButton[n];
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

        hasStopped = new boolean[n];

        timer = new Timer[n];
    }

    private void decorateFrame() {
        this.setSize(650, 250);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Desktop Timer");
        this.setVisible(true);

        Container contentPane = this.getContentPane();
        contentPane.add(mainPanel);
    }

    private void decorateMainPanel() {
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        createBorder();

        for (int i = 0; i < n; i++) {
            createModel(i);
            createComboBox(i);
            createField(i);
            createButton(i);
            addToPanel(i);

            hasStopped[i] = false;

            timer[i] = new Timer(1000, this);

            mainPanel.add(panel[i]);
        }
    }

    private void createEnseiMap() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(Paths.get("ensei.json").toFile());

            int num = json.get("num").intValue();
            for (int i = 0; i < num; i ++) {
                String name = json.get("list").get(i).get("name").textValue();
                int hour = Integer.parseInt(json.get("list").get(i).get("hour").textValue());
                int minute = Integer.parseInt(json.get("list").get(i).get("minute").textValue());

                enseiMap.put(name, Pair.of(hour, minute));
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void createComboBox(int index) {
        comboBox[index] = new JComboBox<>(model[index]);
    }

    private void createModel(int index) {
        model[index] = new DefaultComboBoxModel();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(Paths.get("ensei.json").toFile());

            int num = json.get("num").intValue();
            for (int i = 0; i < num; i ++) {
                String name = json.get("list").get(i).get("name").textValue();
                model[index].addElement(name);
            }
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void addToPanel(int index) {
        panel[index] = new JPanel();
        panel[index].setBackground(blue);

        panel[index].add(comboBox[index]);
        panel[index].add(selectButton[index]);
        panel[index].add(hourField[index]);
        panel[index].add(minuteField[index]);
        panel[index].add(secondField[index]);
        panel[index].add(startButton[index]);
        panel[index].add(stopButton[index]);
        panel[index].add(resetButton[index]);
    }

    private void createBorder() {
        hourBorder = new TitledBorder("hour");
        decorateBorder(hourBorder);

        minuteBorder = new TitledBorder("min");
        decorateBorder(minuteBorder);

        secondBorder = new TitledBorder("sec");
        decorateBorder(secondBorder);
    }

    private void decorateBorder(TitledBorder border) {
        border.setTitleColor(gray);
        border.setTitleColor(gray);
        border.setTitleFont(new Font(Font.SERIF, Font.PLAIN, 12));
    }

    private void createField(int index) {
        hourField[index] = new JTextField();
        decorateField(hourField[index], hourBorder);

        minuteField[index] = new JTextField();
        decorateField(minuteField[index], minuteBorder);

        secondField[index] = new JTextField();
        decorateField(secondField[index], secondBorder);
    }

    private void decorateField(JTextField textField, TitledBorder border) {
        textField.setBorder(border);
        textField.setPreferredSize(new Dimension(50, 50));
        textField.setBackground(deepBlue);

        // 文字に関する設定
        textField.setForeground(gray);
        textField.setFont(new Font(Font.SERIF, Font.PLAIN, 30));
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setCaretColor(gray);
    }

    private void createButton(int index) {
        selectButton[index] = new JButton("select");
        selectButton[index].addActionListener(this);

        startButton[index] = new JButton("start");
        startButton[index].addActionListener(this);

        stopButton[index] = new JButton("stop");
        stopButton[index].addActionListener(this);

        resetButton[index] = new JButton("reset");
        resetButton[index].addActionListener(this);
    }

    private void select(int index) {
        String name = (String)comboBox[index].getSelectedItem();
        int hour = enseiMap.get(name).getLeft();
        int minute = enseiMap.get(name).getRight();

        System.out.println(hour);
        System.out.println(minute);

        currentHour[index] = hour;
        currentMinute[index] = minute;
        currentSecond[index] = 0;
        setStartTime(index);
        setCurrentTimeToField(index);
    }

    private void start(int index) {
        if (!isProper(index)) {
            return;
        }

        setCurrentTime(index);
        if (!hasStopped[index]) {
            setStartTime(index);
        }
        setEnable(index, false);

        timer[index].start();
    }

    private void stop(int index) {
        setEnable(index,true);

        hasStopped[index] = true;
        timer[index].stop();
    }

    private void reset(int index) {
        resetCurrentTime(index);
        formatTime(index);
        setCurrentTimeToField(index);
        setEnable(index, true);

        hasStopped[index] = false;
        timer[index].stop();
    }

    private void run(int index) {
        currentTime[index]--;
        formatTime(index);
        setCurrentTimeToField(index);

        if (currentTime[index] == 0) {
            timer[index].stop();
            setEnable(index, true);
            alert();
        }
    }

    private boolean isProper(int index) {
        if (hourField[index].getText().equals("") || minuteField[index].getText().equals("") || secondField[index].getText().equals("")) {
            return false;
        } else if (hourField[index].getText().equals("0") && minuteField[index].getText().equals("0") && secondField[index].getText().equals("0")) {
            return false;
        }

        return true;
    }

    private void formatTime(int index) {
        currentHour[index] = currentTime[index] / 3600;
        currentTime[index] %= 3600;
        currentMinute[index] = currentTime[index] / 60;
        currentTime[index] %= 60;
        currentSecond[index] = currentTime[index];
        currentTime[index] = currentHour[index] * 3600 + currentMinute[index] * 60 + currentSecond[index];
    }

    private void setStartTime(int index) {
        startHour[index] = currentHour[index];
        startMinute[index] = currentMinute[index];
        startSecond[index] = currentSecond[index];
        startTime[index] = currentTime[index];
    }

    private void setCurrentTime(int index) {
        currentHour[index] = Integer.parseInt(hourField[index].getText());
        currentMinute[index] = Integer.parseInt(minuteField[index].getText());
        currentSecond[index] = Integer.parseInt(secondField[index].getText());
        currentTime[index] = currentHour[index] * 3600 + currentMinute[index] * 60 + currentSecond[index];
    }

    private void resetCurrentTime(int index) {
        currentHour[index] = startHour[index];
        currentMinute[index] = startMinute[index];
        currentSecond[index] = startSecond[index];
        currentTime[index] = startTime[index];
    }

    private void setCurrentTimeToField(int index) {
        hourField[index].setText(String.valueOf(currentHour[index]));
        minuteField[index].setText(String.valueOf(currentMinute[index]));
        secondField[index].setText(String.valueOf(currentSecond[index]));
    }

    private void setEnable(int index, boolean isEnable) {
        comboBox[index].setEnabled(isEnable);
        selectButton[index].setEnabled(isEnable);
        hourField[index].setEditable(isEnable);
        minuteField[index].setEditable(isEnable);
        secondField[index].setEditable(isEnable);
        startButton[index].setEnabled(isEnable);
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