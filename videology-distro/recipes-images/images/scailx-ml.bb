DESCRIPTION = "Scailx swupdate container image"

inherit scailx-swupdate-image
inherit kernel_devel

DEFAULT_DTB = "scailx_karo_cameras.dtb"

IMAGE_FEATURES += " \
    tools-profile \
    bash-completion-pkgs \
    ssh-server-openssh \
    hwcodecs \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'weston', bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11-base x11-sato', '', d), d)} \
"
# read-only-rootfs
# tools-sdk
# tools-debug

CONFLICT_DISTRO_FEATURES = "directfb"

# Add opencv for i.MX GPU
OPENCV_PKGS       ?= ""
OPENCV_PKGS:imxgpu = " \
    opencv-apps \
    python3-opencv \
"

# DOCKER ?= "podman podman-compose catatonit fuse-overlayfs podman-tui"
DOCKER ?= "docker docker-compose fuse-overlayfs "

# slirp4netns

IMAGE_INSTALL += " \
    ${OPENCV_PKGS} \
    packagegroup-ml-demoless \
	openssh-sftp-server \
    gst-perf \
    fuse-exfat \
	xauth \
    gnupg \
    dtc \
    lsof \
    i2c-tools \
    pyhailort \
    hailo-pci \
    hailortcli \
    libgsthailo \
    libhailort \
    hailo-firmware \
    v4l-utils \
    media-ctl \
    v4l2loopback \
    v4l2loopback-utils \
    python3-pyserial \
    gst-variable-rtsp-server \
    lvds2mipi-module \
    python3-lvds2mipi \
    kernel-module-gs-ar0234 \
    python3-ar0234 \
    packagegroup-imx-core-tools \
    packagegroup-imx-security \
    curl \
    nodejs \
    nodejs-npm \
    git \
    go2rtc \
    gputop \
    ser2net \
    ffmpeg \
    python3-pycairo \
    python3-pip \
    python3-periphery \
    ${DOCKER} \
"

IMAGE_INSTALL += " ${@bb.utils.contains('SCAILX_SOURCES', 'yes', 'scailx-gst-plugins scailx-ai-portal', '', d)} "

CORE_IMAGE_EXTRA_INSTALL += " \
    packagegroup-core-full-cmdline \
    packagegroup-imx-isp \
    packagegroup-imx-security \
    packagegroup-fsl-gstreamer1.0 \
    packagegroup-fsl-gstreamer1.0-full \
    packagegroup-fsl-gstreamer1.0-commercial \
    gstreamer1.0-python \
    firmwared \
    ${@bb.utils.contains('DISTRO_FEATURES', 'x11 wayland', 'weston-xwayland xterm', '', d)} \
"

TARGET_HOSTNAME ?= "scailx-ai"

ROOTFS_POSTPROCESS_COMMAND:append = " hostname_change; "
hostname_change () {
    echo "${TARGET_HOSTNAME}" > ${IMAGE_ROOTFS}${sysconfdir}/hostname
}

ROOTFS_POSTPROCESS_COMMAND:append = " cleanup_dirs; "
cleanup_dirs () {
    rm -rf ${IMAGE_ROOTFS}/usr/lib/python3.10/site-packages/torch/test/
}

