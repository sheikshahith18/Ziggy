package utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	static boolean isPasswordValid(String password) {

		if (password.length() < 8)
			return false;

		String regex = "^(?=.*[0-9])" + "(?=.*[a-z])(?=.*[A-Z])" + "(?=.*[!@#$%^&*+=])" + "(?=\\S+$).{8,20}$";

		Pattern p = Pattern.compile(regex);

		Matcher m = p.matcher(password);

		return m.matches();

	}

	static boolean isMailIdValid(String mail) {

		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";

		Pattern pattern = Pattern.compile(regex);

		Matcher m = pattern.matcher(mail);

		return m.matches();
	}

	static boolean isMobileNoValid(String mobileNo) {

		if (mobileNo.charAt(0) - '0' < 6)
			return false;
		Pattern p = Pattern.compile("^\\d{10}$");

		Matcher m = p.matcher(mobileNo);

		return (m.matches());
	}

}
