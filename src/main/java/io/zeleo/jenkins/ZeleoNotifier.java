package io.zeleo.jenkins;

import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import lombok.Data;

@Data
public class ZeleoNotifier extends Notifier {
	
	private boolean onStart;
	private boolean onSuccess;
	private boolean onFailure;
	
	@DataBoundConstructor
    public ZeleoNotifier(boolean onStart, boolean onSuccess, boolean onFailure) {
		super();
		this.onStart = onStart;
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;
	}

	@Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }
	
	@Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        return true;
    }

    @Override
    public ZeleoNotifierDescriptor getDescriptor() {
        return (ZeleoNotifierDescriptor) super.getDescriptor();
    }
	
	@Extension
    public static class ZeleoNotifierDescriptor extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "Zeleo Build Event";
        }
    }

}
