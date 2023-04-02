package io.github.opendglab.core

import kotlin.js.JsName

// 内部结构
// 大端序！
object DGLabStruct {
    // 电池
    data class BatteryLevel(val battery: ByteArray) {
        @JsName("getLevel")
        fun getLevel() = battery[0].toInt()
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as BatteryLevel

            if (!battery.contentEquals(other.battery)) return false

            return true
        }
        override fun hashCode(): Int {
            return battery.contentHashCode()
        }
    }
    // 强度
    data class Power(val power: ByteArray) {


        constructor(a: Int, b: Int) : this(power = ByteArray(3)) {
            if (a < 0 || a > 2047) throw Exceptions.DataOverflowException()
            if (b < 0 || b > 2047) throw Exceptions.DataOverflowException()
            val i = 0 or ((b and 0x7FF) shl 11) or (a and 0x7FF)
            power[0] = (i and 0xFF).toByte()
            power[1] = ((i shr 8) and 0xFF).toByte()
            power[2] = ((i shr 16) and 0xFF).toByte()
        }

        var a: Int
            get() = power.toInt888() and 0x7FF
            set(value) {
                if (value < 0 || value > 2047) throw Exceptions.DataOverflowException()
                val i = 0 or ((b and 0x7FF) shl 11) or (value and 0x7FF)
                power[0] = (i and 0xFF).toByte()
                power[1] = ((i shr 8) and 0xFF).toByte()
                power[2] = ((i shr 16) and 0xFF).toByte()
            }

        var b: Int
            get() = (power.toInt888() shr 11) and 0x7FF
            set(value) {
                if (value < 0 || value > 2047) throw Exceptions.DataOverflowException()
                val i = 0 or ((value and 0x7FF) shl 11) or (a and 0x7FF)
                power[0] = (i and 0xFF).toByte()
                power[1] = ((i shr 8) and 0xFF).toByte()
                power[2] = ((i shr 16) and 0xFF).toByte()
            }

        fun getAB() = Pair(a, b)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as Power

            if (!power.contentEquals(other.power)) return false

            return true
        }

        override fun hashCode(): Int {
            return power.contentHashCode()
        }
    }

    // 波形数据
    data class WaveData(val wave: ByteArray) {
        constructor(x: Int, y: Int, z: Int) : this(wave = ByteArray(3)) {
            if (x < 0 || x > 31) throw Exceptions.DataOverflowException()
            if (y < 0 || y > 1023) throw Exceptions.DataOverflowException()
            if (z < 0 || z > 31) throw Exceptions.DataOverflowException()
            val i = 0 or ((z and 0x1F) shl 15) or ((y and 0x3FF) shl 5) or (x and 0x1F)
            wave[0] = (i and 0xFF).toByte()
            wave[1] = ((i shr 8) and 0xFF).toByte()
            wave[2] = ((i shr 16) and 0xFF).toByte()
        }
        var x: Int
            get() = wave.toInt888() and 0x1F
            set(value) {
                if (value < 0 || value > 31) throw Exceptions.DataOverflowException()
                val i = 0 or ((z and 0x1F) shl 15) or ((y and 0x3FF) shl 5) or (value and 0x1F)
                wave[0] = (i and 0xFF).toByte()
                wave[1] = ((i shr 8) and 0xFF).toByte()
                wave[2] = ((i shr 16) and 0xFF).toByte()
            }
        var y: Int
            get() = (wave.toInt888() shr 5) and 0x3FF
            set(value) {
                if (value < 0 || value > 1023) throw Exceptions.DataOverflowException()
                val i = 0 or ((z and 0x1F) shl 15) or ((value and 0x3FF) shl 5) or (x and 0x1F)
                wave[0] = (i and 0xFF).toByte()
                wave[1] = ((i shr 8) and 0xFF).toByte()
                wave[2] = ((i shr 16) and 0xFF).toByte()
            }
        var z: Int
            get() = (wave.toInt888() shr 15) and 0x1F
            set(value) {
                if (value < 0 || value > 31) throw Exceptions.DataOverflowException()
                val i = 0 or ((value and 0x1F) shl 15) or ((y and 0x3FF) shl 5) or (x and 0x1F)
                wave[0] = (i and 0xFF).toByte()
                wave[1] = ((i shr 8) and 0xFF).toByte()
                wave[2] = ((i shr 16) and 0xFF).toByte()
            }

        @JsName("getXYZ")
        fun getXYZ() = Triple(x, y, z)
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as WaveData

            if (!wave.contentEquals(other.wave)) return false

            return true
        }

        override fun hashCode(): Int {
            return wave.contentHashCode()
        }
    }

    fun ByteArray.toInt888(): Int {
        return 0 or ((this[2].toInt() and 0xFF) shl 16) or ((this[1].toInt() and 0xFF) shl 8) or (this[0].toInt() and 0xFF)
    }
}