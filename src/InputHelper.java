import java.util.Scanner;

public class InputHelper {
    private static final Scanner sc = new Scanner(System.in);

    public static String getString(String label) {
        System.out.print(label + ": ");
        return sc.nextLine();
    }

    public static int getInt(String label) {
        System.out.print("Enter " + label + ": ");
        while (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.print("Invalid input. Enter " + label + " again: ");
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    public static long getLong(String label) {
        System.out.print("Enter " + label + ": ");
        while (!sc.hasNextLong()) {
            sc.nextLine();
            System.out.print("Invalid input. Enter " + label + " again: ");
        }
        long value = sc.nextLong();
        sc.nextLine();
        return value;
    }

    public static boolean getBoolean(String label) {
        System.out.print(label);
        while (!sc.hasNextBoolean()) {
            sc.nextLine();
            System.out.print("Invalid input. " + label);
        }
        boolean value = sc.nextBoolean();
        sc.nextLine();
        return value;
    }
}
