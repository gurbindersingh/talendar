package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import org.springframework.stereotype.Component;

@Component
public class MailData {

    private String adminMail;
    private String adminPassword;
    private String senderMail;
    private String senderPassword;

    public MailData () {
        this.adminMail = "davedude341@gmail.com";
        this.adminPassword = "";
        this.senderMail = "sepmqse25test@gmail.com";
        this.senderPassword = "This!is!a!password!";
    }

    public String getAdminMail() {
        return adminMail;
    }
    public String getAdminPassword() {
        return adminPassword;
    }
    public String getSenderMail() {
        return senderMail;
    }
    public String getSenderPassword() {
        return senderPassword;
    }
}
