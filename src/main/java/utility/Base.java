package utility;

import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Base extends WaitConstant {
    private final Logger ltLogger = LogManager.getLogger(Base.class);

    public boolean retry(int maxTries, Runnable r) {
        int tries = 0;
        while (tries < maxTries) {
            try {
                ltLogger.info("TRIES {}/{}", tries + 1, maxTries);
                r.run();
                return true;
            } catch (Exception e) {
                ltLogger.error("Exception :- {}", e.toString());
                tries++;
            }
        }
        return false;
    }

    public Random newRandom() {
        return new Random();
    }

    public void waitForTime(int sleepInSeconds) {
        try {
            TimeUnit.SECONDS.sleep(sleepInSeconds);
        } catch (InterruptedException e) {
            ltLogger.error("Error occurred in sleep method");
            Thread.currentThread().interrupt();
        }
    }

    public String getRandomAlphaNumericString(int size) {
        byte[] bytArray = new byte[256];
        newRandom().nextBytes(bytArray);
        String randomStr = new String(bytArray, StandardCharsets.UTF_8);
        StringBuilder strBuilder = new StringBuilder();
        String alphaNumericStr = randomStr.replaceAll("[^A-Za-z0-9]", "");
        for (int i = 0; i < alphaNumericStr.length(); i++) {
            if (size > 0 && (Character.isLetter(alphaNumericStr.charAt(i)) || Character.isDigit(alphaNumericStr.charAt(i)))) {
                strBuilder.append(alphaNumericStr.charAt(i));
            }
            size--;
        }
        return strBuilder.toString();
    }

    @SneakyThrows
    public String getOpenPort() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            serverSocket.close();
            return String.valueOf(serverSocket.getLocalPort());
        } catch (IOException e) {
            ltLogger.error("Failed to get an open port :- %s", e);
            throw new IOException(e);
        }
    }
}
