# Class that creates a zip file runnable with NXP's new UUU utility.
# The zip contains a copy of uuu program for convenience, the
# uuu script, card-image to flash and a fastboot compatible u-boot
# image to run on the device.
# USAGE:
#       uuu <image.zip>
# This will run the utility, which will chech for compatible devices
# (NXP imx devices in SDP mode), and will start flashing to the image.
#       uuu -d <image.zip>
# This will run in daemon mode (Runs continuously). Whenever a new
# compatible device is detected, it will start flashing it.

inherit image_types

UUU_VERSION = "1.2.91"

UBOOT_UUU ?= "${DEPLOY_DIR_IMAGE}/imx-boot-karo-mfg"
UBOOT     ?= "${DEPLOY_DIR_IMAGE}/imx-boot"

uuuimg_proc () {
    cd ${IMGDEPLOYDIR}
    ZIP_FOLDER=$(mktemp -d -t uuuimg-XXXXXX)

    for f in $UUU $1 ${UBOOT_UUU} ${UBOOT}; do
        install "$f" ${ZIP_FOLDER}
    done

    uboot=`basename ${UBOOT}`
    uboot_uuu=`basename ${UBOOT_UUU}`
    image=`basename $1`

    echo "uuu_version ${UUU_VERSION}

SDPS: boot -f ${uboot_uuu}
FB: flash -raw2sparse all ${image}
FB: flash bootloader ${uboot}
### Set default u-boot environment
FB: ucmd env default -a
FB: ucmd setenv bootdelay 1
FB: ucmd saveenv
FB: ucmd mmc partconf \${emmc_dev} \${emmc_boot_ack} 1 0
FB: done
" > "${ZIP_FOLDER}/uuu.auto"
    zip -r -1 -j "$2" "${ZIP_FOLDER}"
    rm -rf "${ZIP_FOLDER}"
}

CONVERSIONTYPES += "uuuimg"
CONVERSION_DEPENDS_uuuimg = "parted-native zip-native ${IMAGE_BOOTLOADER} virtual/kernel"
CONVERSION_CMD:uuuimg="uuuimg_proc ${IMAGE_NAME}.${type} ${IMAGE_NAME}${IMAGE_NAME_SUFFIX}.zip"

