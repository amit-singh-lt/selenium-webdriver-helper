package stepdefinitions;

import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.EnvSetup;
import utility.Tunnel;

import java.io.IOException;

public class TunnelStepDefinition extends Tunnel {
    private final Logger ltLogger = LogManager.getLogger(TunnelStepDefinition.class);

    @Then("^User start tunnel$")
    public void iStartTunnel() {
        String tunnelName = System.getProperty("TUNNEL_NAME");
        if (tunnelName != null) {
            EnvSetup.TUNNEL_NAME_THREAD_LOCAL.set(tunnelName);
            ltLogger.info("Tunnel Name received from CLI is :- {}", EnvSetup.TUNNEL_NAME_THREAD_LOCAL.get());
            return;
        }
        startTunnel(getRandomMode(), 3);
    }

    @Then("^I start tunnel with (mitm|ssh) mode$")
    public void iStartTunnelWithMode(String mode) {
        String tunnelName = System.getProperty("TUNNEL_NAME");
        if (tunnelName != null) {
            EnvSetup.TUNNEL_NAME_THREAD_LOCAL.set(tunnelName);
            ltLogger.info("Tunnel Name received from CLI is :- {}", EnvSetup.TUNNEL_NAME_THREAD_LOCAL.get());
            return;
        }
        startTunnel(mode, 24);
    }

    @Then("^User stops tunnel$")
    public void iStopTunnel() throws IOException, InterruptedException {
        stopTunnel();
        uploadTunnelLogs();
    }
}
