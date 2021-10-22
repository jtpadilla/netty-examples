workspace(name = "netty_examples")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "3.1"

RULES_JVM_EXTERNAL_SHA = "e246373de2353f3d34d35814947aa8b7d0dd1a58c2f7a6c41cfeaff3007c2d14"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("//bazel:netty_examples_deps.bzl", "netty_examples_deps")

netty_examples_deps()

load("@speedycontrol_common//bazel:speedycontrol_common_deps.bzl", "speedycontrol_common_deps")

speedycontrol_common_deps()

http_archive(
    name = "openjdk15_linux_archive",
    build_file_content = """
java_runtime(name = 'runtime', srcs =  glob(['**']), visibility = ['//visibility:public'])
exports_files(["WORKSPACE"], visibility = ["//visibility:public"])
""",
    sha256 = "91ac6fc353b6bf39d995572b700e37a20e119a87034eeb939a6f24356fbcd207",
    strip_prefix = "jdk-15.0.2",
    urls = ["https://download.java.net/java/GA/jdk15.0.2/0d1cfde4252546c6931946de8db48ee2/7/GPL/openjdk-15.0.2_linux-x64_bin.tar.gz"],
)

http_archive(
    name = "remote_java_tools",
    sha256 = "b763ee80e5754e593fd6d5be6d7343f905bc8b73d661d36d842b024ca11b6793",
    urls = [
        "https://mirror.bazel.build/bazel_java_tools/releases/java/v11.5/java_tools-v11.5.zip",
        "https://github.com/bazelbuild/java_tools/releases/download/java_v11.5/java_tools-v11.5.zip",
    ],
)
