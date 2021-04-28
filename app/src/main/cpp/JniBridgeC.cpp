/**
 * Copyright(c) Live2D Inc. All rights reserved.
 *
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

#include <jni.h>
#include "JniBridgeC.hpp"
#include "LAppDelegate.hpp"
#include "LAppPal.hpp"

using namespace Csm;

static JavaVM *g_JVM; // JavaVM is valid for all threads, so just save it globally
static jclass g_JniBridgeJavaClass;
static jmethodID g_LoadFileMethodId;
static jmethodID g_LoadFileByDiskMethodId;
static jmethodID g_MoveTaskToBackMethodId;

JNIEnv *GetEnv() {
    JNIEnv *env = NULL;
    g_JVM->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);
    return env;
}

// The VM calls JNI_OnLoad when the native library is loaded
jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    g_JVM = vm;

    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    jclass clazz = env->FindClass("com/yunhualian/JniBridgeJava");
    g_JniBridgeJavaClass = reinterpret_cast<jclass>(env->NewGlobalRef(clazz));
    g_LoadFileMethodId = env->GetStaticMethodID(g_JniBridgeJavaClass, "LoadFile",
                                                "(Ljava/lang/String;)[B");
    g_LoadFileByDiskMethodId = env->GetStaticMethodID(g_JniBridgeJavaClass, "LoadFileFromDisk",
                                                      "(Ljava/lang/String;)[B");
    g_MoveTaskToBackMethodId = env->GetStaticMethodID(g_JniBridgeJavaClass, "MoveTaskToBack",
                                                      "()V");

    return JNI_VERSION_1_6;
}

void JNICALL JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv *env = GetEnv();
    env->DeleteGlobalRef(g_JniBridgeJavaClass);
}

char *JniBridgeC::LoadFileAsBytesFromJava(const char *filePath, unsigned int *outSize) {
    JNIEnv *env = GetEnv();

    // ファイルロード
    jbyteArray obj = (jbyteArray) env->CallStaticObjectMethod(g_JniBridgeJavaClass,
                                                              g_LoadFileMethodId,
                                                              env->NewStringUTF(filePath));
    *outSize = static_cast<unsigned int>(env->GetArrayLength(obj));

    char *buffer = new char[*outSize];
    env->GetByteArrayRegion(obj, 0, *outSize, reinterpret_cast<jbyte *>(buffer));

    return buffer;
}

char *JniBridgeC::LoadFilebyDiskFromJava(const char *filePath, unsigned int *outSize) {
    JNIEnv *env = GetEnv();

    // ファイルロード
    jbyteArray obj = (jbyteArray) env->CallStaticObjectMethod(g_JniBridgeJavaClass,
                                                              g_LoadFileByDiskMethodId,
                                                              env->NewStringUTF(filePath));
    *outSize = static_cast<unsigned int>(env->GetArrayLength(obj));

    char *buffer = new char[*outSize];
    env->GetByteArrayRegion(obj, 0, *outSize, reinterpret_cast<jbyte *>(buffer));

    return buffer;
}

void JniBridgeC::MoveTaskToBack() {
    JNIEnv *env = GetEnv();

    // アプリ終了
    env->CallStaticVoidMethod(g_JniBridgeJavaClass, g_MoveTaskToBackMethodId, NULL);
}

std::string JniBridgeC::JavaStringToString(JNIEnv *env, jstring str) {
    if (env == nullptr || str == nullptr) {
        return "";
    }
    const jchar *chars = env->GetStringChars(str, NULL);
    if (chars == nullptr) {
        return "";
    }
    std::string u8_string = UTF16StringToUTF8String(
            reinterpret_cast<const char16_t *>(chars), env->GetStringLength(str));
    env->ReleaseStringChars(str, chars);
    return u8_string;
}

std::string JniBridgeC::UTF16StringToUTF8String(const char16_t *chars, size_t len) {
    std::u16string u16_string(chars, len);
    return std::wstring_convert<std::codecvt_utf8_utf16<char16_t>, char16_t>{}
            .to_bytes(u16_string);
}

extern "C"
{
JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnStart(JNIEnv *env, jclass type) {
    LAppDelegate::GetInstance()->OnStart();
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnPause(JNIEnv *env, jclass type) {
    LAppDelegate::GetInstance()->OnPause();
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnStop(JNIEnv *env, jclass type) {
    LAppDelegate::GetInstance()->OnStop();
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnDestroy(JNIEnv *env, jclass type) {
    LAppDelegate::GetInstance()->OnDestroy();
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnSurfaceCreated(JNIEnv *env, jclass type) {
    LAppDelegate::GetInstance()->OnSurfaceCreate();
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnSurfaceChanged(JNIEnv *env, jclass type, jint width,
                                                         jint height) {
    LAppDelegate::GetInstance()->OnSurfaceChanged(width, height);
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnDrawFrame(JNIEnv *env, jclass type) {
    LAppDelegate::GetInstance()->Run();
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnTouchesBegan(JNIEnv *env, jclass type, jfloat pointX,
                                                       jfloat pointY) {
    LAppDelegate::GetInstance()->OnTouchBegan(pointX, pointY);
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnTouchesEnded(JNIEnv *env, jclass type, jfloat pointX,
                                                       jfloat pointY) {
    LAppDelegate::GetInstance()->OnTouchEnded(pointX, pointY);
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_nativeOnTouchesMoved(JNIEnv *env, jclass type, jfloat pointX,
                                                       jfloat pointY) {
    LAppDelegate::GetInstance()->OnTouchMoved(pointX, pointY);
}

JNIEXPORT void JNICALL
Java_com_yunhualian_JniBridgeJava_loadModelFile(JNIEnv *env, jclass type, jstring modelPath,
                                                jstring jsonModelName) {
    std::string tempModelPath = JniBridgeC::JavaStringToString(env, modelPath);
    std::string tempJsonModelName = JniBridgeC::JavaStringToString(env, jsonModelName);
    LAppDelegate::GetInstance()->LoadModelFile(tempModelPath, tempJsonModelName);
}
}

