load("@io_bazel_rules_closure//closure:defs.bzl", "closure_js_library")
load("@com_google_j2cl//build_defs:rules.bzl", "j2cl_library")

package(
    default_visibility = ["//visibility:public"],
)

j2cl_library(
    name = "dhall_js_java",
    srcs = ["DhallJs.java"],
    deps = [
        "//modules/core",
        "//modules/parser",
    ],
)

closure_js_library(
    name = "dhall_js",
    srcs = ["dhall.js"],
    deps = [":dhall_js_java"],
)
