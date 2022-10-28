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
			if (attempt > 2)
				System.out.print(Message.ENTER_AGAIN);
			input = sc.nextInt();
			newLineCharacter = true;
			if (input < 0)
				throw new MyException("Invalid data entered...");
			if (input > Integer.MAX_VALUE)
				throw new MyException("Enter Valid Option");
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.out.println(Message.NUMBERS_ONLY);
			input = getInteger(limit);
		} catch (MyException e) {
			System.out.println(e.getMessage());
			input = getInteger(limit);
		}
		attempt = 1;
		return input;
	}

	public static int getInteger(int from, int to) {
		if (attempt++ > 3) {
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
			if (attempt > 2)
				System.out.print(Message.ENTER_AGAIN);
			input = sc.nextInt();
			newLineCharacter = true;
			if (input < from || input > to) {
				throw new MyException("Input's not in Range (" + from + " - " + to + ")");
			}
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.out.println(Message.NUMBERS_ONLY);
			input = getInteger(from, to);
		} catch (MyException e) {
			System.out.println(e.getMessage());
			input = getInteger(from, to);
		}
		attempt = 1;
		return input;
	}

	public static byte getByte() {
		if (attempt++ > 3) {
			System.out.print(Message.TOO_MANY_ATTEMPTS);
			sc.nextLine();
			attempt = 1;
			return -1;
		}
		byte input;
		try {
			if (attempt > 2)
				System.out.print(Message.ENTER_AGAIN);
			input = sc.nextByte();
			newLineCharacter = true;
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.out.println(Message.NUMBERS_ONLY);
			input = getByte();
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
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.out.println(Message.NUMBERS_ONLY);
			input = getDouble(limit);
		}
		attempt = 1;
		return input;
	}

	public static String getString(boolean check) {
		if (attempt++ > 3) {
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
		if (attempt > 2)
			System.out.print(Message.ENTER_AGAIN);
		input = sc.nextLine();
		if (!check || input.matches("^[a-z A-Z]*$")) {
			if (input == "") {
				System.out.println(Message.EMPTY_FIELD);
				input = getString(check);
			}
			attempt = 1;
			return input;
		}

		System.out.println(Message.CHARACTERS_ONLY);
		input = getString(check);

		if (input == null)
			return null;

		attempt = 1;
		if (input.equals(""))
			input = getString(check);
		return input;

	}

	public static long getPhoneNumber() {
		if (attempt++ > 3) {
			System.out.print(Message.TOO_MANY_ATTEMPTS);
			sc.nextLine();
			attempt = 1;
			return -1;
		}
		long input;
		newLineCharacter = true;
		try {
			if (attempt > 2)
				System.out.print(Message.ENTER_AGAIN);
			input = sc.nextLong();
			if (input < 6000000000l || input > 9999999999l)
				throw new MyException("Invalid Phone Number...");
		} catch (InputMismatchException e) {
			sc.nextLine();
			System.out.println(Message.VALID_PHONE_NO);
			input = getPhoneNumber();
		} catch (MyException e) {
			System.out.println(e.getMessage());
			System.out.println(Message.VALID_PHONE_NO);
			input = getPhoneNumber();
		}
		attempt = 1;
		return input;
	}
	
	public static boolean getBoolean() {
		sc.nextLine();
		String s=sc.nextLine();
		
		if(s.toLowerCase().equals("yes"))
			return true;
		
		return true;
	}

	public static void keepScreen() {
		sc.nextLine();
	}
	
	public static String getDateTime() {
		GregorianCalendar g=new GregorianCalendar();
		String s=g.get(Calendar.DATE)+"/"+(g.get(Calendar.MONTH)+1)+"\t"+g.get(Calendar.HOUR)+":"+g.get(Calendar.MINUTE);
		return s;
	}

}

class MyException extends Exception {
	MyException(String message) {
		super(message);
	}
}
