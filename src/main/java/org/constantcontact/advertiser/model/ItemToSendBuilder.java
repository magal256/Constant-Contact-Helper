package org.constantcontact.advertiser.model;

import org.apache.commons.lang3.StringUtils;

public class ItemToSendBuilder {
    private SendingItem sendingItem = new SendingItem();

    public static ItemToSendBuilder newBuilder() { return new ItemToSendBuilder(); }

    public ItemToSendBuilder email(String email) {
        this.sendingItem.setEmail(email);
        return this;
    }

    public ItemToSendBuilder list(ConstantContactWorkList list) {
        this.sendingItem.setList(list);
        return this;
    }

    public ItemToSendBuilder site(String site) {
        this.sendingItem.setSite(site);
        return this;
    }

    public SendingItem build() {
        if (StringUtils.isBlank(sendingItem.getEmail())) {
            throw new RuntimeException("Ivalid email content to sent into Constant Contact");
        }
        if (sendingItem.getList() == null) {
            throw new RuntimeException(" Constant Contact destination list not set");
        }

        return sendingItem;
    }
}
