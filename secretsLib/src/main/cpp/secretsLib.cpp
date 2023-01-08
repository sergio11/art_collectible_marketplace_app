#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_dreamsoftware_artcollectibles_secretsLib_SecretsVaultAPI_getMasterPassword(
        JNIEnv* env,
        jobject /* this */) {
    std::string masterPassword = "PS%#6nx69y5K*gb3dG5T62GJGbMcMIWJQ9q@*nYu";
    return env->NewStringUTF(masterPassword.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_dreamsoftware_artcollectibles_secretsLib_SecretsVaultAPI_getMasterSalt(
        JNIEnv* env,
jobject /* this */) {
std::string masterSalt = "x72oY6q2#p2K%svYwhhf";
return env->NewStringUTF(masterSalt.c_str());
}