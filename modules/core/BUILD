load("@com_google_j2cl//build_defs:rules.bzl", "j2cl_library")

package(
    default_visibility = ["//visibility:public"],
)

j2cl_library(
    name = "cbor",
    srcs = glob([
        "src/main/java/org/dhallj/cbor/*.java",
    ]),
)

j2cl_library(
    name = "core",
    srcs = glob([
        "src/main/java/org/dhallj/core/*.java",
        "src/main/java/org/dhallj/core/binary/*.java",
        "src/main/java/org/dhallj/core/normalization/*.java",
        "src/main/java/org/dhallj/core/typechecking/*.java",
    ]),
    deps = [
        ":cbor",
        "//javascript/jre:java_net",
        "//javascript/jre:java_nio_file",
    ],
)
