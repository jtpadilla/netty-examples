load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

def netty_examples_deps():
    #
    if "netty_examples_deps" not in native.existing_rules():
        git_repository(
            name = "speedycontrol_common",
            commit = "672532f07e166cc2b52a5a87785d30c0ed99f4a6",
            remote = "git@github.com:speedycontrol/speedycontrol-commons.git",
        )
