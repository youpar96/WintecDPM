package nz.park.kenneth.wintecdm;

public class CheckAuthority {
    private static final String PASSWORD = "";//"wintec123";

    public static boolean check(String password){
        boolean result = false;

        result = PASSWORD.equals(password);

        return result;
    }
}
