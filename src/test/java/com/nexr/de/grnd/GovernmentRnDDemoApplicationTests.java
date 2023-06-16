package com.nexr.de.grnd;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

@SpringBootTest
@ActiveProfiles("local")
class GovernmentRnDDemoApplicationTests {

	@Test
	void contextLoads() throws FileNotFoundException, IOException, ApiException {
	    // file path to your KubeConfig

//	    String kubeConfigPath = System.getenv("HOME") + "/.kube/config";
//	    String kubeConfigPath = "C:\\dev\\eclipse_workspace\\GRND\\Government_RnD_Demo\\src\\main\\resources\\kube\\config";
	    String kubeConfigPath = "C:/dev/eclipse_workspace/GRND/Government_RnD_Demo/src/main/resources/kube/config";
//	    String kubeConfigPath = "classpath:/kube/config";

	    // loading the out-of-cluster config, a kubeconfig from file-system
	    ApiClient client =
	        ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();

	    // set the global default api-client to the in-cluster one from above
	    Configuration.setDefaultApiClient(client);

	    // the CoreV1Api loads default api-client from global configuration.
	    CoreV1Api api = new CoreV1Api();

	    // invokes the CoreV1Api client
	    V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);
	    for (V1Pod item : list.getItems()) {
	      System.out.println(item.getMetadata().getName() + " : " + item.getMetadata().getNamespace());
	    }
	  
	}

}
