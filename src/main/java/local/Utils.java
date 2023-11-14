package local;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final int QUORUM_SIZE = 3;

    public static int extractNumberFromTarget(String hostName, String target) {
        // Define a pattern to match "peer" followed by one or more digits
        Pattern pattern = Pattern.compile(target + "(\\d+)");
        // Create a matcher with the input hostName
        Matcher matcher = pattern.matcher(hostName);
        // Check if the pattern is found
        if (matcher.find()) {
            // Extract and parse the matched digits
            String numberString = matcher.group(1);
            return Integer.parseInt(numberString);
        } else {
            // Return a default value or throw an exception, depending on your requirements
            throw new IllegalArgumentException("Invalid host name format: " + hostName);
        }
    }

    public static void sleep(float seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
