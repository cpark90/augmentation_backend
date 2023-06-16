/*
Copyright 2022 The Kubernetes Authors.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.nexr.de.grnd;

import java.io.FileReader;
import java.io.IOException;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class DeployStatus {
	public static void main(String[] args) throws IOException, ApiException {
		
		//k8s API 인증서 로드
		String kubeConfigPath = "C:\\dev\\eclipse_workspace\\GRND\\Government_RnD_Demo\\src\\main\\resources\\kube\\config";
		ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
		
		
		Configuration.setDefaultApiClient(client);
		AppsV1Api appsV1Api = new AppsV1Api(client);
	
		
		String name = "gdemoengine-img";//디플로이먼트 metadata의 name
//		String imageName = "172.27.0.125:8443/gdemoengine-img";
//		String imageName = "nginx:latest";
		String namespace = "default";//k8s의 내부 클러스터 그룹핑용 이름
		try {
			V1Deployment v1Deployment = appsV1Api.readNamespacedDeploymentStatus(name, namespace, null);
			//배포된 것이 있으면 아래 정보 출력
			System.out.println(v1Deployment.getSpec().getReplicas());
			System.out.println(v1Deployment.getStatus().getReadyReplicas());
		}catch(ApiException e) {
			//code : 404 : 어플리케이션 없음, 401: 권한 없음, 422: 도커 이미지를 찾을 수 없음?
			System.out.println("code:" + e.getCode());
			System.out.println("message:" + e.getMessage());
			System.out.println("responseBody:" + e.getResponseBody());
			System.out.println("responseHeaders:" + e.getResponseHeaders());
		}
		
		
	}
}
