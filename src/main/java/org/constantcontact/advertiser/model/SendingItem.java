package org.constantcontact.advertiser.model;

import lombok.Data;

@Data
public class SendingItem {
    private String email;
    private String firstName;
    private String lastName;
    private ConstantContactWorkList list;
    private String site;
}
