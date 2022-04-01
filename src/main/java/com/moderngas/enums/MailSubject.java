package com.moderngas.enums;

import org.springframework.util.ObjectUtils;

public enum MailSubject {

    MAIL_SUBJECT_FORGET_PASSWORD("Forget Password"),
    MAIL_SUBJECT_NEW_PASSWORD("Password Generated");

    private final String name;

    public String getName() {
        return name;
    }

    MailSubject(String name) {
        this.name = name;
    }

    public static MailSubject getByName(String name) {
        if (!ObjectUtils.isEmpty(name)) {
            for (MailSubject mailSubject : MailSubject.values()) {
                if (mailSubject.getName().equals(name)) {
                    return mailSubject;
                }
            }
        }
        return null;
    }
}
