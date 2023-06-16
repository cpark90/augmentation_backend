/*
Copyright 2020 The Kubernetes Authors.
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

import java.io.File;
import java.io.IOException;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.models.V1Deployment;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.util.Yaml;

/**
 * A simple example of how to parse a Kubernetes object.
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.YamlExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
public class YamlExample {
  public static void main(String[] args) throws IOException, ApiException, ClassNotFoundException {
    // Example yaml file can be found in $REPO_DIR/test-svc.yaml
    File file = new File("C:\\dev\\eclipse_workspace\\GRND\\Government_RnD_Demo\\src\\main\\resources\\kube\\gdemoengine-img.yaml");
    V1Deployment yamlDeployment = (V1Deployment) Yaml.load(file);
    // Deployment and StatefulSet is defined in apps/v1, so you should use AppsV1Api instead of
    // CoreV1API
//    CoreV1Api api = new CoreV1Api();
    ApiClient client = Config.defaultClient();
    Configuration.setDefaultApiClient(client);
    AppsV1Api appsV1Api = new AppsV1Api(client);
//    V1Service createResult = api.createNamespacedService("default", yamlSvc, null, null, null, null);
    V1Deployment createResult = appsV1Api.createNamespacedDeployment("default", yamlDeployment, null, null, null, null);

    System.out.println(createResult);

//    V1Service deleteResult =
//        api.deleteNamespacedService(
//            yamlSvc.getMetadata().getName(),
//            "default",
//            null,
//            null,
//            null,
//            null,
//            null,
//            new V1DeleteOptions());
//    System.out.println(deleteResult);
  }
}
