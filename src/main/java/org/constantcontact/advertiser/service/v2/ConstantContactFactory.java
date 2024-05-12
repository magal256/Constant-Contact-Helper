package org.constantcontact.advertiser.service.v2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConstantContactFactory {
    @Autowired
    private ConstantContactService accountService;

    @Autowired
    private ConstantContactService additionalAccountService;

    @Value("${advertiser.login.additional.name}")
    private String accountName;

    public ConstantContactService getService(String account) {
        return accountName.equals(account)? accountService : additionalAccountService;
    }

}
