package org.constantcontact.advertiser.service.v2;

import com.constantcontact.ConstantContact;
import com.constantcontact.components.activities.contacts.request.AddContactsRequest;
import com.constantcontact.components.activities.contacts.request.ContactData;
import com.constantcontact.components.activities.contacts.request.ContactDataLightValue;
import com.constantcontact.components.activities.contacts.request.RemoveContactsRequest;
import com.constantcontact.components.activities.contacts.response.ContactsResponse;
import com.constantcontact.components.contacts.Contact;
import com.constantcontact.components.contacts.ContactList;
import com.constantcontact.components.generic.response.ResultSet;

import com.constantcontact.exceptions.service.ConstantContactServiceException;
import com.constantcontact.services.contactlists.IContactListService;
import org.constantcontact.advertiser.model.*;
import com.constantcontact.components.contacts.EmailAddress;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.core.env.Environment;


public class ConstantContactService {
    private static final DateFormat DF_DATE;
    private static final DateFormat DF_DATE_AND_TIME;

    static {
        DF_DATE = new SimpleDateFormat("yyyy-MM-dd");
        DF_DATE_AND_TIME = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        DF_DATE_AND_TIME.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
    }

    private ConstantContact constantContact;
    private final Environment properties;

    public ConstantContactService(Environment properties) {
        this.properties = properties;
        final String key = properties.getProperty("cc.api.key");
        final String accessToken = properties.getProperty("cc.api.access.token");
        this.constantContact = new ConstantContact(key, accessToken);
    }

    public boolean addSingleEmailToList(String email, ConstantContactWorkList list) throws ConstantContactServiceException {
        String listId = constantContactId(list);

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(listId)) {
            return false;
        }

        ResultSet<Contact> contacts = constantContact.getContactByEmail(email);

        if (contacts != null && contacts.size() > 0) {
            for(Contact co:contacts.getResults()){
                if (!Contact.Status.ACTIVE.toString().equalsIgnoreCase(co.getStatus())) {
                    return false;
                }
                if(co.getLists() != null && co.getLists().size() > 0) {
                    for (ContactList l:co.getLists()){
                        if(listId.equals(l.getId())){
                            return true;
                        }
                    }
                }
                final ContactList ccList = constantContact.getList(listId);

                co.getLists().add(ccList);
//              It loks like a bag in API. We can't update contact with new list if has notes in updated object
//              List cleaning not remove any notes, so it's not broke the data
                if(co.getNotes() != null && !co.getNotes().isEmpty()) {
                    co.getNotes().clear();
                }
                constantContact.updateContact(co);
            }
        } else {
            final ContactList ccList = constantContact.getList(listId);

            Contact contact = new Contact();
            contact.setEmailAddresses(Arrays.asList(new EmailAddress(email.trim())));
            contact.getLists().add(ccList);

            return constantContact.addContact(contact) != null;
        }

