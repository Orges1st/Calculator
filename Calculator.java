import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Calculator extends JFrame {
    private final JTextField display = new JTextField("0");
    private double firstNumber = 0;
    private String operator = "";
    private boolean startNewNumber = true;

    public Calculator() {
        setTitle("Kalkulatori");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(320, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setPreferredSize(new Dimension(300, 70));
        add(display, BorderLayout.NORTH);

        String[] labels = {
            "C", "⌫", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "±", "0", ".", "="
        };

        JPanel panel = new JPanel(new GridLayout(5, 4, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        for (String label : labels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 22));
            button.addActionListener(this::handleClick);
            panel.add(button);
        }
        add(panel, BorderLayout.CENTER);
    }

    private void handleClick(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.matches("[0-9]")) {
            if (startNewNumber || display.getText().equals("0")) {
                display.setText(cmd);
                startNewNumber = false;
            } else {
                display.setText(display.getText() + cmd);
            }
        } else if (cmd.equals(".")) {
            if (startNewNumber) {
                display.setText("0.");
                startNewNumber = false;
            } else if (!display.getText().contains(".")) {
                display.setText(display.getText() + ".");
            }
        } else if (cmd.equals("C")) {
            display.setText("0");
            firstNumber = 0;
            operator = "";
            startNewNumber = true;
        } else if (cmd.equals("⌫")) {
            String text = display.getText();
            if (!startNewNumber && text.length() > 1) {
                display.setText(text.substring(0, text.length() - 1));
            } else {
                display.setText("0");
                startNewNumber = true;
            }
        } else if (cmd.equals("±")) {
            double value = parse(display.getText());
            display.setText(format(-value));
        } else if (cmd.equals("%")) {
            double value = parse(display.getText());
            display.setText(format(value / 100));
        } else if (cmd.equals("=")) {
            if (!operator.isEmpty()) {
                calculate();
                operator = "";
                startNewNumber = true;
            }
        } else { // + - * /
            if (!operator.isEmpty() && !startNewNumber) {
                calculate();
            }
            firstNumber = parse(display.getText());
            operator = cmd;
            startNewNumber = true;
        }
    }

    private void calculate() {
        double second = parse(display.getText());
        double result = 0;
        switch (operator) {
            case "+": result = firstNumber + second; break;
            case "-": result = firstNumber - second; break;
            case "*": result = firstNumber * second; break;
            case "/":
                if (second == 0) {
                    display.setText("Gabim: pjesëtim me 0");
                    operator = "";
                    startNewNumber = true;
                    return;
                }
                result = firstNumber / second;
                break;
        }
        display.setText(format(result));
        firstNumber = result;
    }

    private double parse(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private String format(double value) {
        if (value == (long) value) return String.valueOf((long) value);
        return String.valueOf(value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator().setVisible(true));
    }
}