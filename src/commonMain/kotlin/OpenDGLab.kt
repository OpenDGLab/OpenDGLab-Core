import KDataUtils.parseSetupData
import kotlin.js.JsName
import kotlin.jvm.JvmStatic

class OpenDGLab {
    public val constants = Constants()
    public val device = Device()
    public val deviceStatus = DeviceStatus(this)
    public val eStimStatus = EStimStatus(this)
    data class WriteBLE(val uuid: String, val data: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as WriteBLE

            if (uuid != other.uuid) return false
            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = uuid.hashCode()
            result = 31 * result + data.contentHashCode()
            return result
        }
    }

    class Constants {
        //两者相除等于强度最大值
        var powerDivideA = 2000
        var powerDivideB = 7
        @JsName("getMaxPower")
        public fun getMaxPower() = powerDivideA / powerDivideB
        //不需要处理 DFU 相关内容
        var f990y = 0 //似乎和 DFU 有关
        var f964B = "" //似乎和 DFU 有关
        var f991z = 0 //似乎和 DFU 有关
        var f963A = 0 //似乎和 DFU 有关
        //版本
        var f965C = 0
        var f966D = 0
        //用于保存用户的对象
        private var userObject : Any? = null
        @JsName("setUserObject")
        public fun setUserObject(obj: Any?) {
            this.userObject = obj
        }
        @JsName("getUserObject")
        public fun getUserObject() = userObject
    }
    class Device {
        companion object {
            public const val name = "D-LAB ESTIM01"
            @JsName("getName")
            public fun getName() = name
            @JsName("isDGLab")
            public fun isDGLab(deviceName: String) = deviceName == name
            @JsName("services")
            public fun services() = arrayOf(DeviceStatus.getUUID(), EStimStatus.getUUID())
        }
    }
    class DeviceStatus(lab: OpenDGLab) { //基础信息
        companion object {
            private const val uuid = "955a180a-0fe2-f5aa-a094-84b8d4f3e8ad"
            @JvmStatic
            @JsName("getUUID")
            public fun getUUID() = uuid
        }
        public val dfu = DFU(lab)
        public val electric = Electric(lab)
        class DFU(private val lab: OpenDGLab) { //DFU 固件更新用 没什么调用的价值
            companion object {
                private const val uuid = "955a1501-0fe2-f5aa-a094-84b8d4f3e8ad"
                @JvmStatic
                @JsName("getUUID")
                public fun getUUID() = uuid
            }
            @JsName("read")
            public fun read(bArr: ByteArray) {
                val c: IntArray = KDataUtils.dfu(bArr)
                lab.constants.f965C = c[0]
                lab.constants.f966D = c[1]
                lab.constants.f990y = 0
                lab.constants.f964B = ""
                lab.constants.f991z = 0
                lab.constants.f963A = 0
                //似乎是固件版本号 没什么用
            }
        }
        class Electric(private val lab: OpenDGLab) { //电量
            companion object {
                private const val uuid = "955a1500-0fe2-f5aa-a094-84b8d4f3e8ad"
                @JvmStatic
                @JsName("getUUID")
                public fun getUUID() = uuid
            }
            @JsName("onChange")
            public fun onChange(bArr: ByteArray) : Int {
                return KDataUtils.getElectric(bArr)
            }
            @JsName("read")
            public fun read(bArr: ByteArray) : Int {
                return KDataUtils.getElectric(bArr)
            }
        }
    }
    class EStimStatus(lab: OpenDGLab) {
        companion object {
            private const val uuid = "955a180b-0fe2-f5aa-a094-84b8d4f3e8ad"
            @JvmStatic
            @JsName("getUUID")
            public fun getUUID() = uuid
        }
        public val setup = Setup(lab)
        public val abPower = ABPower(lab)
        public val wave = Wave(lab)
        class Setup(private val lab: OpenDGLab) { //获取一些基础值
            companion object {
                private const val uuid = "955a1507-0fe2-f5aa-a094-84b8d4f3e8ad"
                @JvmStatic
                @JsName("getUUID")
                public fun getUUID() = uuid
            }
            @JsName("read")
            public fun read(bArr: ByteArray) {
                val a = parseSetupData(bArr)
                lab.constants.powerDivideA = a[0]
                lab.constants.powerDivideB = a[1]
            }
        }
        class ABPower(private val lab: OpenDGLab) { //强度值
            companion object {
                private const val uuid = "955a1504-0fe2-f5aa-a094-84b8d4f3e8ad"
                @JvmStatic
                @JsName("getUUID")
                public fun getUUID() = uuid
            }
            private var aPower = 0
            private var bPower = 0
            public fun getAPower() = aPower
            public fun getBPower() = bPower
            @JsName("onChange")
            public fun onChange(bArr: ByteArray) { //当强度变化时调用
                val abpower = KDataUtils.getABPower(bArr, lab.constants.powerDivideB)
                aPower = abpower[1]
                bPower = abpower[0]
            }
            @JsName("setABPower")
            public fun setABPower(a: Int, b: Int) : WriteBLE { //修改强度值 0-getMaxPower() 不知道能不能超出
                return WriteBLE(
                        uuid = uuid, data = KDataUtils.changePower(
                            b * lab.constants.powerDivideB,
                            a * lab.constants.powerDivideB
                        )
                    )
            }
        }
        class Wave(private val lab: OpenDGLab) { //波形
            companion object {
                private const val uuid_A = "955a1505-0fe2-f5aa-a094-84b8d4f3e8ad"
                private const val uuid_B = "955a1506-0fe2-f5aa-a094-84b8d4f3e8ad"
                @JvmStatic
                @JsName("getUUIDA")
                public fun getUUIDA() = uuid_A
                @JvmStatic
                @JsName("getUUIDB")
                public fun getUUIDB() = uuid_B
            }
            private var waveCenterA = WaveCenter()
            private var waveCenterB = WaveCenter()
            enum class WaveChannel(private val channel: Int) {
                CHANNEL_A(1), CHANNEL_B(2);
                operator fun invoke() = channel
            }
            @JsName("setWave")
            public fun setWave(wave: ByteArray, channel: WaveChannel) : WriteBLE {
                return when (channel) {
                    WaveChannel.CHANNEL_A -> {
                        WriteBLE(uuid = uuid_A, data = wave)
                    }
                    WaveChannel.CHANNEL_B -> {
                        WriteBLE(uuid = uuid_B, data = wave)
                    }
                }
            }
            @JsName("getWaveCenterA")
            fun getWaveCenterA() = waveCenterA
            @JsName("getWaveCenterB")
            fun getWaveCenterB() = waveCenterB
        }
    }
}