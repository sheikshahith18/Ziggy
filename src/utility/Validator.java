package utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
	static boolean validatePassword(String password) {
		if (password.length() < 8)
			return false;

		String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[!@#$%^&*+=])" + "(?=\\S+$).{8,20}$";

		Pattern p = Pattern.compile(regex);

		Matcher m = p.matcher(password);

		return m.matches();

	}

	static boolean isAttemptExceeded(int cnt) {
	
		if (cnt >= 3)
			return true;

		return false;
	}

}
