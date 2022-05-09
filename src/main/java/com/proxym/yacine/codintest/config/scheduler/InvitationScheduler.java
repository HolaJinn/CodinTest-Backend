package com.proxym.yacine.codintest.config.scheduler;

import com.proxym.yacine.codintest.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class InvitationScheduler {

    @Autowired
    private InvitationService invitationService;

    @Scheduled(cron = "0 */1 * * * *")
    public void cronInvitations() {
        invitationService.checkForExpiration();
    }
}
