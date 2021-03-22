package cn.ning.android.gradle.plugin

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project


/**
 * @author ning <zgning0819@gmail.com>
 * @date 2019/10/28
 * @description: 修正 armeabi-v7a 缺失问题 (只有 armeabi 的 so ,没有 armeabi-v7a 的 so)
 */
class FixArmeabiV7libsPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        project.afterEvaluate {

            project.tasks.findAll {
                task ->
                    if (task.path.startsWith(":app:transformNativeLibsWithStripDebugSymbolFor")) {
                        task.doLast {
                            println("=================== fix-v7libs start ===================")
                            if(!task?.outputs?.files?.files?.isEmpty()){
                                task.outputs.files.files.each {
                                    File armeabiDirPath = searchForDirectory("armeabi", it)
                                    if (armeabiDirPath != null) {
                                        File armeabiv7DirPath = searchForDirectory("armeabi-v7a", it)
                                        if (armeabiv7DirPath == null) {
                                            armeabiv7DirPath = new File(armeabiDirPath.getParent() + File.separator + "armeabi-v7a" + File.separator)
                                        }
                                        if (!armeabiv7DirPath.isDirectory()) {
                                            throw new GradleException("JNILibs File Search Error !")
                                        }

                                        armeabiDirPath.listFiles().each {
                                            armeabiFile ->
                                                if (!(armeabiFile as File).exists() || armeabiFile.isDirectory()) {
                                                    throw new GradleException("JNILibs File Error !")
                                                }
                                                boolean skip = armeabiv7DirPath.list().contains(armeabiFile.name)
                                                if (!skip) {
                                                    File targetFile = new File(armeabiv7DirPath, (armeabiFile as File).name)

                                                    targetFile.withOutputStream { os ->
                                                        (armeabiFile as File).withInputStream {
                                                            is ->
                                                                os << is
                                                        }
                                                    }
                                                    println("COPY: from  ${(armeabiFile as File).absolutePath} \n\t to ${targetFile.absolutePath}")
                                                } else {
                                                    println("SKIP:${(armeabiFile as File).name}")
                                                }

                                        }
                                    }
                                }
                            }
                            println("=================== fix-v7libs finish ===================")
                        }
                    }
            }
        }
    }

    File searchForDirectory(String name, File currentDir) {
        for (file in currentDir.listFiles()) {
            if (file.isDirectory() && file.name == name) {
                return file
            }

            if (file.isDirectory()) {
                File result = searchForDirectory(name, file)
                if (result != null) {
                    return result
                }
            }
        }
        return null
    }
}

