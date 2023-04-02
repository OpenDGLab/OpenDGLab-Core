# OpenDGLab v2
OpenDGLab v2 是 DG-Lab 电刺激设备的协议组装库第二代开源版。与第一版不同在于，第二版根据[官方文档](https://github.com/DG-LAB-OPENSOURCE/DG-LAB-OPENSOURCE/blob/main/README.md) 完全重写了协议组装机制。并且重写了波形计算代码。

V2 版不再包含原有通过 Android 客户端解析的协议代码转制，同时代码和调用进行了简化。

目前 OpenDGLab 支持所有 Kotlin MPP 所支持的编译目标。

## 编译
由于使用了 Gradle 编译控制脚本，编译各种版本库成为非常简单的事情，仅需要在安装有 JDK 1.8+ 的计算机上运行 `./gradlew assemble` 即可完成编译。

默认编译目标包含 `jvm` `js` `native` 三种类型，其中 `jvm` 类型将会生成 jar 包，`native` 类型将会生成当前平台的动态链接库格式和对应的 C 头文件。

若要交叉编译其他目标类型，参见 Kotlin Multiplatform 编译配置。

默认编译 JS 为 CommonJS 无法在浏览器中使用，若要在浏览器中使用，请打包为 UMD 模式。

## API
本库仅提供对 DG-Lab 协议的组装，不提供对应的 BLE 发送和接收功能。
> 注：所有静态方法均在对应类中的 Companion 对象中
### OpenDGLab
波形相关计算，在 OpenDGLab Core v2 中，所有波形相关内容均已进行重写，不再使用原版移植。并且根据原版原有波形算法进行了拓展，现在可以支持大于原版 3 小节限制的任意数量小节。

#### calcXYZ
官方说明中用于计算频率和 XY 数据的算法。

#### calcTouchWave
原版中触摸的算法，`calcTouchWave(x: Double, y: Double)` 为原版触控板的实现。`calcTouchWave(timeSeq: Int, wave: Array<Waves.TouchWaveData>)` 为原版触控板下方预制波形的实现。

#### calcAutoWave
原版默认 16 个波形和自定义波形的计算。

#### calcWavePlot
原版波形图的实现。此处将每次返回的 state 传入即可计算波形图。

### DGLabBLEDevice
`DGLabBLEDevice` 包含对每个 DG-Lab 设备的管理实例，在使用中，为每一个 DG-Lab 设备创建一个 `DGLabBLEDevice` 实例。

#### selectAutoWave
选中 AutoWave 数据，此项选中于原版的 16 个自动波形相同，通过该方法选中波形后使用 `callAutoWaveTimer` 进行计算。

#### selectPower
修改强度，两个值的取值范围为 [0, 2047] ，原版程序中的取值为 7 倍，即当原版显示为 10 时，实际传入为 70。

#### stop 和 stopAll
停止所有波形，将强度设置为 0，将波形设置为 off。

#### callAutoWaveTimer
每 0.1 秒调用一次，用于计算 AutoWave 数据。

#### callbackBattery 和 callbackPower
回调调用，将 BLE 回调数据传入自动解析。

### Exceptions
`Exceptions` 中包含 OpenDGLab Core 中出现的异常。`DataOverflowException` 代表传入的数据已经超出了限制。

### Waves
`Waves` 中包含原版程序中的 16 个波形和 8 个触摸波形。

### DGLabStruct
`DGLabStruct` 类包含所有 DG-Lab 设备所需的波形结构，以大端序形式存储，详见官方文档。

## 附录
### 内置波形相关
内置波形通过多小节和初始值进行计算，分为小节段和休息段，在一次循环中小节段会依次触发然后经历休息段。数据中的 L 即为休息段时长。

## 关于
OpenDGLab 致力于让一切变得可能。  
这允许 D-Lab 设备与任何其他系统创建连接。无论是第三方控制还是将其连接到其他系统中，都开始变得可能。  
同时，开放的协议给予您更大的权力来控制属于您自己的设备。  
同时，OpenDGLab 也让您同时拥有多个 D-Lab 设备时得以使用更好的设备（如您的电脑）来同时控制多个 D-Lab 设备。让您的游玩过程更加流畅自如。  