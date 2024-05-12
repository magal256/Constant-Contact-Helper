package org.constantcontact.advertiser.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.Set;

@Data
public class Delivery {

    private String id;

    private String name;

    private String subject;

    private Date creationDate;

    private String content;

    private Date fromDate;

    private Date toDate;

    private int            sendTime;

    private String salutation;

    private boolean created = false;

    private Set<DeliveryTask> tasks;

    private Long total;

    private Long sent;
}
