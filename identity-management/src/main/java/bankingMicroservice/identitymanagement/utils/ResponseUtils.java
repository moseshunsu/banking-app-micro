package bankingMicroservice.identitymanagement.utils;

import java.util.Random;

public class ResponseUtils {

    public static final String USER_EXISTS_CODE = "001";
    public static final String USER_EXISTS_MESSAGE = "User with provided email already exists!";
    public  static final int LENGTH_OF_ACCOUNT_NUMBER = 10;
    public static final String SUCCESS = "002";
    public static final String USER_SUCCESS_MESSAGE = "User successfully registered!";
    public static final String SUCCESS_MESSAGE = "Successfully Done!";
    public static final String USER_NOT_FOUND_MESSAGE = "This user doesn't exists";
    public static final String USER_NOT_FOUND_CODE = "003";

    public static String generateAccountNumber(int length) {
        String accountnumber = "";
        int x;
        char[] stringChars = new char[length];

        for (int i = 0; i < length; i++) {
            Random random = new Random();
            x = random.nextInt(9);

            stringChars[i] = Integer.toString(x).toCharArray()[0];
        }

        accountnumber = new String(stringChars);
        return accountnumber.trim();
    }
}
