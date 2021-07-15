load("@com_google_j2cl//build_defs:rules.bzl", "j2cl_library")

package(
    default_visibility = ["//visibility:public"],
)

j2cl_library(
    name = "parser-jacc",
    srcs = glob([
        "src/main/java/org/dhallj/parser/*.java",
        "target/jacc/*.java",
    ]),
    deps = [
        "//javascript/jre:java_io",
        "//javascript/jre:java_net",
        "//javascript/jre:java_nio_file",
        "//modules/core",
    ],
)
