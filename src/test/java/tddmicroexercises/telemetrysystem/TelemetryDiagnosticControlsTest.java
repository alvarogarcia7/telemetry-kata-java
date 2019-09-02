package tddmicroexercises.telemetrysystem;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class TelemetryDiagnosticControlsTest {

	@Test
	public void telemetry_stores_anything_set() {
		final TelemetryDiagnosticControls telemetryDiagnosticControls = new TelemetryDiagnosticControls();
		final String anyContents = "hello";

		telemetryDiagnosticControls.setDiagnosticInfo(anyContents);

		assertThat(telemetryDiagnosticControls.getDiagnosticInfo(), Is.is(anyContents));
	}

	@Test
	public void CheckTransmission_should_send_a_diagnostic_message_and_receive_a_status_message_response() throws Exception {
		final TelemetryDiagnosticControls telemetryDiagnosticControls = new TelemetryDiagnosticControls();
		telemetryDiagnosticControls.checkTransmission();
		assertThat(telemetryDiagnosticControls.getDiagnosticInfo(), Is.is(
			"LAST TX rate................ 100 MBPS\r\n" +
				"HIGHEST TX rate............. 100 MBPS\r\n" +
				"LAST RX rate................ 100 MBPS\r\n" +
				"HIGHEST RX rate............. 100 MBPS\r\n" +
				"BIT RATE.................... 100000000\r\n" +
				"WORD LEN.................... 16\r\n" +
				"WORD/FRAME.................. 511\r\n" +
				"BITS/FRAME.................. 8192\r\n" +
				"MODULATION TYPE............. PCM/FM\r\n" +
				"TX Digital Los.............. 0.75\r\n" +
				"RX Digital Los.............. 0.10\r\n" +
				"BEP Test.................... -5\r\n" +
				"Local Rtrn Count............ 00\r\n" +
				"Remote Rtrn Count........... 00")

		);
	}

	@Test
	public void perform_up_to_3_retries() throws Exception {
		TelemetryClient client = Mockito.mock(TelemetryClient.class);
		when(client.getOnlineStatus()).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true);
		final String telemetryReport = "X";
		when(client.receive()).thenReturn(telemetryReport);
		final TelemetryDiagnosticControls telemetryDiagnosticControls = new TelemetryDiagnosticControls(client);

		telemetryDiagnosticControls.checkTransmission();

		assertThat(telemetryDiagnosticControls.getDiagnosticInfo(), Is.is(telemetryReport));
	}

	@Test(expected = Exception.class)
	public void after_three_retries_there_is_an_exception() throws Exception {
		TelemetryClient client = Mockito.mock(TelemetryClient.class);
		when(client.getOnlineStatus()).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(false).thenReturn(true);
		when(client.receive()).thenReturn("X");
		final TelemetryDiagnosticControls telemetryDiagnosticControls = new TelemetryDiagnosticControls(client);

		telemetryDiagnosticControls.checkTransmission();

	}

}
