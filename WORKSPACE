#WORKSPACE
workspace(name = "augmentation_backend")

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

load("//bazel_scripts:augmentation_backend_rules.bzl", "augmentation_backend_rules")
augmentation_backend_rules()


# grpc
# GRPC
load("@com_github_grpc_grpc//bazel:grpc_deps.bzl", "grpc_deps")
grpc_deps()

load("@com_github_grpc_grpc//bazel:grpc_extra_deps.bzl", "grpc_extra_deps")
grpc_extra_deps()

load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")

grpc_java_repositories()

load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")

protobuf_deps()


# Docker
# Download the rules_docker repository at release v0.14.4
load(
    "@io_bazel_rules_docker//repositories:repositories.bzl",
    container_repositories = "repositories",
)
container_repositories()

load("@io_bazel_rules_docker//repositories:deps.bzl", container_deps = "deps")

container_deps()

load("@io_bazel_rules_docker//container:container.bzl", "container_pull")
container_pull(
    name = "augmentation-backend-base",
    registry = "docker.io",
    repository = "cpark90/openjdk11",
    tag = "jre-11.0.19_7-ubuntu-docker",
)


# maven

load("//bazel_scripts:augmentation_backend_java_deps.bzl", "augmentation_backend_java_deps")
augmentation_backend_java_deps()

load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_ARTIFACTS")
load("@io_grpc_grpc_java//:repositories.bzl", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS")
load("@com_google_protobuf//:protobuf_deps.bzl", "PROTOBUF_MAVEN_ARTIFACTS")
load("@rules_jvm_external//:defs.bzl", "maven_install")
maven_install(
    artifacts = IO_GRPC_GRPC_JAVA_ARTIFACTS + PROTOBUF_MAVEN_ARTIFACTS,
    generate_compat_repositories = True,
    override_targets = IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS,
    repositories = [
        "https://repo.maven.apache.org/maven2/",
    ],
    strict_visibility = True,
)

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()