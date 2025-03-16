package com.siteminder.email;

import java.util.List;

public interface EmailService {
    boolean sendEmail(String subject, String body, List<String> recipients, List<String> ccRecipients, List<String> bccRecipients);
}