package io.zeleo.jenkins;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;

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
import io.zeleo.jenkins.connection.SSLManager;
import io.zeleo.jenkins.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("rawtypes")
@Slf4j
@Extension
public class BuildListener extends RunListener<AbstractBuild> {

	private final static ObjectMapper mapper;
	private final static String ZELEO_LAMBDA = "https://applications.zeleo.io/applications/jenkins/event";
	private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
	
	/**
	 * Set up the service.
	 */
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
	
	/**
	 * Run on started if configured.
	 */
	@Override
    public void onStarted(AbstractBuild build, TaskListener listener) {
		ZeleoNotifier notifier = getBuildUpdate(build);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTimeInMillis(build.getStartTimeInMillis());
		startCalendar.get(Calendar.DAY_OF_WEEK);
		startCalendar.get(Calendar.WEEK_OF_MONTH);
		startCalendar.get(Calendar.DAY_OF_MONTH);
		startCalendar.get(Calendar.MONTH);
		startCalendar.get(Calendar.YEAR);
		startCalendar.get(Calendar.HOUR_OF_DAY);
		startCalendar.get(Calendar.WEEK_OF_YEAR);
		log.info("Zeleo Running at Build Start");
		if(notifier != null && notifier.isOnStart()) {
			ZeleoUpdate event = new ZeleoUpdate(build.getDescription(), build.getDuration(), build.getEstimatedDuration(), 
					build.getExternalizableId(), build.getFullDisplayName(), build.getId(), build.getSearchUrl(), 
					build.getStartTimeInMillis(), build.getTimestampString(), build.getDisplayName(), 
					build.getBuildStatusUrl(), "START", DateUtils.getDay(startCalendar.get(Calendar.DAY_OF_WEEK)), 
					startCalendar.get(Calendar.WEEK_OF_MONTH), startCalendar.get(Calendar.DAY_OF_MONTH), 
					DateUtils.getMonth(startCalendar.get(Calendar.MONTH)), startCalendar.get(Calendar.YEAR), 
					startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.WEEK_OF_YEAR), "true");
			fireEvent(event);
		}
	}
	
	/**
	 * Handle the build completed event.
	 */
	@Override
    public void onCompleted(AbstractBuild build, @Nonnull TaskListener listener) {
		log.info("Running Zeleo Plugin on Build Complete");
		ZeleoNotifier notifier = getBuildUpdate(build);
		Result result = build.getResult();
		if(notifier != null && result != null) {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTimeInMillis(build.getStartTimeInMillis());
			ZeleoUpdate event = new ZeleoUpdate(build.getDescription(), build.getDuration(), build.getEstimatedDuration(), 
					build.getExternalizableId(), build.getFullDisplayName(), build.getId(), build.getSearchUrl(), 
					build.getStartTimeInMillis(), build.getTimestampString(), build.getDisplayName(), 
					build.getBuildStatusUrl(), result.toString(), DateUtils.getDay(startCalendar.get(Calendar.DAY_OF_WEEK)), 
					startCalendar.get(Calendar.WEEK_OF_MONTH), startCalendar.get(Calendar.DAY_OF_MONTH), 
					DateUtils.getMonth(startCalendar.get(Calendar.MONTH)), startCalendar.get(Calendar.YEAR), 
					startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.WEEK_OF_YEAR), "true");
			fireEvent(event);
		}
		
	}
	
	/**
	 * Grab the build update from the notification.
	 * 
	 * @param build The build object.
	 * @return The Zeleo build notification.
	 */
	private ZeleoNotifier getBuildUpdate(AbstractBuild build) {
        for (Object update : build.getProject().getPublishersList().toMap().values()) {
            if (update instanceof ZeleoNotifier) {
                return (ZeleoNotifier) update;
            }
        }
        return null;
    }
	
	/**
	 * Convert an object to a JSON string.
	 * 
	 * @param obj The object.
	 * @return The json string.
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private String toJSON(final Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, obj);
		return writer.toString();
	}
	
	/**
	 * Fire the event to the Zeleo service.
	 * 
	 * @param event The object with event data.
	 */
	private void fireEvent(ZeleoUpdate event) {
		try {
			final String json = toJSON(event);
			OkHttpClient client = SSLManager.trustZeleoSslClient(new OkHttpClient());
	        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
	        Request request = new Request.Builder().url(ZELEO_LAMBDA).post(body).build();
	        Response response = client.newCall(request).execute();
	        log.info("Event Posted to Zeleo: " + response.message()); 
		} catch(Exception ex) {
			log.error(ex.getMessage());
			log.error(ex.getCause().getMessage());
		}
	}
}
