load("@com_google_j2cl//build_defs:rules.bzl", "j2cl_library")

package(
    default_visibility = ["//visibility:public"],
)

j2cl_library(
    name = "parser",
    srcs = glob([
        "src/main/java/org/dhallj/parser/*.java",
        "src/main/java/org/dhallj/parser/support/*.java",
        "target/javacc/*.java",
    ]),
    deps = [
        "//javascript/jre:java_io",
        "//javascript/jre:java_net",
        "//javascript/jre:java_nio_file",
        "//modules/core",
    ],
)
