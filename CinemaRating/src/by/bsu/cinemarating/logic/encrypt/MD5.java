package by.bsu.cinemarating.logic.encrypt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static javafx.scene.input.KeyCode.M;

/**
 * Created with IntelliJ IDEA.
 * User: Irina
 * Date: 18.04.16
 * Time: 13:37
 * Class for quick and easy MD5 encryption.
 */
public class MD5 {
    private static Logger log = LogManager.getLogger(MD5.class);

    private static final String MD5 = "MD5";

    /**
     * @param word String to encrypt
     * @return String that contains MD5 encryption of the given word
     */
    public static String encrypt(String word) {
        MessageDigest messageDigest;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.reset();
            messageDigest.update(word.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("Algorithm wasn't recognized.", e);
        }
        BigInteger bigIntPass = new BigInteger(1, digest);
        return bigIntPass.toString(16);
    }
}
