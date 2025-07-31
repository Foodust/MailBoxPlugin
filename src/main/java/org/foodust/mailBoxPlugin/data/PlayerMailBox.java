package org.foodust.mailBoxPlugin.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerMailBox {
    @Getter
    private final UUID playerUUID;
    private final List<Mail> mails;

    public PlayerMailBox(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.mails = new ArrayList<>();
    }

    public List<Mail> getMails() {
        return new ArrayList<>(mails);
    }

    public void addMail(Mail mail) {
        mails.add(mail);
    }

    public void removeMail(Mail mail) {
        mails.remove(mail);
    }

    public void removeMail(int index) {
        if (index >= 0 && index < mails.size()) {
            mails.remove(index);
        }
    }

    public void clearMails() {
        mails.clear();
    }

    public boolean isEmpty() {
        return mails.isEmpty();
    }

    public int getSize() {
        return mails.size();
    }
}