package io.zeleo.jenkins;

import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

public class BuildListener extends RunListener<AbstractBuild> {
	
	public BuildListener() {
		super(AbstractBuild.class);
	}
	
	@Override
    public void onStarted(AbstractBuild build, TaskListener listener) {
		ZeleoNotifier update = getBuildUpdate(build);
		if(update != null && update.isOnStart()) {
			ZeleoUpdate event = new ZeleoUpdate(build.getProject().getDisplayName(), build.getDisplayName(), build.getBuildStatusUrl(), "START");
		}
		// post the event.
	}
	
	private ZeleoNotifier getBuildUpdate(AbstractBuild build) {
        for (Object update : build.getProject().getPublishersList().toMap().values()) {
            if (update instanceof ZeleoNotifier) {
                return (ZeleoNotifier) update;
            }
        }
        return null;
    }
}
