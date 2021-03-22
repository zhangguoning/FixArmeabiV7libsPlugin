# FixArmeabiV7libsPlugin
A plugin to Fix lack armeabi-v7 so files when the ABI upgrade to armeabi-v7(the armeabi so file exist, but the armeabi-v7 so file not exist).

这个插件用于修复将ABI由 armeabi 升级至 armeabi-v7a 时，由于部分类库只提供了 armeabi 下的 ‘.so’ 文件,而没有提供 armeabi-v7a 下的‘.so’文件 而导致的错误。<br><br>详细分析过程可以参考：https://www.jianshu.com/p/5e8caa25629d
