package com.refapp.tester.services

import android.content.Context
import com.refapp.tester.models.DeviceInfo

class DeviceInfoFactory(val context: Context) {

    fun getDeviceInfo() : List<DeviceInfo> {
        val list = ArrayList<DeviceInfo>()
        val pckInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val appInfo = context.applicationInfo

        list.add(DeviceInfo(
            "Package Name",
            "Return the name of this application's package..",
            context.packageName))

        list.add(DeviceInfo(
            "Min SDK Version",
            "The minimum SDK version this application can run on.",
            appInfo.minSdkVersion.toString()))

        list.add(DeviceInfo(
            "Target SDK Version",
            "The minimum SDK version this application targets.",
            appInfo.targetSdkVersion.toString()))

        list.add(DeviceInfo(
            "Data Directory",
            "Full path to the default directory assigned to the package for its persistent data.",
            appInfo.dataDir))

        if (pckInfo.permissions != null && pckInfo.permissions.any()){
            list.add(DeviceInfo(
                "Permissions",
                "Array of all <permission> tags included under <manifest>, or null if there were none.",
                pckInfo.permissions.joinToString()))

        }
        list.add(DeviceInfo(
            "Code Name",
            "The current development codename, or the string \"REL\" if this is a release build.",
            android.os.Build.VERSION.CODENAME))

        list.add(DeviceInfo(
            "Incremental",
            "The internal value used by the underlying source control to represent this build.",
            android.os.Build.VERSION.INCREMENTAL))

        list.add(DeviceInfo(
            "Release",
            "The user-visible version string.",
            android.os.Build.VERSION.RELEASE))

        list.add(DeviceInfo(
            "SDK",
            "The user-visible SDK version of the framework in its raw String representation.",
            android.os.Build.VERSION.SDK_INT.toString()))

        list.add(DeviceInfo(
            "Security Patch",
            "The user-visible security patch level.",
            android.os.Build.VERSION.SECURITY_PATCH))

        list.add(DeviceInfo(
            "Radio Version",
            "Returns the version string for the radio firmware.",
            android.os.Build.getRadioVersion()))

        list.add(DeviceInfo(
            "Board",
            "The name of the underlying board, like \"goldfish\".",
            android.os.Build.BOARD))

        list.add(DeviceInfo(
            "Bootloader",
            "The system bootloader version number.",
            android.os.Build.BOOTLOADER))

        list.add(DeviceInfo(
            "Bootloader",
            "The consumer-visible brand with which the product/hardware will be associated, if any.",
            android.os.Build.BRAND))

        list.add(DeviceInfo(
            "Brand",
            "The name of the industrial design.",
            android.os.Build.DEVICE))

        list.add(DeviceInfo(
            "Display",
            "The name of the industrial design.",
            android.os.Build.DISPLAY))

        list.add(DeviceInfo(
            "Fingerprint",
            "A string that uniquely identifies this build.",
            android.os.Build.FINGERPRINT))

        list.add(DeviceInfo(
            "Hardware",
            "The name of the hardware (from the kernel command line or /proc).",
            android.os.Build.HARDWARE))

        list.add(DeviceInfo(
            "Host",
            "Host",
            android.os.Build.HOST))

        list.add(DeviceInfo(
            "ID",
            "Either a changelist number, or a label like \"M4-rc20\".",
            android.os.Build.ID))

        list.add(DeviceInfo(
            "Manufacturer",
            "The manufacturer of the product/hardware.",
            android.os.Build.MANUFACTURER))

        list.add(DeviceInfo(
            "Model",
            "The end-user-visible name for the end product.",
            android.os.Build.MODEL))

        list.add(DeviceInfo(
            "Product",
            "The name of the overall product.",
            android.os.Build.PRODUCT))

        list.add(DeviceInfo(
            "Supported 32 bit ABIs",
            "An ordered list of 32 bit ABIs supported by this device.",
            android.os.Build.SUPPORTED_32_BIT_ABIS.joinToString()))

        list.add(DeviceInfo(
            "Supported 64 bit ABIs",
            "An ordered list of 64 bit ABIs supported by this device.",
            android.os.Build.SUPPORTED_64_BIT_ABIS.joinToString()
        ))

        list.add(DeviceInfo(
            "Tags",
            "Comma-separated tags describing the build, like unsigned,debug.",
            android.os.Build.TAGS))

        list.add(DeviceInfo(
            "Type",
            "The type of build.",
            android.os.Build.TYPE))

        return list
    }
}