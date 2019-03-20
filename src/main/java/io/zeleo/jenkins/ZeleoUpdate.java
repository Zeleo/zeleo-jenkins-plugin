package io.zeleo.jenkins;

import lombok.Data;

@Data
public class ZeleoUpdate {
	private String projectName;
    private String buildName;
    private String buildStatusUrl;
    private String event;
	
	public ZeleoUpdate(String projectName, String buildName, String buildStatusUrl, String event) {
		this.projectName = projectName;
		this.buildName = buildName;
		this.buildStatusUrl = buildStatusUrl;
		this.event = event;
	}
}
