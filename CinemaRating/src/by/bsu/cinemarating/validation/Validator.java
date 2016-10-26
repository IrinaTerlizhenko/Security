package by.bsu.cinemarating.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 11.04.16
 * Time: 5:08
 */
public class Validator {

    /**
     * @param target             target string to validate
     * @param regularExpressions regular expressions that a target string needs to match
     * @return true if target string matches all the expressions, false otherwise
     */
    public static boolean validate(String target, String... regularExpressions) {
        boolean valid = true;
        Pattern pattern;
        Matcher matcher;
        for (String regExp : regularExpressions) {
            pattern = Pattern.compile(regExp);
            matcher = pattern.matcher(target);
            valid = valid && matcher.matches();
        }
        return valid;
    }
}
