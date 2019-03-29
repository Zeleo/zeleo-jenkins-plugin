package io.zeleo.jenkins;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ZeleoUpdate {
	private String description;
	private Long duration;
	private Long estimatedDuration;
	private String externalId;
	private String displayName;
	private String buildId;
	private String searchURL;
	private Long startTime;
	private String timestamp;
    private String buildName;
    private String buildStatusUrl;
    private String event;
    private String dayOfTheWeek;
    private Integer weekOfTheMonth;
    private Integer dayOfTheMonth;
    private String month;
    private Integer year;
    private Integer hourOfTheDay;
    private Integer weekOfTheYear;
    private String listener = "true";
}
