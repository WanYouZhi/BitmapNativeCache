#include <jni.h>
#include <android/log.h>
#include <map>

using namespace std;
#define TAG    "bitmap-cache" // 这个是自定义的LOG的标识
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)

class Cache {
private:
    char *bytes;
    unsigned int byteLength;
public:
    void setBytes(char *bytes) {
        this->bytes = bytes;
    }

    void setByteLength(unsigned int byteLength) {
        this->byteLength = byteLength;
    }

    char *getBytes() {
        return bytes;
    }

    unsigned int getByteLength() {
        return byteLength;
    }
};

map<int64_t, Cache> caches;

char *jbyteArrayToChar(JNIEnv *env, jbyteArray bytes, jint len) {
    char *rtn = nullptr;
    jbyte *ba = env->GetByteArrayElements(bytes, nullptr);
    if (len > 0) {
        rtn = (char *) malloc(static_cast<size_t>(len));
        memcpy(rtn, ba, static_cast<size_t>(len));
    }
    env->ReleaseByteArrayElements(bytes, ba, 0);
    return rtn;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wan_natives_Cache_addCache(JNIEnv *env, jclass type, jlong id,
                                    jbyteArray bytes_) {
    jint len = env->GetArrayLength(bytes_);
    char *bytes = jbyteArrayToChar(env, bytes_, len);
    Cache cache = Cache();
    cache.setBytes(bytes);
    cache.setByteLength(static_cast<unsigned int>(len));
    caches[id] = cache;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_wan_natives_Cache_getCache(JNIEnv *env, jclass type, jlong id) {
    jbyteArray data = nullptr;
    auto ir = caches.find(id);
    if (ir != caches.end()) {
        Cache cache = ir->second;
        char *bytes = cache.getBytes();
        jint size = cache.getByteLength();
        data = env->NewByteArray(size);
        env->SetByteArrayRegion(data, 0, size, (jbyte *) bytes);
    }
    return data;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wan_natives_Cache_removeCache(JNIEnv *env, jclass type, jlong id) {
    auto ir = caches.find(id);
    if (ir != caches.end()) {
        Cache cache = ir->second;
        free(cache.getBytes());
        cache.setBytes(nullptr);
        caches.erase(id);
    }
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wan_natives_Cache_cacheSize(JNIEnv *env, jclass type) {
    return caches.size();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_wan_natives_Cache_clearCache(JNIEnv *env, jclass type) {
    map<int64_t, Cache>::iterator ir;
    for (ir = caches.begin(); ir != caches.end(); ir++) {
        Cache cache = ir->second;
        free(cache.getBytes());
        cache.setBytes(nullptr);
    }
    caches.clear();
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_wan_natives_Cache_getCacheLength(JNIEnv *env, jclass type, jlong id) {
    auto ir = caches.find(id);
    return ir != caches.end() ? ir->second.getByteLength() : 0;
}