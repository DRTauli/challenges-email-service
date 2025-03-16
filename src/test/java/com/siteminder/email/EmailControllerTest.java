package com.siteminder.email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows; // For exception checking
import static org.mockito.Mockito.*;

class EmailControllerTest {

	@InjectMocks
	private EmailController emailController;

	@Mock
	private EmailServiceFactory emailServiceFactory;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void sendEmail_Success() {
		// Arrange
		String subject = "Test Subject";
		String body = "Hello World";
		List<String> recipients = Collections.singletonList("recipient@example.com");

		when(emailServiceFactory.sendEmail(subject, body, recipients, null, null)).thenReturn(true);

		// Act
		String response = emailController.sendEmail(subject, body, recipients, null, null);

		// Assert
		assertEquals("Email sent successfully!", response);
		verify(emailServiceFactory, times(1)).sendEmail(subject, body, recipients, null, null);
	}

	@Test
	void sendEmail_MissingSubject() {
		// Arrange
		String body = "Hello World";
		List<String> recipients = Collections.singletonList("recipient@example.com");

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			emailController.sendEmail(null, body, recipients, null, null);
		});

		assertEquals("Subject is required.", exception.getMessage());
	}

	@Test
	void sendEmail_LongSubject() {
		String body = "Hello World";
		String longSubject = "This subject is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for subjects which is 255. "
				+ "This subject is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for subjects which is 255. ";

		List<String> recipients = Collections.singletonList("recipient@example.com");

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			emailController.sendEmail(longSubject, body, recipients, null, null);
		});

		assertEquals("Subject must not exceed 255 characters.", exception.getMessage());
	}

	@Test
	void sendEmail_MissingBody() {
		// Arrange
		String subject = "Test Subject";
		List<String> recipients = Collections.singletonList("recipient@example.com");

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			emailController.sendEmail(subject, null, recipients, null, null);
		});

		assertEquals("Body is required.", exception.getMessage());
	}

	@Test
	void sendEmail_LongBody() {
		String subject = "Test Subject";
		String longBody = "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. "
				+ "This body is way too long and should trigger validation errors "
				+ "because it exceeds the maximum length limit set for bodies. ";

		List<String> recipients = Collections.singletonList("recipient@example.com");

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			emailController.sendEmail(subject, longBody, recipients, null, null);
		});

		assertEquals("Body must not exceed 2000 characters.", exception.getMessage());
	}

	@Test
	void sendEmail_MissingRecipients() {
		// Arrange
		String subject = "Test Subject";
		String body = "Hello World";

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			emailController.sendEmail(subject, body, null, null, null);
		});

		assertEquals("At least one recipient is required.", exception.getMessage());
	}

	@Test
	void sendEmail_EmptyRecipientsList() {
		// Arrange
		String subject = "Test Subject";
		String body = "Hello World";
		List<String> recipients = Collections.emptyList(); // Empty list for recipients

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			emailController.sendEmail(subject, body, recipients, null, null);
		});

		assertEquals("At least one recipient is required.", exception.getMessage());
	}

	@Test
	void sendEmail_WithMultipleRecipients() {
		// Arrange
		String subject = "Test Subject";
		String body = "Hello World";
		List<String> recipients = Arrays.asList("recipient1@example.com", "recipient2@example.com",
				"recipient3@example.com");

		when(emailServiceFactory.sendEmail(subject, body, recipients, null, null)).thenReturn(true);

		// Act
		String response = emailController.sendEmail(subject, body, recipients, null, null);

		// Assert
		assertEquals("Email sent successfully!", response);
		verify(emailServiceFactory, times(1)).sendEmail(subject, body, recipients, null, null);
	}

	@Test
	void sendEmail_WithCCAndBCC() {
		// Arrange
		String subject = "Test Subject";
		String body = "Hello World";
		List<String> recipients = Collections.singletonList("recipient@example.com");
		List<String> cc = Collections.singletonList("cc@example.com");
		List<String> bcc = Collections.singletonList("bcc@example.com");

		when(emailServiceFactory.sendEmail(subject, body, recipients, cc, bcc)).thenReturn(true);

		// Act
		String response = emailController.sendEmail(subject, body, recipients, cc, bcc);

		// Assert
		assertEquals("Email sent successfully!", response);
		verify(emailServiceFactory, times(1)).sendEmail(subject, body, recipients, cc, bcc);
	}

	@Test
	void sendEmail_WithMultipleCCs() {
		// Arrange
		String subject = "Test Subject";
		String body = "Hello World";
		List<String> recipients = Collections.singletonList("recipient@example.com");
		List<String> cc = Arrays.asList("cc1@example.com", "cc2@example.com", "cc3@example.com");

		when(emailServiceFactory.sendEmail(subject, body, recipients, cc, null)).thenReturn(true);

		// Act
		String response = emailController.sendEmail(subject, body, recipients, cc, null);

		// Assert
		assertEquals("Email sent successfully!", response);
		verify(emailServiceFactory, times(1)).sendEmail(subject, body, recipients, cc, null);
	}

	@Test
	void sendEmail_WithDuplicateRecipients() {
		// Arrange
		String subject = "Test Subject";
		String body = "Hello World";
		List<String> recipients = Arrays.asList("recipient@example.com", "recipient@example.com");
		when(emailServiceFactory.sendEmail(subject, body, Collections.singletonList("recipient@example.com"), null,
				null)).thenReturn(true); // Simulate successful sending

		// Act
		String response = emailController.sendEmail(subject, body, recipients, null, null);

		// Assert
		assertEquals("Email sent successfully!", response);
		verify(emailServiceFactory, times(1)).sendEmail(subject, body,
				Collections.singletonList("recipient@example.com"), null, null);
	}

	@Test
	void sendEmail_InvalidRecipientEmailFormat() {
		// Arrange
		String subject = "Test Subject";
		String body = "Hello World";
		List<String> recipients = Arrays.asList("invalid-email", "recipient@example.com");

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			emailController.sendEmail(subject, body, recipients, null, null);
		});

		assertEquals("recipient email 'invalid-email' is not valid.", exception.getMessage());
	}

	@Test
	void sendEmail_TooManyRecipients() {
		String subject = "Test Subject";
		String body = "Hello World";
		List<String> recipients = Arrays.asList("recipient1@example.com", "recipient2@example.com",
				"recipient3@example.com", "recipient4@example.com", "recipient5@example.com", "recipient6@example.com",
				"recipient7@example.com", "recipient8@example.com", "recipient9@example.com", "recipient10@example.com",
				"recipient11@example.com"); // Exceeds the allowed limit

		// Act & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			emailController.sendEmail(subject, body, recipients, null, null);
		});

		assertEquals("Too many recipients. Maximum allowed: 10.", exception.getMessage());
	}
}