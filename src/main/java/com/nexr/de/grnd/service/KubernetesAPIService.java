package com.nexr.de.grnd.service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexr.de.grnd.util.PropertiesUtil;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

@Service
public class KubernetesAPIService {
//	private static Logger LOGGER = LoggerFactory.getLogger(KubernetesAPIService.class);
	
	@Autowired
	private PropertiesUtil propertiesUtil;
	
	public AppsV1Api getAppsV1Api() throws FileNotFoundException, IOException {
		ApiClient client = this.getApiClient();
		Configuration.setDefaultApiClient(client);
		return new AppsV1Api(client);
	}
	
	private ApiClient getApiClient() throws FileNotFoundException, IOException {
		String kubeConfigPath = this.propertiesUtil.getKubernetesConfigPath();
		ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
		
		return client;
	}
}
