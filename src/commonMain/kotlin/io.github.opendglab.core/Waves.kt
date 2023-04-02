package io.github.opendglab.core

import kotlin.math.ceil
import kotlin.math.pow

object Waves {
    data class TouchWaveData(val ax: Int, val ay: Int, val az: Int)
    object TouchWave {
        fun extrustion() =
            arrayOf(
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 20)
            )

        fun bubble() =
            arrayOf(
                TouchWaveData(3, 42, 0),
                TouchWaveData(3, 42, 20)
            )

        fun signal() =
            arrayOf(
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 10),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 10),
                TouchWaveData(1, 9, 20),
                TouchWaveData(3, 42, 0),
                TouchWaveData(2, 23, 20),
                TouchWaveData(2, 28, 20),
                TouchWaveData(3, 35, 20),
                TouchWaveData(3, 43, 20),
                TouchWaveData(3, 54, 20),
                TouchWaveData(0, 0, 0),
                TouchWaveData(0, 0, 0)
            )

        fun climb() =
            arrayOf(
                TouchWaveData(3, 53, 10),
                TouchWaveData(3, 36, 12),
                TouchWaveData(2, 26, 14),
                TouchWaveData(2, 17, 16),
                TouchWaveData(1, 13, 18),
                TouchWaveData(1, 9, 20),
            )

        fun rhythm() =
            arrayOf(
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 20)
            )

        fun keepClick() =
            arrayOf(
                TouchWaveData(5, 95, 20)
            )

        fun pulse() =
            arrayOf(
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 11, 20),
                TouchWaveData(1, 14, 20),
                TouchWaveData(2, 16, 20),
                TouchWaveData(2, 20, 20),
                TouchWaveData(2, 26, 20),
                TouchWaveData(2, 32, 20),
                TouchWaveData(3, 39, 20),
                TouchWaveData(3, 49, 20),
                TouchWaveData(4, 60, 20),
                TouchWaveData(4, 75, 20),
                TouchWaveData(4, 93, 20),
                TouchWaveData(5, 115, 20),
                TouchWaveData(6, 141, 20),
                TouchWaveData(6, 175, 20),
                TouchWaveData(7, 216, 20)
            )

        fun airWaves() =
            arrayOf(
                TouchWaveData(1, 9, 20),
                TouchWaveData(2, 15, 20),
                TouchWaveData(2, 28, 20),
                TouchWaveData(3, 49, 20),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 20),
                TouchWaveData(1, 9, 0),
                TouchWaveData(1, 9, 20),
                TouchWaveData(0, 0, 0),
                TouchWaveData(0, 0, 0)
            )
    }

    data class WaveSectionData(val section: AutoWaveSection,val localTick :Int, val waveMaxTiming:Int, val lastWaveMaxTiming: Int)
    // l - 休息时长 小节间休息时长 向上取整(L/10)
    // zy - 高低频平衡 取值 0-20 越低高频越强 越高低频更强
    data class AutoWaveData(val sections: Array<AutoWaveSection>, val l: Int, val zy: Int) {
        val waveMaxTimingSection: Array<Int> =
            this.sections.map { ceil(it.j.toDouble() / it.c.toDouble()).toInt() }.toTypedArray()

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as AutoWaveData

            if (!sections.contentEquals(other.sections)) return false
            if (l != other.l) return false
            if (zy != other.zy) return false

            return true
        }

        override fun hashCode(): Int {
            var result = sections.contentHashCode()
            result = 31 * result + l
            result = 31 * result + zy
            return result
        }
    }
    data class AutoWaveSection(val _a: Int, val _b: Int, val c: Int, val _j: Int, val pc: Int, val points: Array<AutoWavePoint>) {
        val a = _a * 20 + 1000
        val b = _b * 20 + 1000
        val j = ceil(((_j + 1).toDouble() / 101.0).pow(1.6) * 100.0).toInt()
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as AutoWaveSection

            if (_a != other._a) return false
            if (_b != other._b) return false
            if (c != other.c) return false
            if (_j != other._j) return false
            if (pc != other.pc) return false
            if (!points.contentEquals(other.points)) return false
            if (a != other.a) return false
            if (b != other.b) return false
            if (j != other.j) return false

            return true
        }

        override fun hashCode(): Int {
            var result = _a
            result = 31 * result + _b
            result = 31 * result + c
            result = 31 * result + _j
            result = 31 * result + pc
            result = 31 * result + points.contentHashCode()
            result = 31 * result + a
            result = 31 * result + b
            result = 31 * result + j
            return result
        }
    }
    data class AutoWavePoint(val x: Int, val y: Double, val anchor: Int)
    object AutoWave {
        fun off() = AutoWaveData(sections = arrayOf(AutoWaveSection(0,0,0,0,0, arrayOf(
            AutoWavePoint(0,0.0,1), AutoWavePoint(0,0.0,1)
        ))), l = 0, zy = 0)
        fun breath() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 20, 8, 0, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 4.0, 0),AutoWavePoint(2, 8.0, 0),AutoWavePoint(3, 12.0, 0),AutoWavePoint(4, 16.0, 0),AutoWavePoint(5, 20.0, 1),AutoWavePoint(6, 20.0, 1),AutoWavePoint(7, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=35, zy=8)
        fun tide() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 32, 11, 20, 2, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 3.3333333, 0),AutoWavePoint(2, 6.6666665, 0),AutoWavePoint(3, 10.0, 1),AutoWavePoint(4, 13.333333, 0),AutoWavePoint(5, 16.666666, 0),AutoWavePoint(6, 20.0, 1),AutoWavePoint(7, 18.402824, 0),AutoWavePoint(8, 16.805649, 0),AutoWavePoint(9, 15.208473, 0),AutoWavePoint(10, 13.611298, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=5, zy=8)
        fun keepClick() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 34, 8, 20, 1, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 0.0, 1),AutoWavePoint(2, 20.0, 1),AutoWavePoint(3, 13.333333, 0),AutoWavePoint(4, 6.6666665, 0),AutoWavePoint(5, 0.0, 1),AutoWavePoint(6, 0.2399439, 0),AutoWavePoint(7, 0.4798878, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=0, zy=8)
        fun quickPress() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 29, 2, 44, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=16, zy=8)
        fun stronger() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 20, 11, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 5.719211, 1),AutoWavePoint(2, 0.0, 1),AutoWavePoint(3, 10.499579, 1),AutoWavePoint(4, 0.3826058, 1),AutoWavePoint(5, 14.682558, 1),AutoWavePoint(6, 0.0, 1),AutoWavePoint(7, 17.454998, 1),AutoWavePoint(8, 0.0, 1),AutoWavePoint(9, 20.0, 1),AutoWavePoint(10, 0.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=5, zy=8)
        fun heartbeat() = AutoWaveData(sections=arrayOf(AutoWaveSection(65, 20, 2, 6, 1, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 14, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 0.0, 0),AutoWavePoint(2, 0.0, 0),AutoWavePoint(3, 0.0, 0),AutoWavePoint(4, 0.0, 1),AutoWavePoint(5, 14.972563, 1),AutoWavePoint(6, 16.648375, 0),AutoWavePoint(7, 18.324188, 0),AutoWavePoint(8, 20.0, 1),AutoWavePoint(9, 0.0, 1),AutoWavePoint(10, 0.0, 0),AutoWavePoint(11, 0.0, 0),AutoWavePoint(12, 0.0, 0),AutoWavePoint(13, 0.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=5, zy=16)
        fun compress() = AutoWaveData(sections=arrayOf(AutoWaveSection(52, 16, 11, 0, 2, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 20.0, 0),AutoWavePoint(2, 20.0, 0),AutoWavePoint(3, 20.0, 0),AutoWavePoint(4, 20.0, 0),AutoWavePoint(5, 20.0, 0),AutoWavePoint(6, 20.0, 0),AutoWavePoint(7, 20.0, 0),AutoWavePoint(8, 20.0, 0),AutoWavePoint(9, 20.0, 0),AutoWavePoint(10, 20.0, 1))),AutoWaveSection(0, 20, 10, 0, 1, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 20.0, 0),AutoWavePoint(2, 20.0, 0),AutoWavePoint(3, 20.0, 0),AutoWavePoint(4, 20.0, 0),AutoWavePoint(5, 20.0, 0),AutoWavePoint(6, 20.0, 0),AutoWavePoint(7, 20.0, 0),AutoWavePoint(8, 20.0, 0),AutoWavePoint(9, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=0, zy=16)
        fun rhythmic() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 20, 26, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 4.0, 0),AutoWavePoint(2, 8.0, 0),AutoWavePoint(3, 12.0, 0),AutoWavePoint(4, 16.0, 0),AutoWavePoint(5, 20.0, 1),AutoWavePoint(6, 0.0, 1),AutoWavePoint(7, 5.0, 0),AutoWavePoint(8, 10.0, 0),AutoWavePoint(9, 15.0, 0),AutoWavePoint(10, 20.0, 1),AutoWavePoint(11, 0.0, 1),AutoWavePoint(12, 6.6666665, 0),AutoWavePoint(13, 13.333333, 0),AutoWavePoint(14, 20.0, 1),AutoWavePoint(15, 0.0, 1),AutoWavePoint(16, 10.0, 0),AutoWavePoint(17, 20.0, 1),AutoWavePoint(18, 0.0, 1),AutoWavePoint(19, 20.0, 1),AutoWavePoint(20, 0.0, 1),AutoWavePoint(21, 20.0, 1),AutoWavePoint(22, 0.0, 1),AutoWavePoint(23, 20.0, 1),AutoWavePoint(24, 0.0, 1),AutoWavePoint(25, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=5, zy=8)
        fun grainTouch() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 38, 4, 25, 2, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 20.0, 0),AutoWavePoint(2, 20.0, 1),AutoWavePoint(3, 0.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=0, zy=8)
        fun spring() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 30, 4, 45, 2, arrayOf(AutoWavePoint(0, 0.18084228, 1),AutoWavePoint(1, 6.7872286, 0),AutoWavePoint(2, 13.393615, 0),AutoWavePoint(3, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=20, zy=16)
        fun waveRipple() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 60, 4, 53, 4, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 10.0, 0),AutoWavePoint(2, 20.0, 1),AutoWavePoint(3, 14.669602, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=5, zy=16)
        fun rainSwept() = AutoWaveData(sections=arrayOf(AutoWaveSection(4, 0, 3, 39, 1, arrayOf(AutoWavePoint(0, 6.7057176, 1),AutoWavePoint(1, 13.3528595, 0),AutoWavePoint(2, 20.0, 1))),AutoWaveSection(44, 54, 2, 35, 1, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=25, zy=8)
        fun knock() = AutoWaveData(sections=arrayOf(AutoWaveSection(14, 20, 7, 41, 1, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 20.0, 0),AutoWavePoint(2, 20.0, 1),AutoWavePoint(3, 0.0, 1),AutoWavePoint(4, 0.0, 0),AutoWavePoint(5, 0.0, 0),AutoWavePoint(6, 0.0, 1))),AutoWaveSection(65, 20, 4, 40, 1, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 20.0, 0),AutoWavePoint(2, 20.0, 0),AutoWavePoint(3, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=15, zy=8)
        fun signal() = AutoWaveData(sections=arrayOf(AutoWaveSection(78, 64, 4, 20, 1, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 20.0, 0),AutoWavePoint(2, 20.0, 0),AutoWavePoint(3, 20.0, 1))),AutoWaveSection(0, 20, 4, 20, 3, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 6.6666665, 0),AutoWavePoint(2, 13.333333, 0),AutoWavePoint(3, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=0, zy=8)
        fun flirt1() = AutoWaveData(sections=arrayOf(AutoWaveSection(0, 20, 10, 36, 3, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 5.0, 0),AutoWavePoint(2, 10.0, 0),AutoWavePoint(3, 15.0, 0),AutoWavePoint(4, 20.0, 1),AutoWavePoint(5, 20.0, 1),AutoWavePoint(6, 20.0, 1),AutoWavePoint(7, 0.0, 1),AutoWavePoint(8, 0.0, 0),AutoWavePoint(9, 0.0, 1))),AutoWaveSection(0, 20, 2, 22, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=5, zy=8)
        fun flirt2() = AutoWaveData(sections=arrayOf(AutoWaveSection(27, 7, 10, 33, 3, arrayOf(AutoWavePoint(0, 0.2853297, 1),AutoWavePoint(1, 2.4758487, 0),AutoWavePoint(2, 4.6663675, 0),AutoWavePoint(3, 6.8568864, 0),AutoWavePoint(4, 9.047405, 0),AutoWavePoint(5, 11.237925, 0),AutoWavePoint(6, 13.428443, 0),AutoWavePoint(7, 15.618962, 0),AutoWavePoint(8, 17.80948, 0),AutoWavePoint(9, 20.0, 1))),AutoWaveSection(0, 20, 2, 40, 2, arrayOf(AutoWavePoint(0, 20.0, 1),AutoWavePoint(1, 0.0, 1))),AutoWaveSection(0, 20, 2, 20, 1, arrayOf(AutoWavePoint(0, 0.0, 1),AutoWavePoint(1, 20.0, 1)))), l=18, zy=8)
    }

    enum class AutoWaveStateMachine {
        SEND, SLEEP
    }

    data class AutoWaveState(val wave: AutoWaveData, var a: Int,var b: Int, var c: Int, var pc: Int, var j: Int, var points: Array<AutoWavePoint>, var section: Int,
                             var waveTiming: Int = 1, var lastWaveMaxTiming: Int = 1, var waveStrength: Float = 0f, var stateMachine: AutoWaveStateMachine = AutoWaveStateMachine.SEND,
                             var pow: Int = 1, var i7: Int = 0,var i2: Int = 0,var i3: Int = 0, val wavePlot: IntArray = IntArray(100), var f785A: Int = 0, val f813y: IntArray = IntArray(100), var f811w: Int = 0, var f812x: Int = 0) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as AutoWaveState

            if (wave != other.wave) return false
            if (a != other.a) return false
            if (b != other.b) return false
            if (c != other.c) return false
            if (pc != other.pc) return false
            if (j != other.j) return false
            if (!points.contentEquals(other.points)) return false
            if (section != other.section) return false
            if (waveTiming != other.waveTiming) return false
            if (lastWaveMaxTiming != other.lastWaveMaxTiming) return false
            if (waveStrength != other.waveStrength) return false
            if (stateMachine != other.stateMachine) return false
            if (pow != other.pow) return false
            if (i7 != other.i7) return false
            if (i2 != other.i2) return false
            if (i3 != other.i3) return false
            if (!wavePlot.contentEquals(other.wavePlot)) return false
            if (f785A != other.f785A) return false
            if (!f813y.contentEquals(other.f813y)) return false
            if (f811w != other.f811w) return false
            if (f812x != other.f812x) return false

            return true
        }

        override fun hashCode(): Int {
            var result = wave.hashCode()
            result = 31 * result + a
            result = 31 * result + b
            result = 31 * result + c
            result = 31 * result + pc
            result = 31 * result + j
            result = 31 * result + points.contentHashCode()
            result = 31 * result + section
            result = 31 * result + waveTiming
            result = 31 * result + lastWaveMaxTiming
            result = 31 * result + waveStrength.hashCode()
            result = 31 * result + stateMachine.hashCode()
            result = 31 * result + pow
            result = 31 * result + i7
            result = 31 * result + i2
            result = 31 * result + i3
            result = 31 * result + wavePlot.contentHashCode()
            result = 31 * result + f785A
            result = 31 * result + f813y.contentHashCode()
            result = 31 * result + f811w
            result = 31 * result + f812x
            return result
        }
    }
}