package io.zeleo.jenkins;

import java.io.IOException;
import java.io.StringWriter;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Result;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
@Extension
public class BuildListener extends RunListener<AbstractBuild> {

	private final static ObjectMapper mapper;
	
	static {
        mapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        mapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
		SimpleFilterProvider filters = new SimpleFilterProvider().setFailOnUnknownId(false);
		mapper.setFilterProvider(filters);
    }
	
	public BuildListener() {
		super(AbstractBuild.class);
		log.info("Starting Zeleo Plugin");
	}
	
	@Override
    public void onStarted(AbstractBuild build, TaskListener listener) {
		ZeleoNotifier notifier = getBuildUpdate(build);
		log.info("Zeleo Running at Build Start");
		log.info("Agent: " + build.getBuiltOnStr());
		log.info("Description: " + build.getDescription());
		log.info("Duration: " + build.getDuration());
		log.info("Estimated Duration: " + build.getEstimatedDuration());
		log.info("External ID: " + build.getExternalizableId());
		log.info("Full Display Name: " + build.getFullDisplayName());
		log.info("Jenkins Version: " + build.getHudsonVersion());
		log.info("Build ID: " + build.getId());
		log.info("Search URL: " + build.getSearchUrl());
		log.info("Start Time: " + build.getStartTimeInMillis());
		log.info("Timestamp: " + build.getTimestampString());
		log.info("Log: " + build.getLogText());
		try {log.info("Summary: " + toJSON(build.getBuildStatusSummary()));} catch(IOException ex) {}

		if(notifier != null && notifier.isOnStart()) {
			ZeleoUpdate event = new ZeleoUpdate(build.getProject().getDisplayName(), build.getDisplayName(), 
					build.getBuildStatusUrl(), "START");
			fireEvent(event);
		}
	}
	
	@Override
    public void onCompleted(AbstractBuild build, @Nonnull TaskListener listener) {
		log.info("Running Zeleo Plugin on Build Complete");
		ZeleoNotifier notifier = getBuildUpdate(build);
		Result result = build.getResult();
		if(notifier != null && result != null) {
			ZeleoUpdate event = new ZeleoUpdate(build.getProject().getDisplayName(), build.getDisplayName(), 
					build.getBuildStatusUrl(), 
					result.toString());
			fireEvent(event);
		}
		
	}
	
	private ZeleoNotifier getBuildUpdate(AbstractBuild build) {
        for (Object update : build.getProject().getPublishersList().toMap().values()) {
            if (update instanceof ZeleoNotifier) {
                return (ZeleoNotifier) update;
            }
        }
        return null;
    }
	
	private String toJSON(final Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, obj);
		return writer.toString();
	}
	
	private void fireEvent(ZeleoUpdate event) {
		try {
			log.info(toJSON(event));
			System.out.println(toJSON(event));
		} catch(Exception ex) {
			log.error(ex.getMessage());
		}
	}
}
