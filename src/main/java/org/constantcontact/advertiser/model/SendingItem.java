package org.constantcontact.advertiser.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SendingItem {
    private String email;
    private String firstName;
    private String lastName;
    private ConstantContactWorkList list;
    private String site;

    public SendingItem() {
    }

    public SendingItem(String email, ConstantContactWorkList list) {
        this.email = email;
        this.list = list;
    }

    public SendingItem(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof SendingItem)) {
            return false;
        }

        SendingItem item = (SendingItem) obj;

        return Objects.equals(email, item.email) &&
                Objects.equals(list, item.list) &&
                Objects.equals(site, item.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, list, site);
    }

    @Override
    public String toString() {
        return "SendingItem{" +
                "email='" + email + '\'' +
                ", list=" + list +
                '}';
    }
}
