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
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.kubernetes.client.custom.Quantity;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Container;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.openapi.models.V1DeploymentBuilder;
import io.kubernetes.client.openapi.models.V1DeploymentSpec;
import io.kubernetes.client.openapi.models.V1LabelSelector;
import io.kubernetes.client.openapi.models.V1ObjectMeta;
import io.kubernetes.client.openapi.models.V1PodSpec;
import io.kubernetes.client.openapi.models.V1PodTemplateSpec;
import io.kubernetes.client.openapi.models.V1ResourceRequirements;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.wait.Wait;

public class Deploy {
	public static void main(String[] args) throws IOException, ApiException {
	
		//k8s API 인증서 로드
		String kubeConfigPath = "C:\\dev\\eclipse_workspace\\GRND\\Government_RnD_Demo\\src\\main\\resources\\kube\\config";
		ApiClient client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
		
		Configuration.setDefaultApiClient(client);
		AppsV1Api appsV1Api = new AppsV1Api(client);
	
		String name = "gdemoengine-img";//디플로이먼트 metadata의 name
		String imageName = "172.27.0.125:8443/gdemoengine-img";//도커 이미지 이름
//		String imageName = "nginx:latest";
		String namespace = "default";//k8s의 내부 클러스터 그룹핑용 이름
		//kubectl create namespace ian
		//하여 ian이라는 네임스페이스 등록
		//kubectl get namespace
		//조회
	
		// Create an example deployment (yaml 생성 - 샘플 소스 하단 참조)
		V1DeploymentBuilder deploymentBuilder = new V1DeploymentBuilder();
		deploymentBuilder.withApiVersion("apps/v1");
		deploymentBuilder.withKind("Deployment");
		
		//yaml의 metadata 영역 - 20230504 : docker image가 생성되면 다시 코딩해야 함
		V1ObjectMeta v1ObjectMeta = new V1ObjectMeta();
		v1ObjectMeta.name(name);
		Map<String, String> labels = new HashMap<String, String>();
		labels.put("app", name);
		v1ObjectMeta.labels(labels);
		deploymentBuilder.withMetadata(v1ObjectMeta);
		
		//yaml의 spec 영역
		V1DeploymentSpec v1DeploymentSpec = new V1DeploymentSpec();
		v1DeploymentSpec.setReplicas(1);//1개에 배포
		v1DeploymentSpec.setSelector(new V1LabelSelector().putMatchLabelsItem("app", name));
		
		V1PodTemplateSpec v1PodTemplateSpec = new V1PodTemplateSpec();
		v1PodTemplateSpec.setMetadata(new V1ObjectMeta().putLabelsItem("app", name));
		
		V1PodSpec v1PodSpec = new V1PodSpec();
		
//		V1Container v1Container = new V1Container();
//		v1Container.name("gdemoengine-run").image(imageName);
//		v1Container.resources(new V1ResourceRequirements().putLimitsItem("nvidia.com/gpu", new Quantity("1")));
//		v1PodSpec.setContainers(Collections.singletonList(v1Container));
		v1PodSpec.setContainers(Collections.singletonList(new V1Container().name("gdemoengine-run").image(imageName)));
		
		v1PodTemplateSpec.setSpec(v1PodSpec);
		v1DeploymentSpec.setTemplate(v1PodTemplateSpec);
		deploymentBuilder.withSpec(v1DeploymentSpec);
		
		
		V1Deployment v1Deployment = deploymentBuilder.build();
		System.out.println(v1Deployment.toString());
		
		//v1Deployment를 이용하여 k8s API로 디플로이먼트 요청
		appsV1Api.createNamespacedDeployment(namespace, v1Deployment, null, null, null, null);
	
		// 디플로이 완료까지 대기하는 코드
		Wait.poll(
			Duration.ofSeconds(3),
			Duration.ofSeconds(60),
			() -> {
			try {
				System.out.println("Waiting until example deployment is ready...");
				return appsV1Api
						.readNamespacedDeployment(name, namespace, null)
						.getStatus()
						.getReadyReplicas()
					> 0;
			} catch (ApiException e) {
				e.printStackTrace();
				return false;
			}
		});
		System.out.println("Created example deployment!");
	
		/*
		//서버에서 결과 확인
root@rndMaster2:~# kubectl get pods -o wide
NAME                               READY   STATUS    RESTARTS   AGE   IP             NODE           NOMINATED NODE   READINESS GATES
gdemoengine-img-66b5c89cdb-l9nzx   1/1     Running   0          55s   10.100.0.246   rndworker201   <none>           <none>

root@rndMaster2:~# docker images
REPOSITORY                          TAG       IMAGE ID       CREATED       SIZE
172.27.0.125:8443/gdemoengine-img   latest    866ecc2aad17   8 days ago    605MB
gdemoengine-img                     latest    866ecc2aad17   8 days ago    605MB
registry                            2         8db46f9d7550   3 weeks ago   24.2MB

		 */
	}
}



