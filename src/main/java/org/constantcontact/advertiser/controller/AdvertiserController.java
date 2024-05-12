package org.constantcontact.advertiser.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.constantcontact.advertiser.dto.ContactInfoDto;
import org.constantcontact.advertiser.model.ItemToSendBuilder;
import org.constantcontact.advertiser.service.v2.ConstantContactSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.constantcontact.advertiser.model.ConstantContactWorkList.BusinessList;

@RestController
@RequestMapping("${spring.application.apiPath}/advertiser/constant-contact")
@Tag(name = "Advertiser web API")
public class AdvertiserController {

    @Autowired
    private ConstantContactSender sender;

    @PostMapping(path = "/save-contact")
    public ResponseEntity<String> saveContact(final @RequestBody ContactInfoDto contact) {
        sender.addEmailToQueue(
                ItemToSendBuilder.newBuilder()
                        .email(contact.getEmail())
                        .list(BusinessList)
                        .site(contact.getAccount())
                        .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body("Message has been added to queue");
    }
}
