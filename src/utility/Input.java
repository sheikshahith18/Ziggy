package utility;

import java.util.Scanner;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;

public class Input {
	private static Scanner sc = new Scanner(System.in);
	private static boolean newLineCharacter = false;
	private static int attempt = 1;

	public static int getInteger(boolean limit) {
		if (attempt++ > 3 && limit) {
			System.out.print(Message.TOO_MANY_ATTEMPTS);
			if (newLineCharacter) {
				sc.nextLine();
				newLineCharacter = false;
			}
			sc.nextLine();
			attempt = 1;
			return -1;
		}
		int input;
		try {
			input = sc.nextInt();
			newLineCharacter = true;
			if (input < 0)
				throw new MyException("Invalid data entered...");
			if (input > Integer.MAX_VALUE)
				throw new MyException("Enter Valid Option : ");
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.out.print(Message.NUMBERS_ONLY);
			input = getInteger(limit);
		} catch (MyException e) {
			System.out.println(e.getMessage());
			input = getInteger(limit);
		}
		attempt = 1;
		return input;
	}

	public static int getInteger(int from, int to, boolean limit) {
		if (attempt++ > 3 && limit) {
			System.out.print(Message.TOO_MANY_ATTEMPTS);
			if (newLineCharacter) {
				sc.nextLine();
				newLineCharacter = false;
			}
			sc.nextLine();
			attempt = 1;
			return -1;
		}
		int input;
		try {
			input = sc.nextInt();
			newLineCharacter = true;
			if (input < from || input > to) {
				throw new MyException("Input's not in Range (" + from + " - " + to + ")");
			}
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.out.print(Message.NUMBERS_ONLY);
			input = getInteger(from, to, limit);
		} catch (MyException e) {
			System.out.println(e.getMessage());
			System.out.print(Message.ENTER_AGAIN);
			input = getInteger(from, to, limit);
		}
		attempt = 1;
		return input;
	}

	public static double getDouble(boolean limit) {
		if (attempt++ > 3 && limit) {
			System.out.print(Message.TOO_MANY_ATTEMPTS);
			if (newLineCharacter) {
				sc.nextLine();
				newLineCharacter = false;
			}
			sc.nextLine();
			attempt = 1;
			return -1;
		}
		double input;
		try {
			input = sc.nextDouble();
			newLineCharacter = true;
			if (input < 0)
				throw new MyException("Invalid data entered...");
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.out.print(Message.NUMBERS_ONLY);
			input = getDouble(limit);
		} catch (MyException e) {
			System.out.println(e.getMessage());
			input = getDouble(limit);
		}
		attempt = 1;
		return input;
	}

	public static String getString(boolean check, boolean limit) {
		if (attempt++ > 3 && limit) {
			System.out.print(Message.TOO_MANY_ATTEMPTS);
			sc.nextLine();
			attempt = 1;
			return null;
		}
		String input;
		if (newLineCharacter) {
			sc.nextLine();
			newLineCharacter = false;
		}
		input = sc.nextLine();
		if (!check || input.matches("^[a-z A-Z]*$")) {
			if (input == "") {
				System.out.println("\n"+Message.EMPTY_FIELD);
				System.out.print(Message.ENTER_AGAIN);
				input = getString(check, limit);
			}
			attempt = 1;
			return input;
		}

		System.out.print(Message.CHARACTERS_ONLY);
		input = getString(check, limit);

		if (input == null)
			return null;

		attempt = 1;
		if (input.equals(""))
			input = getString(check, limit);
		return input;

	}

	public static String getDateTime() {
		GregorianCalendar g = new GregorianCalendar();
		String s = g.get(Calendar.DATE) + "/" + (g.get(Calendar.MONTH) + 1) + "\t" + g.get(Calendar.HOUR_OF_DAY) + ":"
				+ g.get(Calendar.MINUTE);
		return s;
	}

}

class MyException extends Exception {
	MyException(String message) {
		super(message);
	}
}
