package com.siteminder.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.siteminder.util.HttpClientUtil;

import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class MailgunEmailService implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(MailgunEmailService.class);

	@Value("${mailgun.apiBaseUrl}")
	private String apiBaseUrl;

	@Value("${mailgun.apiKey}")
	private String apiKey;

	@Value("${mailgun.domain}")
	private String domain;

	@Value("${mailgun.fromEmail}")
	private String fromEmail;

	@Override
	public boolean sendEmail(String subject, String body, List<String> recipients, List<String> ccRecipients,
			List<String> bccRecipients) {

		StringBuilder requestBody = new StringBuilder();
		requestBody.append("from=").append(fromEmail).append("&to=").append(String.join(",", recipients))
				.append("&subject=").append(subject).append("&text=").append(body);
		// Handle CC
		if (ccRecipients != null && !ccRecipients.isEmpty()) {
			requestBody.append("&cc=").append(String.join(",", ccRecipients));
		}
		// Handle BCC
		if (bccRecipients != null && !bccRecipients.isEmpty()) {
			requestBody.append("&bcc=").append(String.join(",", bccRecipients));
		}
		logger.debug("Mailgun request body: {}", requestBody.toString());

		String url = apiBaseUrl + "/" + domain + "/messages";
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(("api:" + apiKey).getBytes()));

		try {
			String response = HttpClientUtil.sendPostRequest(url, requestBody.toString(), apiKey);
			logger.info("Mailgun Response: {}", response);
		} catch (IOException | InterruptedException e) {
			logger.error("Error sending email via Mailgun: {}", e.getMessage());
			throw new RuntimeException("Failed to send email via Mailgun", e);
		}

		logger.info("Mailgun: Email sent successfully!");
		return true;
	}
}