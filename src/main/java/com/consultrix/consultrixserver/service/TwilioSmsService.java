package com.consultrix.consultrixserver.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TwilioSmsService {

    private static final Logger log = LoggerFactory.getLogger(TwilioSmsService.class);

    private final String fromNumber;
    private final List<String> recipientNumbers;

    public TwilioSmsService(
            @Value("${TWILIO_ACCOUNT_SID}") String accountSid,
            @Value("${TWILIO_AUTH_TOKEN}") String authToken,
            @Value("${TWILIO_FROM_NUMBER}") String fromNumber,
            @Value("${TWILIO_RECIPIENT_1}") String recipient1,
            @Value("${TWILIO_RECIPIENT_2}") String recipient2
    ) {
        Twilio.init(accountSid, authToken);
        this.fromNumber = fromNumber;
        this.recipientNumbers = List.of(recipient1, recipient2);
    }

    public void sendTicketNotification(Integer ticketId, String issue, String submitterName) {
        String body = String.format(
                "[Consultrix] New Support Ticket #%d\nSubmitted by: %s\nIssue: %s",
                ticketId, submitterName, issue
        );

        for (String recipient : recipientNumbers) {
            try {
                Message message = Message.creator(
                        new PhoneNumber(recipient),
                        new PhoneNumber(fromNumber),
                        body
                ).create();
                log.info("Twilio SMS sent to {} | SID: {}", recipient, message.getSid());
            } catch (Exception e) {
                log.error("Failed to send Twilio SMS to {}: {}", recipient, e.getMessage());
            }
        }
    }
}

