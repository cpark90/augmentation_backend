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
import io.kubernetes.client.openapi.models.V1Status;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

public class DeployDelete {
	public static void main(String[] args) throws IOException, ApiException {
	
		//k8s API 인증서 로드
		String kubeConfigPath = "C:\\dev\\eclipse_workspace\\GRND\\Government_RnD_Demo\\src\\main\\resources\\kube\\config";
		ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
		
		
		Configuration.setDefaultApiClient(client);
		AppsV1Api appsV1Api = new AppsV1Api(client);
	
		String name = "gdemoengine-img";//디플로이먼트 metadata의 name
//		String name = "gdemoengine-imgg";//디플로이먼트 metadata의 name
//		String imageName = "172.27.0.125:8443/gdemoengine-img";
//		String imageName = "nginx:latest";
		String namespace = "default";//k8s의 내부 클러스터 그룹핑용 이름
		try {
			//
			V1Status v1Status = appsV1Api.deleteNamespacedDeployment(name, namespace, null, null, null, null, null, null);
			System.out.println(v1Status.toString());
		}catch(ApiException e) {
			//code : 404 : 어플리케이션 없음, 401: 권한 없음, 422: 도커 이미지를 찾을 수 없음?
			System.out.println("code:" + e.getCode());
			System.out.println("message:" + e.getMessage());
			System.out.println("responseBody:" + e.getResponseBody());
			System.out.println("responseHeaders:" + e.getResponseHeaders());
			System.out.println("localizedMessage:" + e.getLocalizedMessage());
			/*
-------------1------------
404
-------------2------------
{"kind":"Status","apiVersion":"v1","metadata":{},"status":"Failure","message":"deployments.apps \"gdemoengine-img\" not found","reason":"NotFound","details":{"name":"gdemoengine-img","group":"apps","kind":"deployments"},"code":404}
-------------3------------
{audit-id=[1d91100e-9ae8-4d08-8f57-d6f24c16990a], cache-control=[no-cache, private], content-length=[232], content-type=[application/json], date=[Tue, 11 Apr 2023 02:06:31 GMT], x-kubernetes-pf-flowschema-uid=[c938a0de-3636-4e5c-9d75-f6994987d016], x-kubernetes-pf-prioritylevel-uid=[4eb2ad9a-020a-4566-9737-868e4003526c]}
-------------4------------


			*/
		}
	}
}
