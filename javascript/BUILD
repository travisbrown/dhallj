load("@com_google_j2cl//build_defs:rules.bzl", "j2cl_application")

j2cl_application(
    name = "dhall",
    closure_defines = {"jre.classMetadata": "'STRIPPED'"},
    entry_points = ["dhall.js"],
    jre_checks_check_level = "MINIMAL",
    deps = ["//javascript/api:dhall_js"],
)
