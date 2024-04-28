package org.constantcontact.advertiser.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${spring.application.apiPath}/advertiser/constant-contact")
@Tag(name = "Advertiser web API")
public class AdvertiserController {

    @PostMapping(path = "/save-contact")
    public ResponseEntity<String> saveContact(final @RequestBody String email) {
        return ResponseEntity.status(HttpStatus.CREATED).body(email);
    }
}
