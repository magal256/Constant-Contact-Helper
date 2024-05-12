package org.constantcontact.advertiser.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class User  implements Serializable {
    private static final long serialVersionUID = 4377479346510417475L;

    private Double commRate;

    private Boolean deleted;

    private String phone;

    private String firstName;

    private String lastName;

    private String email;

    private boolean hideInEc;

    private boolean hideUncalledReqs;

    private String name;

    public String getAnyEmail() {
        return email;
    }

    public String getDisplayName() {
        return lastName + " " + firstName;
    }
}
