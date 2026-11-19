import java.util.Scanner;

public class CalculatorCLI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Kalkulatori ===");
        System.out.println("Shkruaj p.sh.: 5 + 3   (operatorët: + - * / %)");
        System.out.println("Shkruaj 'exit' për të dalë.\n");

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Mirupafshim!");
                break;
            }

            String[] parts = line.split("\\s+");
            if (parts.length != 3) {
                System.out.println("Format i gabuar. Shembull: 5 + 3");
                continue;
            }

            try {
                double a = Double.parseDouble(parts[0]);
                String op = parts[1];
                double b = Double.parseDouble(parts[2]);
                double result;

                switch (op) {
                    case "+": result = a + b; break;
                    case "-": result = a - b; break;
                    case "*": result = a * b; break;
                    case "/":
                        if (b == 0) {
                            System.out.println("Gabim: pjesëtim me 0");
                            continue;
                        }
                        result = a / b;
                        break;
                    case "%":
                        if (b == 0) {
                            System.out.println("Gabim: pjesëtim me 0");
                            continue;
                        }
                        result = a % b;
                        break;
                    default:
                        System.out.println("Operator i panjohur: " + op);
                        continue;
                }

                if (result == (long) result) {
                    System.out.println("= " + (long) result);
                } else {
                    System.out.println("= " + result);
                }
            } catch (NumberFormatException e) {
                System.out.println("Numër i pavlefshëm.");
            }
        }
        sc.close();
    }
}
