package tddmicroexercises.telemetrysystem;

class RetriesTelemetryClient extends TelemetryClient {

	private final TelemetryClient telemetryClient;
	private int retryLeft = 3;

	public RetriesTelemetryClient(TelemetryClient telemetryClient) {
		this.telemetryClient = telemetryClient;
	}

	@Override
	public void connect(String diagnosticChannelConnectionString) throws RuntimeException {
        while (!telemetryClient.getOnlineStatus() && retryLeft > 0)
        {
            telemetryClient.connect(diagnosticChannelConnectionString);
            retryLeft -= 1;
        }

        if(!telemetryClient.getOnlineStatus())
        {
            throw new RuntimeException("Unable to connect.");
        }
    }

	@Override
	public String receive() {
		return telemetryClient.receive();
	}

	@Override
	public boolean getOnlineStatus() {
		return telemetryClient.getOnlineStatus();
	}

	@Override
	public void disconnect() {
		telemetryClient.disconnect();
	}

	@Override
	public void send(String message) {
		telemetryClient.send(message);
	}
}
