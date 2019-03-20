package io.zeleo.jenkins;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ZeleoUpdate {
	private String projectName;
    private String buildName;
    private String buildStatusUrl;
    private String event;
}
