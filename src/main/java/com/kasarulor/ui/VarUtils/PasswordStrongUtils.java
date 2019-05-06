package com.kasarulor.ui.VarUtils;

/**/
public class PasswordStrongUtils {


    public static boolean passwordStrong(String password) {
        if (password != null && password.length() >= 8) {
            if (!(password.matches("[a-zA-Z0-9]{8,20}")
                    && password.matches("[a-zA-Z0-9]*[a-z]+[a-zA-Z0-9]*")
                    && password.matches("[a-zA-Z0-9]*[A-Z]+[a-zA-Z0-9]*")
                    && password.matches("[a-zA-Z0-9]*[0-9]+[a-zA-Z0-9]*"))) {
                return false;
            }
            return true;
        }
        return false;

    }


}
