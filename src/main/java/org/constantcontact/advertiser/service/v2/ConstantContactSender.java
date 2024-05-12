package org.constantcontact.advertiser.service.v2;

import com.constantcontact.exceptions.service.ConstantContactServiceException;
import lombok.extern.slf4j.Slf4j;
import org.constantcontact.advertiser.model.ConstantContactList;
import org.constantcontact.advertiser.model.ConstantContactWorkList;
import org.constantcontact.advertiser.model.SendingItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Slf4j
public class ConstantContactSender {

    @Autowired
    private ConstantContactFactory factory;

    @Value("${advertiser.login.additional.name}")
    private String additionalAccountName;

    @Value("${advertiser.login.main.name}")
    private String mainAccountName;

    private ConcurrentLinkedQueue<SendingItem> blockingQueue = new ConcurrentLinkedQueue();

    public void addEmailToQueue(SendingItem item) {
        for (SendingItem si:blockingQueue) {
            if(si.equals(item)) {
                return;
            }
        }

        blockingQueue.add(item);
    }

    @Scheduled(fixedDelay = 10000)
    public void checkBusinessEmails() {
        final Set<SendingItem> emails = new HashSet<>();

        SendingItem email = null;
        while ((email = blockingQueue.poll()) != null) {
            emails.add(email);
        }

        emails.forEach(item -> {
            if (Optional.ofNullable(item.getSite()).isPresent()) {
                sendEmail(item, item.getSite());
            } else {
                sendEmail(item, additionalAccountName);
                sendEmail(item, mainAccountName);
            }
        });
    }

    public ConcurrentLinkedQueue<SendingItem> getBlockingQueue() {
        return blockingQueue;
    }

    private void sendEmail(SendingItem item, String site) {
        final String email = item.getEmail();
        final ConstantContactWorkList list = item.getList();

        try {
            factory.getService(site).addSingleEmailToList(email, list);
        } catch (ConstantContactServiceException e) {
            if (e.getErrorInfo() != null && !e.getErrorInfo().isEmpty()) {
                log.error(String.format("Failed to save %s into CC list %s - %s: %s", email, list, site.toString(), e.getErrorInfo().get(0).toString()), e);
            } else {
                log.error(String.format("Failed to save %s into CC list %s - %s", email, list, site.toString()), e);
            }
        }
    }
}
