package io.onedev.server.web.websocket;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.onedev.commons.launcher.loader.Listen;
import io.onedev.server.event.build.BuildEvent;
import io.onedev.server.model.Build;
import io.onedev.server.model.Project;
import io.onedev.server.web.util.WicketUtils;

@Singleton
public class BuildEventBroadcaster {
	
	private final WebSocketManager webSocketManager;
	
	@Inject
	public BuildEventBroadcaster(WebSocketManager webSocketManager) {
		this.webSocketManager = webSocketManager;
	}

	@Listen
	public void on(BuildEvent event) {
		PageKey pageKey = WicketUtils.getPageKey();
		
		Project project = event.getProject();
		Build build = event.getBuild();
		
		webSocketManager.notifyObservableChange(Build.getWebSocketObservable(build.getId()), pageKey);
		
		String observable = "commit-status:" + project.getId() + ":" + build.getCommitHash();
		webSocketManager.notifyObservableChange(observable, pageKey);
		
		observable = "job-status:" + project.getId() + ":" + build.getCommitHash() + ":" + build.getJobName();
		webSocketManager.notifyObservableChange(observable, pageKey);
	}

}