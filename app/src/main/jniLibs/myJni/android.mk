LOCAL_PATH := $(call my-dir)
    include $(CLEAR_VARS)

    LOCAL_MODULE := hello
    LOCAL_SRC_FILES := myJni.c

    include $(BUILD_SHARED_LIBRARY)