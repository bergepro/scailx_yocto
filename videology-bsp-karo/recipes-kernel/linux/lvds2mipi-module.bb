DESCRIPTION = "Kernel loadable module for Videology LVDS-MIPI converter"

require lvds2mipi.inc

S = "${WORKDIR}/git"

inherit module

do_install:append(){
    install -d ${D}${nonarch_base_libdir}/firmware/
    install -m 0644 ${S}/LVDS_MIPI* "${D}${nonarch_base_libdir}/firmware/"
}

FILES:${PN} += "${nonarch_base_libdir}/firmware/"

# KERNEL_MODULE_AUTOLOAD = "lvds2mipi_lvds2mipi"

RRECOMMENDS:${PN} += "python3-lvds2mipi"

