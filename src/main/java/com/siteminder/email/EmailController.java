package com.siteminder.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

	private static final Logger logger = LoggerFactory.getLogger(EmailController.class);
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

	// adjust as needed
	private static final int SUBJECT_MAX_LENGTH = 255;
	private static final int BODY_MAX_LENGTH = 2000;
	private static final int RECIPIENTS_MAX_SIZE = 10;

	@Autowired
	private EmailServiceFactory emailServiceFactory;

	@PostMapping("/send")
	public String sendEmail(@RequestParam String subject, @RequestParam String body,
			@RequestParam List<String> recipients, @RequestParam(required = false) List<String> cc,
			@RequestParam(required = false) List<String> bcc) {

		// Log the incoming request parameters
		logger.info("Received request to send email:");
		logger.info("Subject: {}", subject);
		logger.info("Body: {}", body);
		logger.info("Recipients: {}", recipients);
		logger.info("CC: {}", cc);
		logger.info("BCC: {}", bcc);

		// Input validations
		validateInput(subject, body, recipients, cc, bcc);

		// Remove duplicates from recipients, cc, and bcc
		List<String> uniqueRecipients = recipients.stream().distinct().collect(Collectors.toList());
		List<String> uniqueCC = (cc != null) ? cc.stream().distinct().collect(Collectors.toList()) : null;
		List<String> uniqueBCC = (bcc != null) ? bcc.stream().distinct().collect(Collectors.toList()) : null;

		// Send the email
		emailServiceFactory.sendEmail(subject, body, uniqueRecipients, uniqueCC, uniqueBCC);

		logger.info("Email sending process initiated successfully.");
		return "Email sent successfully!";
	}

	private void validateInput(String subject, String body, List<String> recipients, List<String> cc,
			List<String> bcc) {
		if (subject == null || subject.trim().isEmpty()) {
			throw new IllegalArgumentException("Subject is required.");
		}
		if (subject.length() > SUBJECT_MAX_LENGTH) {
			throw new IllegalArgumentException("Subject must not exceed " + SUBJECT_MAX_LENGTH + " characters.");
		}
		if (body == null || body.trim().isEmpty()) {
			throw new IllegalArgumentException("Body is required.");
		}
		if (body.length() > BODY_MAX_LENGTH) {
			throw new IllegalArgumentException("Body must not exceed " + BODY_MAX_LENGTH + " characters.");
		}
		// Validate email
		if (recipients == null || recipients.isEmpty()) {
			throw new IllegalArgumentException("At least one recipient is required.");
		}
		if (recipients.size() > RECIPIENTS_MAX_SIZE) {
			throw new IllegalArgumentException("Too many recipients. Maximum allowed: " + RECIPIENTS_MAX_SIZE + ".");
		}
		validateEmailFormat(recipients, "recipient");
		if (cc != null) {
			if (cc.size() > RECIPIENTS_MAX_SIZE) {
				throw new IllegalArgumentException("Too many cc. Maximum allowed: " + RECIPIENTS_MAX_SIZE + ".");
			}
			validateEmailFormat(cc, "CC");
		}
		if (bcc != null) {
			if (bcc.size() > RECIPIENTS_MAX_SIZE) {
				throw new IllegalArgumentException("Too many bcc. Maximum allowed: " + RECIPIENTS_MAX_SIZE + ".");
			}
			validateEmailFormat(bcc, "BCC");
		}
	}

	private void validateEmailFormat(List<String> emails, String type) {
		for (String email : emails) {
			if (!EMAIL_PATTERN.matcher(email).matches()) {
				throw new IllegalArgumentException(type + " email '" + email + "' is not valid.");
			}
		}
	}
}