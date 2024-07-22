package artifacts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Constant;
import utility.EnvSetup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Artifacts extends Constant {

    private final Logger ltLogger = LogManager.getLogger(Artifacts.class);

    public void processCommand(List<String> command) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            BufferedReader stdOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            ltLogger.info("Standard Output:");
            while ((line = stdOutput.readLine()) != null) {
                ltLogger.info(line);
            }

            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            ltLogger.info("Standard Error:");
            while ((line = stdError.readLine()) != null) {
                ltLogger.error(line);
            }

            int exitCode = process.waitFor();
            ltLogger.info("Exited with code: {}", exitCode);
        } catch (Exception e) {
            ltLogger.error(e);
        }
    }

    public void checkVideo() {
        List<String> command = new ArrayList<>();
        command.add(System.getProperty(Constant.USER_DIR) + "/bash/video.sh");
        command.add(EnvSetup.SELENIUM_TEST_DRIVER_SESSION_ID_THREAD_LOCAL.get());
        command.add(EnvSetup.USER_NAME);
        command.add(EnvSetup.USER_ACCESS_KEY);
        ltLogger.info("Video Verification Command :- {}", command);
        processCommand(command);
    }

    public void checkCommandLogs() {
        List<String> command = new ArrayList<>();
        command.add(System.getProperty(Constant.USER_DIR) + "/bash/commandLogs.sh");
        command.add(EnvSetup.SELENIUM_TEST_DRIVER_SESSION_ID_THREAD_LOCAL.get());
        command.add(EnvSetup.USER_NAME);
        command.add(EnvSetup.USER_ACCESS_KEY);
        ltLogger.info("Command Logs Verification Command :- {}", command);
        processCommand(command);
    }
}
