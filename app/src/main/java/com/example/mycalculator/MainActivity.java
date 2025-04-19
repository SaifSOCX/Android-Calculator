package com.example.mycalculator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText display;
    private String currentInput = "";
    private String currentExpression = "";
    private boolean lastClickWasOperator = false;
    private boolean isRadians = true;
    private double memoryValue = 0;
    private boolean isSecondFunction = false;
    private DecimalFormat formatter = new DecimalFormat("#.##########");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set layout based on orientation
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main);
        } else {
            setContentView(R.layout.activity_main);
        }

        // Initialize display
        display = findViewById(R.id.display);
        display.setText("0");
        display.setFocusable(false);

        // Setup button click listeners
        setupButtons();

        // Restore state if available
        if (savedInstanceState != null) {
            currentInput = savedInstanceState.getString("currentInput", "");
            currentExpression = savedInstanceState.getString("currentExpression", "");
            lastClickWasOperator = savedInstanceState.getBoolean("lastClickWasOperator", false);
            isRadians = savedInstanceState.getBoolean("isRadians", true);
            memoryValue = savedInstanceState.getDouble("memoryValue", 0);
            display.setText(savedInstanceState.getString("display", "0"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("currentInput", currentInput);
        outState.putString("currentExpression", currentExpression);
        outState.putBoolean("lastClickWasOperator", lastClickWasOperator);
        outState.putBoolean("isRadians", isRadians);
        outState.putDouble("memoryValue", memoryValue);
        outState.putString("display", display.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void setupButtons() {
        // Numbers
        setupButtonClickListener(R.id.btn_0);
        setupButtonClickListener(R.id.btn_1);
        setupButtonClickListener(R.id.btn_2);
        setupButtonClickListener(R.id.btn_3);
        setupButtonClickListener(R.id.btn_4);
        setupButtonClickListener(R.id.btn_5);
        setupButtonClickListener(R.id.btn_6);
        setupButtonClickListener(R.id.btn_7);
        setupButtonClickListener(R.id.btn_8);
        setupButtonClickListener(R.id.btn_9);
        setupButtonClickListener(R.id.btn_decimal);

        // Basic operations
        setupButtonClickListener(R.id.btn_add);
        setupButtonClickListener(R.id.btn_subtract);
        setupButtonClickListener(R.id.btn_multiply);
        setupButtonClickListener(R.id.btn_divide);
        setupButtonClickListener(R.id.btn_equals);
        setupButtonClickListener(R.id.btn_percent);
        setupButtonClickListener(R.id.btn_sign);

        // Clear buttons
        setupButtonClickListener(R.id.btn_ac);

        // Memory operations
        setupButtonClickListener(R.id.btn_mc);
        setupButtonClickListener(R.id.btn_m_plus);
        if (findViewById(R.id.btn_m_minus) != null) {
            setupButtonClickListener(R.id.btn_m_minus);
        }
        setupButtonClickListener(R.id.btn_mr);

        // Parentheses
        setupButtonClickListener(R.id.btn_open_bracket);
        setupButtonClickListener(R.id.btn_close_bracket);

        // Advanced functions
        setupButtonClickListener(R.id.btn_second);
        setupButtonClickListener(R.id.btn_square);
        setupButtonClickListener(R.id.btn_cube);
        setupButtonClickListener(R.id.btn_power);
        setupButtonClickListener(R.id.btn_exp);
        setupButtonClickListener(R.id.btn_ten_power);
        setupButtonClickListener(R.id.btn_reciprocal);
        setupButtonClickListener(R.id.btn_sqrt);
        setupButtonClickListener(R.id.btn_cube_root);
        setupButtonClickListener(R.id.btn_y_root);
        setupButtonClickListener(R.id.btn_ln);
        setupButtonClickListener(R.id.btn_log);
        setupButtonClickListener(R.id.btn_factorial);
        setupButtonClickListener(R.id.btn_e);
        setupButtonClickListener(R.id.btn_ee);
        setupButtonClickListener(R.id.btn_pi);

        // Trigonometric functions
        setupButtonClickListener(R.id.btn_sin);
        setupButtonClickListener(R.id.btn_cos);
        setupButtonClickListener(R.id.btn_tan);
        setupButtonClickListener(R.id.btn_sinh);
        setupButtonClickListener(R.id.btn_cosh);
        setupButtonClickListener(R.id.btn_tanh);
        setupButtonClickListener(R.id.btn_rad);
        setupButtonClickListener(R.id.btn_rand);
    }

    private void setupButtonClickListener(int buttonId) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        // Handle number buttons
        if (id == R.id.btn_0 || id == R.id.btn_1 || id == R.id.btn_2 || id == R.id.btn_3 ||
                id == R.id.btn_4 || id == R.id.btn_5 || id == R.id.btn_6 || id == R.id.btn_7 ||
                id == R.id.btn_8 || id == R.id.btn_9) {
            handleNumberInput(((Button) view).getText().toString());
        }
        // Handle decimal
        else if (id == R.id.btn_decimal) {
            handleDecimalInput();
        }
        // Handle operators
        else if (id == R.id.btn_add || id == R.id.btn_subtract || id == R.id.btn_multiply || id == R.id.btn_divide) {
            handleOperatorInput(((Button) view).getText().toString());
        }
        // Handle equals
        else if (id == R.id.btn_equals) {
            calculateResult();
        }
        // Handle clear
        else if (id == R.id.btn_ac) {
            clearAll();
        }
        // Handle percentage
        else if (id == R.id.btn_percent) {
            handlePercentage();
        }
        // Handle sign change
        else if (id == R.id.btn_sign) {
            changeSign();
        }
        // Handle memory operations
        else if (id == R.id.btn_mc) {
            memoryValue = 0;
        }
        else if (id == R.id.btn_m_plus) {
            addToMemory();
        }
        else if (id == R.id.btn_m_minus && findViewById(R.id.btn_m_minus) != null) {
            subtractFromMemory();
        }
        else if (id == R.id.btn_mr) {
            recallMemory();
        }
        // Handle parentheses
        else if (id == R.id.btn_open_bracket) {
            appendToExpression("(");
            currentInput = "";
            updateDisplay();
        }
        else if (id == R.id.btn_close_bracket) {
            // First add currentInput to expression if needed
            if (!currentInput.isEmpty()) {
                appendToExpression(currentInput);
                currentInput = "";
            }
            appendToExpression(")");
            updateDisplay();
        }
        // Handle second function toggle
        else if (id == R.id.btn_second) {
            isSecondFunction = !isSecondFunction;
        }
        // Handle advanced functions
        else if (id == R.id.btn_square) {
            handleSquare();
        }
        else if (id == R.id.btn_cube) {
            handleCube();
        }
        else if (id == R.id.btn_power) {
            handlePower();
        }
        else if (id == R.id.btn_exp) {
            handleExponential();
        }
        else if (id == R.id.btn_ten_power) {
            handleTenPower();
        }
        else if (id == R.id.btn_reciprocal) {
            handleReciprocal();
        }
        else if (id == R.id.btn_sqrt) {
            handleSquareRoot();
        }
        else if (id == R.id.btn_cube_root) {
            handleCubeRoot();
        }
        else if (id == R.id.btn_y_root) {
            handleYRoot();
        }
        else if (id == R.id.btn_ln) {
            handleNaturalLog();
        }
        else if (id == R.id.btn_log) {
            handleLog10();
        }
        else if (id == R.id.btn_factorial) {
            handleFactorial();
        }
        else if (id == R.id.btn_sin) {
            handleSin();
        }
        else if (id == R.id.btn_cos) {
            handleCos();
        }
        else if (id == R.id.btn_tan) {
            handleTan();
        }
        else if (id == R.id.btn_sinh) {
            handleSinh();
        }
        else if (id == R.id.btn_cosh) {
            handleCosh();
        }
        else if (id == R.id.btn_tanh) {
            handleTanh();
        }
        else if (id == R.id.btn_pi) {
            handlePi();
        }
        else if (id == R.id.btn_e) {
            handleE();
        }
        else if (id == R.id.btn_ee) {
            handleScientificNotation();
        }
        else if (id == R.id.btn_rad) {
            toggleRadians();
        }
        else if (id == R.id.btn_rand) {
            handleRandom();
        }
    }

    private void handleNumberInput(String number) {
        if (display.getText().toString().equals("0") && !number.equals("0")) {
            currentInput = number;
        } else if (display.getText().toString().equals("0") && number.equals("0")) {
            currentInput = "0";
        } else {
            currentInput += number;
        }
        lastClickWasOperator = false;
        updateDisplay();
    }

    private void handleDecimalInput() {
        if (lastClickWasOperator || currentInput.isEmpty()) {
            currentInput = "0.";
        } else if (!currentInput.contains(".")) {
            currentInput += ".";
        }
        lastClickWasOperator = false;
        updateDisplay();
    }

    private void handleOperatorInput(String operator) {
        if (!currentInput.isEmpty()) {
            appendToExpression(currentInput);
            currentInput = "";
        } else if (lastClickWasOperator) {
            // Replace the last operator
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
        } else if (currentExpression.isEmpty()) {
            // If starting with operator, add 0 first
            appendToExpression("0");
        }

        appendToExpression(operator);
        lastClickWasOperator = true;
        updateDisplay();
    }

    private void appendToExpression(String value) {
        currentExpression += value;
    }

    private void calculateResult() {
        if (!currentInput.isEmpty()) {
            currentExpression += currentInput;
            currentInput = "";
        }

        if (!currentExpression.isEmpty()) {
            try {
                // Handle expression evaluation
                double result = evaluateExpression(currentExpression);

                // Format the result
                String resultStr = formatResult(result);

                // Update the display and reset the state
                display.setText(resultStr);
                currentInput = resultStr;
                currentExpression = "";
                lastClickWasOperator = false;
            } catch (Exception e) {
                display.setText("Error");
                currentInput = "";
                currentExpression = "";
            }
        }
    }

    private double evaluateExpression(String expression) {
        // Replace certain symbols for proper evaluation
        expression = expression.replace("×", "*")
                .replace("÷", "/")
                .replace("π", String.valueOf(Math.PI))
                .replace("e", String.valueOf(Math.E));

        try {
            // Check if it's a simple number first
            return Double.parseDouble(expression);
        } catch (NumberFormatException e) {
            // Continue with expression evaluation
        }

        // Handle expressions with ^ (power)
        expression = handlePowerOperators(expression);

        // Handle expressions with scientific notation (E)
        expression = handleScientificNotation(expression);

        // Evaluate the expression using a stack-based approach
        return evaluateInfixExpression(expression);
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
            return false;
        return true;
    }

    private double applyOperation(char operator, double b, double a) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0)
                    display.setText("infinity");
                //throw new ArithmeticException("Cannot divide by zero");
                return a / b;
        }
        return 0;
    }
    private double evaluateInfixExpression(String expression) {
        // This method evaluates an infix expression using the Shunting Yard algorithm
        try {
            // Parse the expression
            char[] tokens = expression.toCharArray();

            // Stack for numbers
            Stack<Double> values = new Stack<>();

            // Stack for operators
            Stack<Character> operators = new Stack<>();

            for (int i = 0; i < tokens.length; i++) {
                // Skip spaces
                if (tokens[i] == ' ')
                    continue;

                // If current token is a number, push it to the stack
                if (Character.isDigit(tokens[i]) || tokens[i] == '.') {
                    StringBuilder sb = new StringBuilder();

                    // Parse multi-digit numbers
                    while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                        sb.append(tokens[i++]);
                    }
                    i--; // Move back one position

                    values.push(Double.parseDouble(sb.toString()));
                }
                // If current token is an opening bracket, push it to the operator stack
                else if (tokens[i] == '(') {
                    operators.push(tokens[i]);
                }
                // If current token is a closing bracket, solve the bracket
                else if (tokens[i] == ')') {
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                    }

                    // Remove the opening bracket
                    if (!operators.isEmpty() && operators.peek() == '(')
                        operators.pop();
                }
                // If current token is an operator
                else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                    // Handle unary minus (negative numbers)
                    if (tokens[i] == '-' && (i == 0 || tokens[i-1] == '(' || tokens[i-1] == '+' ||
                            tokens[i-1] == '-' || tokens[i-1] == '*' || tokens[i-1] == '/')) {
                        values.push(0.0);
                    }

                    // While top of operator stack has higher or same precedence to current token,
                    // apply the operator at the top to the top two values in the values stack
                    while (!operators.isEmpty() && hasPrecedence(tokens[i], operators.peek())) {
                        values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                    }

                    // Push current token to operator stack
                    operators.push(tokens[i]);
                }
            }

            // Apply all remaining operators to the values
            while (!operators.isEmpty()) {
                values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
            }

            // The final result should be the only value in the stack
            return values.pop();
        } catch (Exception e) {
            throw new ArithmeticException("Invalid expression");
        }
    }

    private String handlePowerOperators(String expression) {
        // Replace a^b with Math.pow(a, b)
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)\\^(\\d+(\\.\\d+)?)");
        Matcher matcher = pattern.matcher(expression);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            double base = Double.parseDouble(matcher.group(1));
            double exponent = Double.parseDouble(matcher.group(3));
            double result = Math.pow(base, exponent);
            matcher.appendReplacement(sb, String.valueOf(result));
        }
        matcher.appendTail(sb);

        // Handle "yroot" operator
        pattern = Pattern.compile("(\\d+(\\.\\d+)?)yroot(\\d+(\\.\\d+)?)");
        matcher = pattern.matcher(sb.toString());
        sb = new StringBuffer();

        while (matcher.find()) {
            double x = Double.parseDouble(matcher.group(1));
            double y = Double.parseDouble(matcher.group(3));
            double result = Math.pow(x, 1.0 / y);
            matcher.appendReplacement(sb, String.valueOf(result));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }
    private String handleScientificNotation(String expression) {
        // Convert a E b to a * 10^b
        Pattern pattern = Pattern.compile("(\\d+(\\.\\d+)?)E([+-]?\\d+(\\.\\d+)?)");
        Matcher matcher = pattern.matcher(expression);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            double mantissa = Double.parseDouble(matcher.group(1));
            double exponent = Double.parseDouble(matcher.group(3));
            double result = mantissa * Math.pow(10, exponent);
            matcher.appendReplacement(sb, String.valueOf(result));
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.valueOf((long) result);
        } else {
            return formatter.format(result);
        }
    }

    private void clearAll() {
        currentInput = "";
        currentExpression = "";
        display.setText("0");
        lastClickWasOperator = false;
    }

    private void handlePercentage() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                value = value / 100;
                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void changeSign() {
        if (!currentInput.isEmpty()) {
            if (currentInput.startsWith("-")) {
                currentInput = currentInput.substring(1);
            } else {
                currentInput = "-" + currentInput;
            }
            updateDisplay();
        } else if (!currentExpression.isEmpty()) {
            calculateResult();
            changeSign();
        }
    }

    private void addToMemory() {
        try {
            if (!currentInput.isEmpty()) {
                memoryValue += Double.parseDouble(currentInput);
            } else if (!currentExpression.isEmpty()) {
                calculateResult();
                if (!currentInput.isEmpty()) {
                    memoryValue += Double.parseDouble(currentInput);
                }
            }
        } catch (NumberFormatException e) {
            // Ignore invalid input
        }
    }

    private void subtractFromMemory() {
        try {
            if (!currentInput.isEmpty()) {
                memoryValue -= Double.parseDouble(currentInput);
            } else if (!currentExpression.isEmpty()) {
                calculateResult();
                if (!currentInput.isEmpty()) {
                    memoryValue -= Double.parseDouble(currentInput);
                }
            }
        } catch (NumberFormatException e) {
            // Ignore invalid input
        }
    }

    private void recallMemory() {
        currentInput = formatResult(memoryValue);
        updateDisplay();
    }

    private void handleSquare() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                value = value * value;
                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleCube() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                value = value * value * value;
                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handlePower() {
        if (!currentInput.isEmpty()) {
            appendToExpression(currentInput + "^");
            currentInput = "";
            updateDisplay();
        }
    }

    private void handleExponential() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                value = Math.exp(value);
                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleTenPower() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                value = Math.pow(10, value);
                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleReciprocal() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                if (value != 0) {
                    value = 1 / value;
                    currentInput = formatResult(value);
                    updateDisplay();
                } else {
                    display.setText("Error");
                }
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleSquareRoot() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                if (value >= 0) {
                    value = Math.sqrt(value);
                    currentInput = formatResult(value);
                    updateDisplay();
                } else {
                    display.setText("Error");
                }
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleCubeRoot() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                value = Math.cbrt(value);
                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleYRoot() {
        if (!currentInput.isEmpty()) {
            appendToExpression(currentInput + "yroot");
            currentInput = "";
            updateDisplay();
        }
    }

    private void handleNaturalLog() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                if (value > 0) {
                    value = Math.log(value);
                    currentInput = formatResult(value);
                    updateDisplay();
                } else {
                    display.setText("Error");
                }
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleLog10() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                if (value > 0) {
                    value = Math.log10(value);
                    currentInput = formatResult(value);
                    updateDisplay();
                } else {
                    display.setText("Error");
                }
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleFactorial() {
        if (!currentInput.isEmpty()) {
            try {
                double inputValue = Double.parseDouble(currentInput);
                if (inputValue >= 0 && inputValue == (int)inputValue && inputValue <= 170) {
                    long n = (long) inputValue;
                    double result = 1;
                    for (int i = 2; i <= n; i++) {
                        result *= i;
                    }
                    currentInput = formatResult(result);
                    updateDisplay();
                } else {
                    display.setText("Error");
                }
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleSin() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);

                // Handle radians/degrees conversion
                if (!isRadians) {
                    value = Math.toRadians(value);
                }

                if (isSecondFunction) {
                    // Handle arcsin (inverse sine)
                    if (value < -1 || value > 1) {
                        display.setText("Error: Domain [-1,1]");
                        currentInput = "";
                        return;
                    }
                    value = Math.asin(value);
                    if (!isRadians) {
                        value = Math.toDegrees(value);
                    }
                } else {
                    // Handle regular sine
                    value = Math.sin(value);
                }

                // Format and display the result
                currentInput = formatResult(value);
                updateDisplay();

            } catch (NumberFormatException e) {
                display.setText("Error");
                currentInput = "";
            }
        }
    }

    private void handleCos() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                if (!isRadians) {
                    value = Math.toRadians(value);
                }

                if (isSecondFunction) {
                    // arccos
                    if (value >= -1 && value <= 1) {
                        value = Math.acos(value);
                        if (!isRadians) {
                            value = Math.toDegrees(value);
                        }
                    } else {
                        display.setText("Error");
                        return;
                    }
                } else {
                    value = Math.cos(value);
                }

                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleTan() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                if (!isRadians) {
                    value = Math.toRadians(value);
                }

                if (isSecondFunction) {
                    // arctan
                    value = Math.atan(value);
                    if (!isRadians) {
                        value = Math.toDegrees(value);
                    }
                } else {
                    // Check for tan(90°) and other invalid values
                    if (Math.abs(Math.cos(value)) < 1e-10) {
                        display.setText("Error");
                        return;
                    }
                    value = Math.tan(value);
                }

                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleSinh() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                if (isSecondFunction) {
                    // arcsinh
                    value = Math.log(value + Math.sqrt(value * value + 1));
                } else {
                    value = Math.sinh(value);
                }
                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleCosh() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                if (isSecondFunction) {
                    // arccosh
                    if (value >= 1) {
                        value = Math.log(value + Math.sqrt(value * value - 1));
                    } else {
                        display.setText("Error");
                        return;
                    }
                } else {
                    value = Math.cosh(value);
                }
                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handleTanh() {
        if (!currentInput.isEmpty()) {
            try {
                double value = Double.parseDouble(currentInput);
                if (isSecondFunction) {
                    // arctanh
                    if (value > -1 && value < 1) {
                        value = 0.5 * Math.log((1 + value) / (1 - value));
                    } else {
                        display.setText("Error");
                        return;
                    }
                } else {
                    value = Math.tanh(value);
                }
                currentInput = formatResult(value);
                updateDisplay();
            } catch (NumberFormatException e) {
                display.setText("Error");
            }
        }
    }

    private void handlePi() {
        currentInput = String.valueOf(Math.PI);
        updateDisplay();
    }

    private void handleE() {
        currentInput = String.valueOf(Math.E);
        updateDisplay();
    }

    private void handleScientificNotation() {
        if (!currentInput.isEmpty()) {
            currentInput += "E";
            updateDisplay();
        }
    }

    private void toggleRadians() {
        isRadians = !isRadians;
        Button radButton = findViewById(R.id.btn_rad);
        if (radButton != null) {
            radButton.setText(isRadians ? "Rad" : "Deg");
        }
    }

    private void handleRandom() {
        currentInput = formatResult(Math.random());
        updateDisplay();
    }

    private void updateDisplay() {
        if (!currentInput.isEmpty()) {
            display.setText(currentInput);
        } else if (!currentExpression.isEmpty()) {
            display.setText(currentExpression);
        } else {
            display.setText("0");
        }
    }

}