        return true;
    }

    public int addContactsToList(List<SendingItem> items, String listId) throws ConstantContactServiceException {
        if (items == null || items.isEmpty()) {
            return 0;
        }

        Collection<Customer> customers = items.stream().map(item -> {
            Customer customer = new Customer();
            customer.setFirstName(item.getFirstName());
            customer.setLastName(item.getLastName());
            customer.setEmail(item.getEmail());
            return customer;
        }).collect(Collectors.toList());

        return addContactsToList(customers, listId);
    }

    public int addContactsToList(Collection<Customer> customers, String listId) throws
            ConstantContactServiceException {
        AddContactsRequest request = new AddContactsRequest();
        List<ContactData> contacts = new ArrayList<>();

        for (Customer customer : customers) {
            String email = customer.getEmail();
            if (email == null || email.trim().isEmpty()) {
                continue;
            }

            final String EMAIL_REGEX = "(^([a-zA-Z0-9]+([\\.+_-][a-zA-Z0-9]+)*)@(([a-zA-Z0-9]+((\\" +
                    ".|[-]{1,2})[a-zA-Z0-9]+)*)\\.[a-zA-Z]{2,6})$)|(^$)";
            email = email.trim();
            boolean isEmail = email.matches(EMAIL_REGEX);
            if (!isEmail)
                continue;

            ContactData cd = new ContactData();
            String firstName = customer.getFirstName();
            if (firstName != null && !firstName.trim().isEmpty()) {
                firstName = firstName.trim();
                if (firstName.length() == 1) {
                    firstName = firstName.toUpperCase();
                } else {
                    firstName = (firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase());
                }
            }
            if (firstName != null && firstName.length() > 40)
                continue;
            String lastName = customer.getLastName();
            if (lastName != null && !lastName.trim().isEmpty()) {
                lastName = lastName.trim();
                if (lastName.length() == 1) {
                    lastName = lastName.toUpperCase();
                } else {
                    lastName = (lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase());
                }
            }
            cd.setEmailAddresses(Collections.singletonList(email));
            /*if(!updateContact(customer)) {*/
            cd.setFirstName(firstName);
            cd.setLastName(lastName);
            /*}*/
            contacts.add(cd);
        }

        if (contacts.isEmpty()) {
            return 0;
        }
        request.setColumnNames(Arrays.asList("FIRST NAME", "LAST NAME", "EMAIL"));
        request.setImportData(contacts);
        request.setLists(Arrays.asList(listId));
        ContactsResponse response = constantContact.addBulkContacts(request);

        return response.getContactCount();
    }

    public List<Contact> getContactsFromList(String listId) throws ConstantContactServiceException {
        ContactList list = getContactList(listId);
        ResultSet<Contact> contacts = constantContact.getContactsFromList(list);
        return contacts.getResults();
    }

    public boolean updateContact(Customer customer) throws ConstantContactServiceException {
        String email = customer.getEmail();
        ResultSet<Contact> contacts = constantContact.getContactByEmail(email);
        for (Contact contact : contacts.getResults()) {
            contact.setFirstName(customer.getFirstName());
            contact.setLastName(customer.getLastName());
            constantContact.updateContact(contact);
        }

        return contacts.size() > 0;
    }

    public int removeContactsFromList(Collection<String> emails, String listId) throws
            ConstantContactServiceException {
        RemoveContactsRequest request = new RemoveContactsRequest();
        List<ContactDataLightValue> contactsLight = new ArrayList<ContactDataLightValue>();
        for (String email : emails) {
            if (email == null || emails.isEmpty()) {
                continue;
            }
            ContactDataLightValue cdlv = new ContactDataLightValue();
            cdlv.setEmailAddresses(Arrays.asList(email));
            contactsLight.add(cdlv);
        }

        request.setImportData(contactsLight);
        request.setLists(Arrays.asList(listId));

        ContactsResponse response = constantContact.removeBulkContactsFromLists(request);
        return response.getErrorCount();
    }

    public ContactList getContactList(String listId) throws ConstantContactServiceException {
        return constantContact.getList(listId);
    }

    public Collection<ContactList> getContactLists(Date date) throws ConstantContactServiceException {
        return constantContact.getLists(DF_DATE.format(date == null ? DateTime.now().withYear(1900).toDate() : date));
    }

    public boolean removeContactList(String id) throws ConstantContactServiceException {
        IContactListService cls = constantContact.getListService();
        return cls.deleteList(constantContact.getAccessToken(), id);
    }

    public  ContactList createContactList(String name) throws ConstantContactServiceException {
        IContactListService cls = constantContact.getListService();
        ContactList cl = new ContactList();
        cl.setName(name);
        cl.setStatus("HIDDEN");
        return cls.addList(constantContact.getAccessToken(), cl);
    }

    private boolean removeEmailCampaignSchedule(String campaignId, String scheduleId) throws
            ConstantContactServiceException {
        return constantContact.deleteEmailCampaignSchedule(campaignId, scheduleId);
    }

    private String constantContactId(ConstantContactWorkList list){
        return (list != null? properties.getProperty("cc.api." + list.toString()): null);
    }
}
