package org.constantcontact.advertiser.model;

import lombok.*;

@Data
public class ConstantContactList {
    private String id;
    private String status;
    private String name;
    private int contactCount;
    private String createdDate;
}
