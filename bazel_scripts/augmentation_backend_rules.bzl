load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")


def augmentation_backend_rules():
    """Loads common dependencies needed to compile the protobuf library."""

    if not native.existing_rule("io_bazel_rules_docker"):
        http_archive(
            name = "io_bazel_rules_docker",
            sha256 = "85ffff62a4c22a74dbd98d05da6cf40f497344b3dbf1e1ab0a37ab2a1a6ca014",
            strip_prefix = "rules_docker-0.23.0",
            urls = ["https://github.com/bazelbuild/rules_docker/releases/download/v0.23.0/rules_docker-v0.23.0.tar.gz"],
        )

    if not native.existing_rule("rules_java"):
        git_repository(
            name = "rules_java",
            remote = "https://github.com/bazelbuild/rules_java.git",
            tag = "6.1.0",
        )

    if not native.existing_rule("rules_jvm_external"):
        RULES_JVM_EXTERNAL_TAG = "4.5"
        RULES_JVM_EXTERNAL_SHA = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6"

        http_archive(
            name = "rules_jvm_external",
            strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
            sha256 = RULES_JVM_EXTERNAL_SHA,
            url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
        )

    if not native.existing_rule("io_grpc_grpc_java"):
        git_repository(
            name = "io_grpc_grpc_java",
            remote = "https://github.com/grpc/grpc-java",
            tag = "v1.50.0",
        )

    if not native.existing_rule("com_github_grpc_grpc"):
        git_repository(
            name = "com_github_grpc_grpc",
            remote = "https://github.com/grpc/grpc",
            tag = "v1.45.2",
        )        