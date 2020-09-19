# OpenDGLab
OpenDGLab 是 DG-Lab 电刺激设备的协议组装库。通过 Kotlin Multiplatform 语言得以将原始的、基于 Java/Android 的协议组装代码迁移至纯 Kotlin 实现，以进行跨平台使用。

目前 OpenDGLab 支持所有 Kotlin MPP 所支持的编译目标（除了 WASM）。

## 编译
由于使用了 Gradle 编译控制脚本，编译各种版本库成为非常简单的事情，仅需要在安装有 JDK 1.8+ 的计算机上运行 `./gradlew assemble` 即可完成编译。

默认编译目标包含 `jvm` `js` `native` 三种类型，其中 `jvm` 类型将会生成 jar 包，`native` 类型将会生成当前平台的动态链接库格式和对应的 C 头文件。

若要交叉编译其他目标类型，参见 Kotlin Multiplatform 编译配置。

## API
本库仅提供对 DG-Lab 协议的组装，不提供对应的 BLE 发送和接收功能。
> 注：所有静态方法均在对应类中的 Companion 对象中

### KDataUtils
KDataUtils 由原始 DataUtils 库中的部分运算代码移植而来。它包含主要的协议运算代码。KDataUtils 中的函数是私有的，无需您关注。

### OpenDGLab
OpenDGLab 是主要的功能实现库。实例化 OpenDGLab 类后，可以通过内部的 `constants` `device` `deviceStatus` `eStimStatus` 实例访问对应的数据。

#### Constants
Constants 中维护着当前实例的一些参数，大部分参数是直接移植自原始 DataCenter 类中使用过的对应的变量。

`getMaxPower() : Int` 方法将会返回当前电刺激设备的最大可用强度。目前协议上似乎可以超出最大值，但可能造成未知问题甚至损坏您的设备。

`setUserObject(Any)` 方法可以将任意用户对象保存在当前对象中。

`getUserObject() : Any?` 方法会返回 `setUserObject(Any)` 设置的用户对象。

#### Device
Device 中保存有一些搜索设备和创建连接所需要的数据。

`getName() : String` 返回设备名

`services() : Array(String)` 返回需要的 BLE Service UUID

#### DeviceStatus
DeviceStatus 中含有解析 DG-Lab 设备信息的方法。

获取 DeviceStatus 对应的 BLE Service UUID 使用**静态**方法 `getUUID()` 

DeviceStatus 会提供 `dfu` 和 `electric` 实例。

##### DFU
DFU 用于对 DG-Lab 使用的 nrf52832 蓝牙芯片进行 OTA 升级，若要进行升级，请获得升级包，使用 nRF 的 DFU 升级工具进行。

本类和实例没有任何作用。

##### Electric
Electric 用于解析当前 DG-Lab 设备的剩余电量。

获取 Electric 对应的 BLE Characteristic UUID 使用**静态**方法 `getUUID()` 

`read(ByteArray) : Int` 和 `onChange(ByteArray) : Int` 输入值为对当前类控制的 BLE Characteristic 进行读或通知监听操作后获得的字节数组，返回值为电量，取值为 0-100，单位 %。

#### EStimStatus
EStimStatus 用于解析 DG-Lab 电刺激状态。

获取 EStimStatus 对应的 BLE Service UUID 使用**静态**方法 `getUUID()` 

EStimStatus 会提供 `setup` `abPower` 和 `wave` 实例。

##### Setup
Setup 用于获取一些设备固件报告的默认值。

获取 Setup 对应的 BLE Characteristic UUID 使用**静态**方法 `getUUID()` 

`read(ByteArray)` 输入值为对当前类控制的 BLE Characteristic 进行读操作后返回的字节数组。调用完成后即可进行下一步操作。

##### ABPower
ABPower 用于控制当前电刺激强度。

获取 ABPower 对应的 BLE Characteristic UUID 使用**静态**方法 `getUUID()` 

`getAPower() : Int` 返回 A 通道强度

`getBPower() : Int` 返回 B 通道强度

`setABPower(a: Int, b: Int) : WriteBLE` 设置 A B 通道强度值。返回类型为 WriteBLE 结构，详见附录。

`onChange(ByteArray)` 输入值为对当前类控制的 BLE Characteristic 进行通知监听操作后返回的字节数组。调用完成后即可使用 `getAPower()` `getBPower()` 获取当前强度值。

##### Wave
Wave 用于处理波形。

获取 A 通道对应的 BLE Characteristic UUID 使用**静态**方法 `getUUIDA()` 

获取 B 通道对应的 BLE Characteristic UUID 使用**静态**方法 `getUUIDB()` 

`setWave(ByteArray, WaveChannel) : WriteBLE` 第一个输入值为当前选中波形的运算数组，详见下方 WaveCenter，第二个输入值为要操作的通道，A 通道使用 `Wave.WaveChannel.CHANNEL_A`，B 通道使用 `Wave.WaveChannel.CHANNEL_B`。返回值为 WriteBLE 详见附录。

`getWaveCenterA(): WaveCenter` 获取 A 通道的波形管理器。 

`getWaveCenterB(): WaveCenter` 获取 B 通道的波形管理器。 

### WaveCenter
WaveCenter 用于管理波形。

WaveCenter 拥有下列**静态**方法。

`getBasicWave(String) : BasicWave` 按名称获取一个默认的基础波形。（即 APP 中控制页显示的默认 16 个波形）

`getTouchWave(String) : BasicWave` 按名称获取一个默认的触控波形。（即 APP 中触摸控制页下方的 8 个波形）

`getRandomBasicWave() : WaveDetail` 获取一个随机的基础波形。WaveDetail 详见附录。

`getRandomTouchWave() : WaveDetail` 获取一个随机的触控波形。WaveDetail 详见附录。

`getRandomWave() : WaveDetail` 获取一个随机的波形。WaveDetail 详见附录。

`getBasicWaveList() : Array<String>` 获取基础波形名称列表。

`getTouchWaveList() : Array<String>` 获取触控波形名称列表。

`stop() : ByteArray` 获取关闭波形的数组。

WaveCenter 含有以下实例对象。

`touchWave : BasicWave` 触控控制对象，用于使用 `inputTouch` 方法。等于 APP 中的触摸功能。

WaveCenter 含有以下实例方法。

`resetWave()` 清空所有波形时钟。

`inputTouch(Double, Double)` 设置 touchWave 实例中的值，取值范围为 0.0-1.0，等于 APP 中的触摸功能。

`selectWave(BasicWave?)` 输入值可以为任意的 BasicWave 设置波形模式，null 时清除波形。

`waveTick() : ByteArray?` 返回当前选择的波形所创建的字节数组。当没有选择波形时返回 null，此函数需要每 100 毫秒运行一次并传给 Wave 中的 setWave 方法。

## 附录
### WriteBLE
WriteBLE 是一个数据结构，定义为

```
WriteBLE(uuid: String, data: ByteArray)
```

`uuid` 为要写入的 BLE Characteristic UUID。`data` 为要发送的字节数组。
### WaveDetail
WaveDetail 是一个数据结构，定义为

```
WaveDetail(name: String, wave: BasicWave)
```

`name` 为随机出的波形名。`wave` 为此波形的 BasicWave 对象。