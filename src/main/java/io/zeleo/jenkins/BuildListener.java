package io.zeleo.jenkins;

import javax.annotation.Nonnull;

import hudson.model.AbstractBuild;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;

@SuppressWarnings("rawtypes")
public class BuildListener extends RunListener<AbstractBuild> {
	
	public BuildListener() {
		super(AbstractBuild.class);
	}
	
	@Override
    public void onStarted(AbstractBuild build, TaskListener listener) {
		ZeleoNotifier notifier = getBuildUpdate(build);
		if(notifier != null && notifier.isOnStart()) {
			ZeleoUpdate event = new ZeleoUpdate(build.getProject().getDisplayName(), build.getDisplayName(), 
					build.getBuildStatusUrl(), "START");
			fireEvent(event);
		}
	}
	
	@Override
    public void onCompleted(AbstractBuild build, @Nonnull TaskListener listener) {
		ZeleoNotifier notifier = getBuildUpdate(build);
		if(notifier != null || build == null || build.getResult() == null) {
			ZeleoUpdate event = new ZeleoUpdate(build.getProject().getDisplayName(), build.getDisplayName(), 
					build.getBuildStatusUrl(), build.getResult().toString());
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
	
	private void fireEvent(ZeleoUpdate event) {
		// TODO: code the event.
	}
}
