package tddmicroexercises.telemetrysystem;

public class TelemetryDiagnosticControls
{
    private final String DiagnosticChannelConnectionString = "*111#";

    private final TelemetryClient telemetryClient;
    private String diagnosticInfo = "";

        public TelemetryDiagnosticControls()
        {
	        this(new TelemetryClient());
        }

        public TelemetryDiagnosticControls(TelemetryClient telemetryClient)
        {
	        this.telemetryClient = new RetriesTelemetryClient(telemetryClient);
        }

        public String getDiagnosticInfo(){
            return diagnosticInfo;
        }

        public void setDiagnosticInfo(String diagnosticInfo){
            this.diagnosticInfo = diagnosticInfo;
        }

        public void checkTransmission() throws Exception
        {
            diagnosticInfo = "";

            telemetryClient.disconnect();

            telemetryClient.connect(DiagnosticChannelConnectionString);

            telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
            diagnosticInfo = telemetryClient.receive();
    }

    private class RetriesTelemetryClient extends TelemetryClient {

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
}
