#include <jni.h>
#include <string>

#include <android/asset_manager_jni.h>
#include <android/log.h>

#define LOG_TAG  "android_jni"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#include "ncnn/net.h"
#include "YoloV5.h"

extern "C" JNIEXPORT void JNICALL
Java_com_example_yolov5application_Native_init(JNIEnv *env, jclass, jobject assetManager) {
    LOGI("nativeInit");

//    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);
//
//    ncnn::Net * Yolov5Net = new ncnn::Net();
//    Yolov5Net->load_param(mgr, "yolov5ssim.param");
//    Yolov5Net->load_model(mgr, "yolov5ssim.bin");
//
//
//    ncnn::Extractor ex = Yolov5Net->create_extractor();
//
//    ncnn::Mat in(640, 640, 3);
//    ex.input(0, in);
//
//    ncnn::Mat blog740;
//    ncnn::Mat blog761;
//    ncnn::Mat blogoutput;
//
//
//    ex.extract("740", blog740);
//    ex.extract("761", blog761);
//    ex.extract("output", blogoutput);

    if (YoloV5::detector == nullptr) {
        AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);
//        YoloV5::detector = new YoloV5(mgr, "yolov5ssim.param", "yolov5ssim.bin", true);
        YoloV5::detector = new YoloV5(mgr, "yolov5s320sim.param", "yolov5s320sim.bin", true);
//        YoloV5::detector = new YoloV5(mgr, "yolov5s.param", "yolov5s.bin", true);

    }



}


extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_example_yolov5application_Native_detect(JNIEnv *env, jclass, jobject image, jdouble threshold, jdouble nms_threshold) {
    auto result = YoloV5::detector->detect(env, image, threshold, nms_threshold);

    auto box_cls = env->FindClass("com/example/yolov5application/Box");
    auto cid = env->GetMethodID(box_cls, "<init>", "(FFFFIF)V");
    jobjectArray ret = env->NewObjectArray(result.size(), box_cls, nullptr);
    int i = 0;
    for (auto &box:result) {
        env->PushLocalFrame(1);
        jobject obj = env->NewObject(box_cls, cid, box.x1, box.y1, box.x2, box.y2, box.label, box.score);
        obj = env->PopLocalFrame(obj);
        env->SetObjectArrayElement(ret, i++, obj);
    }
    return ret;
}