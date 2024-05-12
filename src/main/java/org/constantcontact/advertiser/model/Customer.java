package org.constantcontact.advertiser.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
public class Customer implements Serializable {

    private static final long serialVersionUID = 341959986705670209L;

    private String id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String billAddr1;

    private String billAddr2;

    private String billCity;

    private String billZipCode;

    private String billState;

    private String billCountry;

    private String daytimePhone;

    private String daytimeCode;

    private String eveningPhone;

    private String eveningCode;

    private String cellPhone;

    private String cellCode;

    private String email;

    private String notes;

    private Date date;

    private Date firstContactDate;

    private Date dateOfBirth;

    private String gender;

    private String cardNumber;

    private String cardNumberEnc;

    private String cardCVV;

    private String cardCVVEnc;

    private String cardExp;

    private String lastTrackId;

    private Date lastTrackDate;

    private Boolean dead = false;

    private boolean noEmails;

    private boolean merged = false;

    private Date nextContactDate;

    private Double repeatableRate;

    private Double currentMonthRepeatableRate;

    private Double saleNextMonth;

    private String mobileTrackId;

    private boolean isRecycled = false;

    private String bankName;

    private String accountNumber;

    private String routingNumber;
}
