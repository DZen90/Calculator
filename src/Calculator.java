import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Zigfrid on 11/22/2014.
 */
public class Calculator extends JFrame{

    private enum Operation {
        Addition, Subtraction, Multiplication, Division, None
    }
    private  Operation operation = Operation.None;
    private JPanel mainPanel, numPanel, opPanel;
    private JButton[] numbers, operations;
    private JFormattedTextField field;
    private boolean pointPressed = false;
    private boolean clearText = true;
    private double arg = 0;
    private double result = 0;
    private double checker = 0;

    public Calculator(String name) {

        // Create frame
        super(name);
        setSize(300, 200);
        setLocation(400, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create layout
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.add(mainPanel);
        field = new JFormattedTextField(java.text.NumberFormat.getInstance());
        field.setText("0");
        mainPanel.add(BorderLayout.NORTH, field);
        numPanel = new JPanel();
        GridLayout grid1 = new GridLayout(4, 3);
        grid1.setVgap(2);
        grid1.setHgap(2);
        numPanel.setLayout(grid1);
        opPanel = new JPanel();
        GridLayout grid2 = new GridLayout(4, 3);
        grid2.setVgap(2);
        grid2.setHgap(2);
        opPanel.setLayout(grid2);

        mainPanel.add(BorderLayout.WEST, numPanel);
        mainPanel.add(BorderLayout.EAST, opPanel);

        // Create and add buttons for numbers:
        numbers = new JButton[12];
        for (int i = 0; i < 9; i++) {
            numbers[i] = new JButton("" + (i+1));
        }
        numbers[9] = new JButton(".");
        numbers[10] = new JButton("0");
        numbers[11] = new JButton("+/-");
        for (int i = 0; i < 12; i++) {
            numPanel.add(numbers[i]);
            numbers[i].addActionListener(new NumberListener());
        }

        // Create and add buttons for operations:
        operations = new JButton[8];
        operations[0] = new JButton("+");
        operations[1] = new JButton("-");
        operations[2] = new JButton("*");
        operations[3] = new JButton("/");
        operations[4] = new JButton("=");
        operations[5] = new JButton("C");
        operations[6] = new JButton("sqrt");
        operations[7] = new JButton("1/x");
        for (int i = 0; i < 8; i++) {
            opPanel.add(operations[i]);
            operations[i].addActionListener(new OperationListener());
        }
        // Disable inappropriate operations
        checker = Double.parseDouble(field.getText());
        if (checker < 0) {
            operations[6].setEnabled(false);
        } else {
            operations[6].setEnabled(true);
        }
        if (checker == 0) {
            operations[7].setEnabled(false);
        } else {
            operations[7].setEnabled(true);
        }


    }

    public static void main(String[] args) {
        Calculator calc = new Calculator("Calculator");
        calc.setVisible(true);
    }

    // Listener for number buttons
    public class NumberListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            String text = field.getText();

            // Check if the . button was pressed before
            if (((JButton)e.getSource()).getText() == "." && pointPressed) {
                return;
            }

            if (((JButton)e.getSource()).getText() == "+/-") {
                arg = Double.parseDouble(text);
                arg *= -1;
                text = String.valueOf(arg);
            } else {
                // Clear text field if its value is a result
                if (clearText) {
                    field.setText("");
                    clearText = false;
                    pointPressed = false;
                    text = "";
                }

                if (((JButton)e.getSource()).getText() == ".") {
                    pointPressed = true;
                }
                text += ((JButton)e.getSource()).getText();
            }
            // Check if the number in textfield starts with 0
            if (text.startsWith("0") && text.length()>1 && !text.startsWith("0.")) {
                text = text.substring(1);
            }
            field.setText(text);


            // Disable inappropriate operations
            checker = Double.parseDouble(text);
            if (checker < 0) {
                operations[6].setEnabled(false);
            } else {
                operations[6].setEnabled(true);
            }
            if (checker == 0) {
                operations[7].setEnabled(false);
            } else {
                operations[7].setEnabled(true);
            }
            if (checker == 0 && operation == Operation.Division) {
                for (int i = 0; i < 5; i++) {
                    operations[i].setEnabled(false);
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    operations[i].setEnabled(true);
                }
            }
        }
    }

    // Listener for operation buttons
    public class OperationListener implements  ActionListener {

        public void actionPerformed(ActionEvent e) {

            // Get operation
            String currentOperation = ((JButton)e.getSource()).getText();

            // Get input from text field
            arg = Double.parseDouble(field.getText());

            // Perform operation
            switch(operation) {
                case Addition:
                    result += arg;
                    break;
                case Subtraction:
                    result -= arg;
                    break;
                case Multiplication:
                    result *= arg;
                    break;
                case Division:
                    result /= arg;
                    break;
                case None:
                    result = arg;
                    break;
            }

            // Output current result into text field
            field.setText(String.valueOf(result));

            // Disable inappropriate operations
            if (result < 0) {
                operations[6].setEnabled(false);
            } else {
                operations[6].setEnabled(true);
            }
            if (result == 0) {
                operations[7].setEnabled(false);
            } else {
                operations[7].setEnabled(true);
            }

            clearText = true;

            if (currentOperation == "+") {
                operation = Operation.Addition;
            } else if (currentOperation == "-") {
                operation = Operation.Subtraction;
            } else if (currentOperation == "*") {
                operation = Operation.Multiplication;
            } else if (currentOperation == "/") {
                operation = Operation.Division;
            } else if (currentOperation == "C") {
                operation = Operation.None;
                field.setText("0");
                arg = 0;
                result = 0;
            } else if (currentOperation == "sqrt") {
                operation = Operation.None;
                if (result >= 0) {
                    result = Math.sqrt(result);
                    arg = result;
                    field.setText(String.valueOf(result));
                }
            } else if (currentOperation == "1/x") {
                operation = Operation.None;
                if (result != 0) {
                    result = 1 / result;
                    arg = result;
                    field.setText(String.valueOf(result));
                }
            } else {
                operation = Operation.None;
            }
        }
    }

}
