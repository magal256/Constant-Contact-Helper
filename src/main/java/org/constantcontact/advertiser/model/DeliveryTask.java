package org.constantcontact.advertiser.model;

import lombok.Data;

@Data
public class DeliveryTask {

    private String id;

    private Delivery delivery;

    private User user;

    private String ccTaskId;

    private String ccListId;

    private boolean created = false;

    private boolean tested   = false;

    private boolean testTask = false;
}
