DESCRIPTION = "Python helper scripts for the Videology LVDS to MIPI converter"

require lvds2mipi.inc
inherit python_setuptools_build_meta
DEPENDS += "python3-setuptools-scm-native"

FILES:${PN} += "${base_bindir}/"

RDEPENDS:${PN} += "\
    ${PYTHON_PN}-fcntl \
    ${PYTHON_PN}-ctypes \
"