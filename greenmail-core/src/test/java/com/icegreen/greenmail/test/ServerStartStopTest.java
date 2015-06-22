package com.icegreen.greenmail.test;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test that checks if greenmail start, stop and reset works correctly
 */
public class ServerStartStopTest {
    @Test
    @Ignore
    public void testStartStop() {
        GreenMail service = new GreenMail(ServerSetupTest.ALL);
        try {
            // Try to stop before start: Nothing happens
            service.stop();
            service.start();
            service.stop();
            // Now the server is stopped, should be started by reset command
            service.reset();
            // Start again
            service.reset();
        } finally {
            // And finally stop
            service.stop();
        }
    }

    @Test
    @Ignore
    public void testServerStartupTimeout() {
        // Create a few setups
        ServerSetup setups[] = ServerSetupTest.ALL;

        // Set too low startup timeout
        for(ServerSetup s: setups) {
            s.setServerStartupTimeout(0L);
        }

        // Will fail, as timeouts are set to 0.
        GreenMail service = new GreenMail(setups);
        try {
            service.start();
            fail("Expected timeout");
        } catch (IllegalStateException ex) {
            assertTrue(ex.getMessage().contains("try to set server startup timeout > "));
        } finally {
            service.stop();
        }

        // Set large startup timeout
        for(ServerSetup s: setups) {
            s.setServerStartupTimeout(3000L);
        }
        service = new GreenMail(setups);
        try {
            service.start(); // Should work with large timeout
        } finally {
            service.stop();
        }
    }
}
