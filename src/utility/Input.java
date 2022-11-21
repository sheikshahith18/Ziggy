package utility;

import java.util.Scanner;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;

public class Input {
	
	private static final Scanner SCANNER = new Scanner(System.in);
	private static boolean newLineCharacter = false;
	private static int attempt = 1;

	public static boolean isAttemptExceeded(boolean limit, int attempts) {
		if (limit && attempts > 3) {
			attempt = 1;
			System.out.println("\nToo many unsuccessful attempts...Try again later...\n");
			return true;
		}
		return false;
	}

	private static void printMessage(boolean limit, int attempts, String message) {
		if (!limit || attempts <= 3)
			System.out.print(message + " : ");
	}

	public static int getInteger(boolean limit) {
		
		if (isAttemptExceeded(limit, attempt++))
			return -1;

		int input;
		try {
			input = SCANNER.nextInt();
			newLineCharacter = true;

			if (input < 0) {
				System.out.println("Invalid data entered...");
				printMessage(limit, attempt, "Enter again");
				return getInteger(limit);
			}

		} catch (InputMismatchException e) {
			SCANNER.nextLine();
			printMessage(limit, attempt, "Enter numbers only");

			return getInteger(limit);
		}

		attempt = 1;
		return input;
	}

	public static int getInteger(int from, int to, boolean limit) {
		if (isAttemptExceeded(limit, attempt++))
			return -1;

		int input;
		try {
			input = SCANNER.nextInt();
			newLineCharacter = true;
			if (input < from || input > to) {
				System.out.println("Input's not in Range (" + from + " - " + to + ")");
				printMessage(limit, attempt, "Enter again");

				return getInteger(from, to, limit);
			}

		} catch (InputMismatchException e) {
			SCANNER.nextLine();
			printMessage(limit, attempt, "Enter numbers only (" + from + " - " + to + ")");

			return getInteger(from, to, limit);
		}

		attempt = 1;
		return input;
	}

	public static double getDouble(boolean limit) {

		if (isAttemptExceeded(limit, attempt++))
			return -1;

		double input;
		try {
			input = SCANNER.nextDouble();
			newLineCharacter = true;
			
			if (input < 0) {
				System.out.println("Invalid data entered...");
				printMessage(limit, attempt, "Enter again");
				return getDouble(limit);
			}

		} catch (InputMismatchException e) {
			SCANNER.nextLine();
			printMessage(limit, attempt, "Enter numbers only");

			return getDouble(limit);
		}

		attempt = 1;
		return input;
	}

	public static String getString(boolean isAlphabetOnly, boolean limit) {
		
		if (isAttemptExceeded(limit, attempt++))
			return "";

		String input;
		if (newLineCharacter) {
			SCANNER.nextLine();
			newLineCharacter = false;
		}

		input = SCANNER.nextLine();

		if (input.equals("")) {
			System.out.println("\nThis field can't be empty");
			printMessage(limit, attempt, "Enter again");

			input = getString(isAlphabetOnly, limit);
		}

		if (!isAlphabetOnly || input.matches("^[a-z A-Z]*$")) {
			attempt = 1;
			return input;
		}

		printMessage(limit, attempt, "Enter alphabets only");

		return getString(isAlphabetOnly, limit);

	}

	public static String getDateTime() {
		GregorianCalendar g = new GregorianCalendar();
		return g.get(Calendar.DATE) + "/" + (g.get(Calendar.MONTH) + 1) + "\t" + g.get(Calendar.HOUR_OF_DAY) + ":"
				+ g.get(Calendar.MINUTE);
	}

}
