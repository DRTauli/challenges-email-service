package com.siteminder.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.siteminder.util.HttpClientUtil;

import java.io.IOException;
import java.util.List;

@Service
public class SendGridEmailService implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(SendGridEmailService.class);
	@Value("${sendgrid.apiBaseUrl}")
	private String apiBaseUrl;

	@Value("${sendgrid.apiKey}")
	private String apiKey;

	@Value("${sendgrid.fromEmail}")
	private String fromEmail;

	@Override
	public boolean sendEmail(String subject, String body, List<String> recipients, List<String> ccRecipients,
			List<String> bccRecipients) {

		// Construct recipients
		StringBuilder requestBody = new StringBuilder();
		requestBody.append("{").append("\"personalizations\": [{\"to\": [");

		for (String recipient : recipients) {
			requestBody.append("{\"email\": \"").append(recipient).append("\"},");
		}
		requestBody.deleteCharAt(requestBody.length() - 1); // Remove last comma

		// Handle CC
		if (ccRecipients != null && !ccRecipients.isEmpty()) {
			requestBody.append(",\"cc\":[");
			for (String cc : ccRecipients) {
				requestBody.append("{\"email\":\"").append(cc).append("\"},");
			}
			requestBody.deleteCharAt(requestBody.length() - 1); // Remove last comma
			requestBody.append("]");
		}
		
		// Handle BCC
		if (bccRecipients != null && !bccRecipients.isEmpty()) {
			requestBody.append(",\"bcc\":[");
			for (String bcc : bccRecipients) {
				requestBody.append("{\"email\":\"").append(bcc).append("\"},");
			}
			requestBody.deleteCharAt(requestBody.length() - 1); // Remove last comma
			requestBody.append("]");
		}
		
		requestBody.append("}],\"from\":{\"email\":\"").append(fromEmail).append("\"},").append("\"subject\":\"")
				.append(subject).append("\",").append("\"content\":[{\"type\":\"text/plain\",\"value\":\"").append(body)
				.append("\"}]}");

		logger.debug("SendGrid request body: {}", requestBody);

		try {
			String response = HttpClientUtil.sendPostRequest(apiBaseUrl, requestBody.toString(), apiKey);
			logger.info("SendGrid Response: {}", response);
		} catch (IOException | InterruptedException e) {
			logger.error("Error sending email via SendGrid: {}", e.getMessage());
			throw new RuntimeException("Failed to send email via SendGrid", e);
		}

		return true;
	}
}