/*
{
    apiVersion: apps/v1
    kind: Deployment
    metadata: class V1ObjectMeta {
        annotations: null
        creationTimestamp: null
        deletionGracePeriodSeconds: null
        deletionTimestamp: null
        finalizers: null
        generateName: null
        generation: null
        labels: {app=gdemoengine-img}
        managedFields: null
        name: gdemoengine-img
        namespace: null
        ownerReferences: null
        resourceVersion: null
        selfLink: null
        uid: null
    }
    spec: class V1DeploymentSpec {
        minReadySeconds: null
        paused: null
        progressDeadlineSeconds: null
        replicas: 1
        revisionHistoryLimit: null
        selector: class V1LabelSelector {
            matchExpressions: null
            matchLabels: {app=gdemoengine-img}
        }
        strategy: null
        template: class V1PodTemplateSpec {
            metadata: class V1ObjectMeta {
                annotations: null
                creationTimestamp: null
                deletionGracePeriodSeconds: null
                deletionTimestamp: null
                finalizers: null
                generateName: null
                generation: null
                labels: {app=gdemoengine-img}
                managedFields: null
                name: null
                namespace: null
                ownerReferences: null
                resourceVersion: null
                selfLink: null
                uid: null
            }
            spec: class V1PodSpec {
                activeDeadlineSeconds: null
                affinity: null
                automountServiceAccountToken: null
                containers: [class V1Container {
                    args: null
                    command: null
                    env: null
                    envFrom: null
                    image: 172.27.0.125:8443/gdemoengine-img
                    imagePullPolicy: null
                    lifecycle: null
                    livenessProbe: null
                    name: gdemoengine-run
                    ports: null
                    readinessProbe: null
                    resources: null
                    securityContext: null
                    startupProbe: null
                    stdin: null
                    stdinOnce: null
                    terminationMessagePath: null
                    terminationMessagePolicy: null
                    tty: null
                    volumeDevices: null
                    volumeMounts: null
                    workingDir: null
                }]
                dnsConfig: null
                dnsPolicy: null
                enableServiceLinks: null
                ephemeralContainers: null
                hostAliases: null
                hostIPC: null
                hostNetwork: null
                hostPID: null
                hostUsers: null
                hostname: null
                imagePullSecrets: null
                initContainers: null
                nodeName: null
                nodeSelector: null
                os: null
                overhead: null
                preemptionPolicy: null
                priority: null
                priorityClassName: null
                readinessGates: null
                restartPolicy: null
                runtimeClassName: null
                schedulerName: null
                securityContext: null
                serviceAccount: null
                serviceAccountName: null
                setHostnameAsFQDN: null
                shareProcessNamespace: null
                subdomain: null
                terminationGracePeriodSeconds: null
                tolerations: null
                topologySpreadConstraints: null
                volumes: null
            }
        }
    }
    status: null
}
*/