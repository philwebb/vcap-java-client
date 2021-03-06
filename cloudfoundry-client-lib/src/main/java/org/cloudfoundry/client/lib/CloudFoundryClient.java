/*
 * Copyright 2009-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cloudfoundry.client.lib;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudApplication.DebugMode;
import org.cloudfoundry.client.lib.archive.ApplicationArchive;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.cloudfoundry.client.lib.rest.CloudControllerClient;
import org.cloudfoundry.client.lib.rest.CloudControllerClientFactory;
import org.cloudfoundry.client.lib.domain.ApplicationStats;
import org.cloudfoundry.client.lib.domain.CloudInfo;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CrashesInfo;
import org.cloudfoundry.client.lib.domain.InstancesInfo;
import org.cloudfoundry.client.lib.domain.ServiceConfiguration;
import org.cloudfoundry.client.lib.domain.Staging;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.Assert;

/**
 * A Java client to exercise the Cloud Foundry API.
 *
 * @author Ramnivas Laddad
 * @author A.B.Srinivasan
 * @author Jennifer Hickey
 * @author Dave Syer
 * @author Thomas Risberg
 */
@SuppressWarnings("unused")
public class CloudFoundryClient implements CloudFoundryOperations {

	private CloudControllerClient cc;

	private CloudInfo info;

	/**
	 * Construct client for anonymous user. Useful only to get to the '/info' endpoint.
	 */
	public CloudFoundryClient(URL cloudControllerUrl) throws MalformedURLException {
		this(null, cloudControllerUrl, null);
	}

	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl) throws MalformedURLException {
		this(credentials, cloudControllerUrl, null);
	}

	public CloudFoundryClient(CloudCredentials credentials, URL cloudControllerUrl, CloudSpace sessionSpace) {
		Assert.notNull(cloudControllerUrl, "URL for cloud controller cannot be null");
		CloudControllerClientFactory cloudControllerClientFactory = new CloudControllerClientFactory();
		this.cc = cloudControllerClientFactory.newCloudController(cloudControllerUrl, credentials, sessionSpace);
    }

	public URL getCloudControllerUrl() {
		return cc.getCloudControllerUrl();
	}

	public CloudInfo getCloudInfo() {
		if (info == null) {
			info = cc.getInfo();
		}
		return info;
	}

	public boolean supportsSpaces() {
		return cc.supportsSpaces();
	}

	public List<CloudSpace> getSpaces() {
		return cc.getSpaces();
	}

	public void register(String email, String password) {
		cc.register(email, password);
	}

	@SuppressWarnings("unchecked")
	public void updatePassword(String newPassword) {
		cc.updatePassword(newPassword);
	}

	public void unregister() {
		cc.unregister();
	}

	@SuppressWarnings("unchecked")
	public String login() {
		return cc.login();
	}

	public void logout() {
		cc.logout();
	}

	@SuppressWarnings("unchecked")
	public List<CloudApplication> getApplications() {
		return cc.getApplications();
	}

	@SuppressWarnings("unchecked")
	public CloudApplication getApplication(String appName) {
		return cc.getApplication(appName);
	}

	public ApplicationStats getApplicationStats(String appName) {
		return cc.getApplicationStats(appName);
	}

	public int[] getApplicationMemoryChoices() {
		return cc.getApplicationMemoryChoices();
	}

	public int getDefaultApplicationMemory(String framework) {
		return cc.getDefaultApplicationMemory(framework);
	}

	public void createApplication(String appName, Staging staging, int memory, List<String> uris,
								  List<String> serviceNames) {
		cc.createApplication(appName, staging, memory, uris, serviceNames, false);
	}

	public void createApplication(String appName, String framework, int memory, List<String> uris,
								  List<String> serviceNames) {
		cc.createApplication(appName, new Staging(framework), memory, uris, serviceNames, false);
	}

	public void createApplication(String appName, String framework, int memory, List<String> uris,
								  List<String> serviceNames, boolean checkExists) {
		cc.createApplication(appName, new Staging(framework), memory, uris, serviceNames, checkExists);
	}

	public void createApplication(String appName, Staging staging, int memory, List<String> uris,
								  List<String> serviceNames, boolean checkExists) {
		cc.createApplication(appName, staging, memory, uris, serviceNames, checkExists);
	}

	public void createService(CloudService service) {
		cc.createService(service);
	}


    public void uploadApplication(String appName, String file) throws IOException {
        cc.uploadApplication(appName, new File(file), null);
    }

    public void uploadApplication(String appName, File file) throws IOException {
        cc.uploadApplication(appName, file, null);
    }

    public void uploadApplication(String appName, File file, UploadStatusCallback callback) throws IOException {
		cc.uploadApplication(appName, file, callback);
    }

    public void uploadApplication(String appName, ApplicationArchive archive) throws IOException {
        cc.uploadApplication(appName, archive, null);
    }

    public void uploadApplication(String appName, ApplicationArchive archive, UploadStatusCallback callback) throws IOException {
		cc.uploadApplication(appName, archive, callback);
    }


	public void startApplication(String appName) {
		cc.startApplication(appName);
	}

	public void debugApplication(String appName, DebugMode mode) {
		cc.debugApplication(appName, mode);
	}

	public void stopApplication(String appName) {
		cc.stopApplication(appName);
	}

	public void restartApplication(String appName) {
		cc.restartApplication(appName);
	}

	public void deleteApplication(String appName) {
		cc.deleteApplication(appName);
	}

	public void deleteAllApplications() {
		cc.deleteAllApplications();
	}

	public void deleteAllServices() {
		cc.deleteAllServices();
	}

	public void updateApplicationMemory(String appName, int memory) {
		cc.updateApplicationMemory(appName, memory);
	}

	public void updateApplicationInstances(String appName, int instances) {
		cc.updateApplicationInstances(appName, instances);
	}

	public void updateApplicationServices(String appName, List<String> services) {
		cc.updateApplicationServices(appName, services);
	}

	public void updateApplicationUris(String appName, List<String> uris) {
		cc.updateApplicationUris(appName, uris);
	}

	public void updateApplicationEnv(String appName, Map<String, String> env) {
		cc.updateApplicationEnv(appName, env);
	}

	public void updateApplicationEnv(String appName, List<String> env) {
		cc.updateApplicationEnv(appName, env);
	}

	public String getFile(String appName, int instanceIndex, String filePath) {
		return cc.getFile(appName, instanceIndex, filePath);
	}

	// list services, un/provision services, modify instance

	public List<CloudService> getServices() {
		return cc.getServices();
	}

	public CloudService getService(String service) {
		return cc.getService(service);
	}

	public void deleteService(String service) {
		cc.deleteService(service);
	}

	@SuppressWarnings("unchecked")
	public List<ServiceConfiguration> getServiceConfigurations() {
		return cc.getServiceConfigurations();
	}

	public void bindService(String appName, String serviceName) {
		cc.bindService(appName, serviceName);
	}

	public void unbindService(String appName, String serviceName) {
		cc.unbindService(appName, serviceName);
	}

	public InstancesInfo getApplicationInstances(String appName) {
		return cc.getApplicationInstances(appName);
	}

	public CrashesInfo getCrashes(String appName) {
		return cc.getCrashes(appName);
	}

	public void rename(String appName, String newName) {
		cc.rename(appName, newName);
	}

}
