package BackEnd;

public class Security {
    private static final String USER_NAME = "head";
    private static final int PASSWORD = -1267202266;
    private static int KEY;

    public static boolean verifyUsername(String username) {
        return USER_NAME.equals(username);
    }

    public static boolean verifyPassword(String password) {
        return PASSWORD == password.hashCode();
    }

    public static boolean validateKey(String key) {
        try {
            KEY = Integer.parseInt(key);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String encrypt(String s) {
        String shifted = "";
        for(int i = 0; i < s.length(); i++) {
            char original = s.charAt(i);
            char shiftedChar = (char) ((original + KEY) % Integer.MAX_VALUE);
            shifted += shiftedChar;
        }
        return shifted;
    }

    public static String decrypt(String s) {
        String shifted = "";
        for(int i = 0; i < s.length(); i++) {
            char original = s.charAt(i);
            char shiftedChar = (char) ((original - KEY) % Integer.MAX_VALUE);
            shifted += shiftedChar;
        }
        return shifted;
    }
}
