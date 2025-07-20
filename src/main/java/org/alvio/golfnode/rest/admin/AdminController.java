package org.alvio.golfnode.rest.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DeleteMapping("/reset")
    public ResponseEntity<?> resetAndShutdown() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS tournament_member, tournament, member");

        Thread shutdownThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // Allow response to return
                System.exit(0);     // Trigger app shutdown
            } catch (InterruptedException ignored) {
            }
        });
        shutdownThread.setDaemon(false);
        shutdownThread.start();

        return ResponseEntity.ok(Map.of("message", "Tables dropped. Application will now shut down. Restart manually."));
    }

}