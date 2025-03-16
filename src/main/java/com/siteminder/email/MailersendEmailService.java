package com.siteminder.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.siteminder.util.HttpClientUtil;

import java.io.IOException;
import java.util.List;

@Service
public class MailersendEmailService implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(MailersendEmailService.class);

	@Value("${mailersend.apiBaseUrl}")
	private String apiBaseUrl;

	@Value("${mailersend.apiKey}") // Load Mailersend API Key from properties
	private String apiKey;

	@Value("${mailersend.fromEmail}")
	private String fromEmail;

	@Override
	public boolean sendEmail(String subject, String body, List<String> recipients, List<String> ccRecipients,
			List<String> bccRecipients) {

		StringBuilder requestBody = new StringBuilder();
		requestBody.append("{").append("\"from\": {\"email\": \"").append(fromEmail).append("\"},").append("\"to\": [");
		for (String recipient : recipients) {
			requestBody.append("{\"email\": \"").append(recipient).append("\"},");
		}
		requestBody.deleteCharAt(requestBody.length() - 1);
		requestBody.append("],");

		// Handle CC
		if (ccRecipients != null && !ccRecipients.isEmpty()) {
			requestBody.append("\"cc\": [");
			for (String cc : ccRecipients) {
				requestBody.append("{\"email\": \"").append(cc).append("\"},");
			}
			requestBody.deleteCharAt(requestBody.length() - 1);
			requestBody.append("],");
		}

		// Handle BCC
		if (bccRecipients != null && !bccRecipients.isEmpty()) {
			requestBody.append("\"bcc\": [");
			for (String bcc : bccRecipients) {
				requestBody.append("{\"email\": \"").append(bcc).append("\"},");
			}
			requestBody.deleteCharAt(requestBody.length() - 1);
			requestBody.append("],");
		}

		requestBody.append("\"subject\": \"").append(subject).append("\",").append("\"text\": \"").append(body)
				.append("\"").append("}");

		logger.debug("Mailersend request body: {}", requestBody.toString());

		try {
			String response = HttpClientUtil.sendPostRequest(apiBaseUrl, requestBody.toString(), apiKey);
			logger.info("Mailersend Response: {}", response);
		} catch (IOException | InterruptedException e) {
			logger.error("Error sending email via Mailersend: {}", e.getMessage());
			throw new RuntimeException("Failed to send email via Mailersend", e);
		}
		return true;
	}
}