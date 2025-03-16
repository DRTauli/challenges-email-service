package com.siteminder.email;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class EmailServiceFactoryTest {

	@InjectMocks
	private EmailServiceFactory emailServiceFactory;

	@Mock
	private MailgunEmailService mailgunEmailService;

	@Mock
	private SendGridEmailService sendGridEmailService;

	@Mock
	private MailersendEmailService mailersendEmailService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void sendEmail_AllServicesFail() {
		// Arrange
		when(mailgunEmailService.sendEmail(anyString(), anyString(), anyList(), anyList(), anyList()))
				.thenReturn(false);
		when(sendGridEmailService.sendEmail(anyString(), anyString(), anyList(), anyList(), anyList()))
				.thenReturn(false);
		when(mailersendEmailService.sendEmail(anyString(), anyString(), anyList(), anyList(), anyList()))
				.thenReturn(false);

		// Act & Assert
		assertThrows(RuntimeException.class, () -> {
			emailServiceFactory.sendEmail("Test Subject", "Hello World", Arrays.asList("recipient@example.com"),
					Arrays.asList("cc@example.com"), Arrays.asList("bcc@example.com"));
		});

		// Verify that all services were attempted
		verify(mailgunEmailService, times(1)).sendEmail(anyString(), anyString(), anyList(), anyList(), anyList());
		verify(sendGridEmailService, times(1)).sendEmail(anyString(), anyString(), anyList(), anyList(), anyList());
		verify(mailersendEmailService, times(1)).sendEmail(anyString(), anyString(), anyList(), anyList(), anyList());
	}

// Enable once Config on Mailgun Succeeds
//	@Test
//	void sendEmail_FirstServiceSucceeds() {
//		// Arrange
//		when(mailgunEmailService.sendEmail(anyString(), anyString(), anyList(), anyList(), anyList()))
//				.thenReturn(true); // Simulate successful sending for Mailgun
//
//		// Act
//		boolean result = emailServiceFactory.sendEmail("Test Subject", "Hello World",
//				Arrays.asList("recipient@example.com"), Arrays.asList("cc@example.com"),
//				Arrays.asList("bcc@example.com"));
//
//		// Assert
//		verify(mailgunEmailService, times(1)).sendEmail(anyString(), anyString(), anyList(), anyList(), anyList());
//		assert (result); // Check that the result is true
//	}

	// Enable once Config on SendGrid Succeeds
//	@Test
//	void sendEmail_SecondServiceSucceeds() {
//		// Arrange
//		when(mailgunEmailService.sendEmail(anyString(), anyString(), anyList(), anyList(), anyList()))
//				.thenReturn(false); // Simulate failure for Mailgun
//		when(sendGridEmailService.sendEmail(anyString(), anyString(), anyList(), anyList(), anyList()))
//				.thenReturn(true); // Simulate successful sending  for SendGrid
//
//		// Act
//		boolean result = emailServiceFactory.sendEmail("Test Subject", "Hello World",
//				Arrays.asList("recipient@example.com"), Arrays.asList("cc@example.com"),
//				Arrays.asList("bcc@example.com"));
//
//		// Assert
//		verify(mailgunEmailService, times(1)).sendEmail(anyString(), anyString(), anyList(), anyList(), anyList());
//		verify(sendGridEmailService, times(1)).sendEmail(anyString(), anyString(), anyList(), anyList(), anyList());
//		assert (result); // Check that the result is true
//	}
	
	@Test
	void sendEmail_ThirdServiceSucceeds() {
		// Arrange
		when(mailgunEmailService.sendEmail(anyString(), anyString(), anyList(), anyList(), anyList()))
				.thenReturn(false); // Simulate failure for Mailgun
		when(sendGridEmailService.sendEmail(anyString(), anyString(), anyList(), anyList(), anyList()))
				.thenReturn(false); // Simulate failure for SendGrid
		when(mailersendEmailService.sendEmail(anyString(), anyString(), anyList(), anyList(), anyList()))
				.thenReturn(true); // Simulate successful sending with Mailersend

		// Act
		boolean result = emailServiceFactory.sendEmail("Test Subject", "Hello World",
				Arrays.asList("recipient@example.com"), Arrays.asList("cc@example.com"),
				Arrays.asList("bcc@example.com"));

		// Assert
		verify(mailgunEmailService, times(1)).sendEmail(anyString(), anyString(), anyList(), anyList(), anyList());
		verify(sendGridEmailService, times(1)).sendEmail(anyString(), anyString(), anyList(), anyList(), anyList());
		verify(mailersendEmailService, times(1)).sendEmail(anyString(), anyString(), anyList(), anyList(), anyList());
		assert (result); // Check that the result is true
	}
}