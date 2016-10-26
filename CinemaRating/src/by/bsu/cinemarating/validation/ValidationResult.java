package by.bsu.cinemarating.validation;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 21.04.16
 * Time: 14:56
 * Possible results of validating input data.
 */
public enum ValidationResult {
    ALL_RIGHT,
    LOGIN_PASS_INCORRECT,
    PASS_INCORRECT,
    EMAIL_INCORRECT,
    PASS_NOT_MATCH,
    LOGIN_NOT_UNIQUE,
    EMAIL_NOT_UNIQUE,
    NAME_INCORRECT,
    SURNAME_INCORRECT,
    DESCRIPTION_INCORRECT,
    YEAR_INCORRECT,
    COUNTRY_INCORRECT,
    UNKNOWN_ERROR;
}
