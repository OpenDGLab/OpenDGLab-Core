import WaveBank.basic
import WaveBank.touch
import kotlin.js.JsName
import kotlin.jvm.JvmStatic
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.round
import kotlin.random.Random

class WaveCenter {
    interface BasicWave
    class TouchRaw : BasicWave {
        var x: Double = 0.0
        var y: Double = 0.0
    }
    data class WaveDetail(val name: String, val wave: BasicWave)
    data class BasicWaveData(
        val A0: Int, //脉冲频率 左端值 取值 0-B
        val A1: Int,
        val A2: Int,
        val B0: Int, //脉冲频率 右端值 A-100
        val B1: Int,
        val B2: Int,
        val C0: Int, //脉冲形状 1 = 0.1s 取值 2-30
        val C1: Int,
        val C2: Int,
        val J0: Int, //小节真实时长 时长等于 向上取整(((J + 1) / 101)^(1.6) * 100) / 10
        val J1: Int, //实际时长等于 向上取整(小节真实时长/(脉冲形状/10)) * (脉冲形状/10)
        val J2: Int, //此值显示全部保留1位小数
        val PC0: Int, //类型 1-固定 2-节间渐变 3-元内渐变 4-节内渐变 5-阶梯渐变 6-每节随机 7-每元随机
        val PC1: Int,
        val PC2: Int,
        val JIE1: Int, //第二小节是否启用 0-不启用 1-启用
        val JIE2: Int, //第三小节
        val L: Int, //休息时长 小节间休息时长 向上取整(L/10)
        val ZY: Int, //高低频平衡 取值 0-20 越低高频越强 越高低频更强
        val points1: Array<BasicDataBean>, //小节
        val points2: Array<BasicDataBean>,
        val points3: Array<BasicDataBean>
    ) : BasicWave {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as BasicWaveData

            if (A0 != other.A0) return false
            if (A1 != other.A1) return false
            if (A2 != other.A2) return false
            if (B0 != other.B0) return false
            if (B1 != other.B1) return false
            if (B2 != other.B2) return false
            if (C0 != other.C0) return false
            if (C1 != other.C1) return false
            if (C2 != other.C2) return false
            if (J0 != other.J0) return false
            if (J1 != other.J1) return false
            if (J2 != other.J2) return false
            if (PC0 != other.PC0) return false
            if (PC1 != other.PC1) return false
            if (PC2 != other.PC2) return false
            if (JIE1 != other.JIE1) return false
            if (JIE2 != other.JIE2) return false
            if (L != other.L) return false
            if (ZY != other.ZY) return false
            if (!points1.contentEquals(other.points1)) return false
            if (!points2.contentEquals(other.points2)) return false
            if (!points3.contentEquals(other.points3)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = A0
            result = 31 * result + A1
            result = 31 * result + A2
            result = 31 * result + B0
            result = 31 * result + B1
            result = 31 * result + B2
            result = 31 * result + C0
            result = 31 * result + C1
            result = 31 * result + C2
            result = 31 * result + J0
            result = 31 * result + J1
            result = 31 * result + J2
            result = 31 * result + PC0
            result = 31 * result + PC1
            result = 31 * result + PC2
            result = 31 * result + JIE1
            result = 31 * result + JIE2
            result = 31 * result + L
            result = 31 * result + ZY
            result = 31 * result + points1.contentHashCode()
            result = 31 * result + points2.contentHashCode()
            result = 31 * result + points3.contentHashCode()
            return result
        }

    }
    data class BasicDataBean( //默认 y=x 一次函数
        val x: Int, //序列值 0-当前小节脉冲形状值
        val y: Float, //值 取值 0-20
        val anchor: Int //是否为变化节点 0-否 1-是
    )
    data class TouchWaveData(val data: Array<TouchDataBean>) : BasicWave {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as TouchWaveData

            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }
    }
    data class TouchDataBean(val ax: Int, val ay: Int, val az: Int)
    companion object {

        @JvmStatic
        @JsName("getBasicWave")
        public fun getBasicWave(name: String) = basic[name]
        @JvmStatic
        @JsName("getTouchWave")
        public fun getTouchWave(name: String) = touch[name]
        @JvmStatic
        @JsName("getRandomTouchWave")
        public fun getRandomTouchWave() : WaveDetail {
            val name = touch.keys.random()
            return WaveDetail(name, touch[name] ?: error(""))
        }
        @JvmStatic
        @JsName("getRandomBasicWave")
        public fun getRandomBasicWave() : WaveDetail {
            val name = basic.keys.random()
            return WaveDetail(name, basic[name] ?: error(""))
        }
        @JvmStatic
        @JsName("getRandomWave")
        public fun getRandomWave() = if(Random.Default.nextBoolean()) getRandomBasicWave() else getRandomTouchWave()
        @JvmStatic
        @JsName("getTouchWaveList")
        public fun getTouchWaveList() = touch.keys.toTypedArray()
        @JvmStatic
        @JsName("getBasicWaveList")
        public fun getBasicWaveList() = basic.keys.toTypedArray()
        fun stop() = KDataUtils.convertStringToByteArray("000000000000000000000000")
        @JvmStatic
        @JsName("fromOpenDGWaveGen")
        fun fromOpenDGWaveGen(odgw: String) : BasicWave? {
            // A0,A1,A2,B0,B1,B2,C0,C1,C2,J0,J1,J2,PC0,PC1,PC2,JIE1,JIE2,L,ZY|y=anchor,y=anchor,y=anchor|y=anchor,y=anchor,y=anchor|y=anchor,y=anchor,y=anchor
            val points1 = arrayListOf<BasicDataBean>()
            val points2 = arrayListOf<BasicDataBean>()
            val points3 = arrayListOf<BasicDataBean>()
            try {
                val ss = odgw.split("|")
                if (ss.size == 4) {
                    val conf = ss[0].split(",")
                    if (conf.size == 19) {
                        val p1 = ss[1].split(",")
                        if (p1.size == conf[6].toInt()) {
                            val p2 = ss[2].split(",")
                            if ((conf[15].toInt() == 1 && p2.size == conf[7].toInt()) || (conf[15].toInt() == 0 && p2[0] == "")) {
                                val p3 = ss[3].split(",")
                                if (conf[16].toInt() == 1 && p3.size == conf[8].toInt() || (conf[16].toInt() == 0 && p3[0] == "")) {
                                    p1.forEachIndexed { index, value ->
                                        val p = value.split("=")
                                        if (p.size == 2) {
                                            val anchor = p[1].toInt()
                                            if (anchor != 0 && anchor != 1) return null
                                            points1.add(BasicDataBean(index, p[0].toFloat(), anchor))
                                        } else return null
                                    }
                                    if (conf[15].toInt() == 1) p2.forEachIndexed { index, value ->
                                        val p = value.split("=")
                                        if (p.size == 2) {
                                            val anchor = p[1].toInt()
                                            if (anchor != 0 && anchor != 1) return null
                                            points2.add(BasicDataBean(index, p[0].toFloat(), anchor))
                                        } else return null
                                    }
                                    if (conf[16].toInt() == 1) p3.forEachIndexed { index, value ->
                                        val p = value.split("=")
                                        if (p.size == 2) {
                                            val anchor = p[1].toInt()
                                            if (anchor != 0 && anchor != 1) return null
                                            points3.add(BasicDataBean(index, p[0].toFloat(), anchor))
                                        } else return null
                                    }
                                    val a0 = conf[0].toInt()
                                    val a1 = conf[1].toInt()
                                    val a2 = conf[2].toInt()
                                    val b0 = conf[3].toInt()
                                    val b1 = conf[4].toInt()
                                    val b2 = conf[5].toInt()
                                    val c0 = conf[6].toInt()
                                    val c1 = conf[7].toInt()
                                    val c2 = conf[8].toInt()
                                    val j0 = conf[9].toInt()
                                    val j1 = conf[10].toInt()
                                    val j2 = conf[11].toInt()
                                    val pc0 = conf[12].toInt()
                                    val pc1 = conf[13].toInt()
                                    val pc2 = conf[14].toInt()
                                    val jie1 = conf[15].toInt()
                                    val jie2 = conf[16].toInt()
                                    val l = conf[17].toInt()
                                    val zy = conf[18].toInt()
                                    if (a0 > b0 || a0 < 0 || b0 > 100) return null
                                    if (a1 > b1 || a1 < 0 || b1 > 100) return null
                                    if (a2 > b2 || a2 < 0 || b2 > 100) return null
                                    if (c0 < 2 || c0 > 30) return null
                                    if (c1 < 2 || c1 > 30) return null
                                    if (c2 < 2 || c2 > 30) return null
                                    if (pc0 < 1 || pc0 > 7) return null
                                    if (pc1 < 1 || pc1 > 7) return null
                                    if (pc2 < 1 || pc2 > 7) return null
                                    if (jie1 != 0 && jie1 != 1) return null
                                    if (jie2 != 0 && jie2 != 1) return null
                                    if (zy < 0 || zy > 20) return null
                                    if (pc0 == 1 && a0 != 0) return null
                                    if (pc1 == 1 && a1 != 0) return null
                                    if (pc2 == 1 && a2 != 0) return null
                                    return BasicWaveData(
                                        a0,
                                        a1,
                                        a2,
                                        b0,
                                        b1,
                                        b2,
                                        c0,
                                        c1,
                                        c2,
                                        j0,
                                        j1,
                                        j2,
                                        pc0,
                                        pc1,
                                        pc2,
                                        jie1,
                                        jie2,
                                        l,
                                        zy,
                                        points1.toTypedArray(),
                                        points2.toTypedArray(),
                                        points3.toTypedArray()
                                    )
                                } else return null
                            } else return null
                        } else return null
                    } else return null
                } else return null
            } catch (e: Exception) {
                return null
            }
        }
        @JvmStatic
        @JsName("toDGWaveGen")
        fun toDGWaveGen(wave: BasicWaveData) : String {
            val p1 = wave.points1.joinToString(",") {
                "${it.y}=${it.anchor}"
            }
            val p2 = wave.points2.joinToString(",") {
                "${it.y}=${it.anchor}"
            }
            val p3 = wave.points3.joinToString(",") {
                "${it.y}=${it.anchor}"
            }
            return "${wave.A0},${wave.A1},${wave.A2},${wave.B0},${wave.B1},${wave.B2},${wave.C0},${wave.C1},${wave.C2},${wave.J0},${wave.J1},${wave.J2},${wave.PC0},${wave.PC1},${wave.PC2},${wave.JIE1},${wave.JIE2},${wave.L},${wave.ZY}|$p1|$p2|$p3"
        }
    }
    private var timeSeqTouch: Int = 0
    private var wave : BasicWave? = null
    private var waveConstructA = 0
    private var waveMaxTiming = 0
    private var waveTiming = 0
    private var lastWaveMaxTiming = 0
    private var f800l = 0
    private var waveConstructB = 0
    private var waveStrength = 0.0f
    private var f805q = 0
    private var f806r = 0.0f
    public val touchRaw = TouchRaw()
    //Plot
    private var f811w = 0
    private var f812x = 0
    private var f785A = 0
    private var f813y = IntArray(100)
    private var f814z = IntArray(100)
    private var plot = mutableListOf<Int>()

    private fun fromBasicWaveToByteArray(wave: BasicWaveData) : ByteArray {
        val waveC: Int
        val waveDataList: Array<BasicDataBean>
        val wavePC: Int
        val waveJ: Int
        val waveB: Int
        var waveA: Int
        when (this.waveConstructA) {
            0 -> {
                waveA = wave.A0 * 20 + 1000
                waveB = wave.B0 * 20 + 1000
                waveC = wave.C0
                waveJ = ceil(((wave.J0 + 1).toDouble() / 101.0).pow(1.6) * 100.0).toInt()
                wavePC = wave.PC0
                waveDataList = wave.points1
            }
            1 -> {
                waveA = wave.A1 * 20 + 1000
                waveB = wave.B1 * 20 + 1000
                waveC = wave.C1
                waveJ = ceil(((wave.J1 + 1).toDouble() / 101.0).pow(1.6) * 100.0).toInt()
                wavePC = wave.PC1
                waveDataList = wave.points2
            }
            2 -> {
                waveA = wave.A2 * 20 + 1000
                waveB = wave.B2 * 20 + 1000
                waveC = wave.C2
                waveJ = ceil(((wave.J2 + 1).toDouble() / 101.0).pow(1.6) * 100.0).toInt()
                wavePC = wave.PC2
                waveDataList = wave.points3
            }
            else -> {
                waveDataList = arrayOf()
                waveA = 0
                waveB = 0
                waveJ = 0
                wavePC = 0
                waveC = 0
            }
        }
        this.waveMaxTiming = ceil(waveJ.toDouble() / waveC.toDouble()).toInt()
        this.waveTiming = (round(this.waveMaxTiming.toDouble() * 1.0 * (this.waveTiming - 1).toDouble() / this.lastWaveMaxTiming.toDouble()) + 1).toInt()
        if (this.waveTiming < 1) {
            this.waveTiming = 1
        }
        lastWaveMaxTiming = waveMaxTiming
        this.f800l = ceil(wave.L.toDouble() / 10.0).toInt()
        when (this.waveConstructB) {
            0 -> {
                this.waveStrength = (this.waveStrength * waveC.toFloat() + 1.0f) / waveC.toFloat()
                if (wavePC == 4) {
                    if (waveMaxTiming > 1) {
                        waveA += (waveB - waveA) * (waveTiming - 1) / (waveMaxTiming - 1)
                    }
                } else if (wavePC == 3) {
                    waveA =
                        (waveA.toFloat() + (waveB - waveA).toFloat() * (waveC.toFloat() * this.waveStrength - 1.0f) / (waveC - 1).toFloat()).toInt()
                } else if (wavePC == 2) {
                    waveA =
                        (waveA.toDouble() + (waveB - waveA).toDouble() * 1.0 * (waveTiming.toFloat() + (waveC.toFloat() * this.waveStrength - 1.0f) / (waveC - 1).toFloat() - 1.0f).toDouble() / waveMaxTiming.toDouble()).toInt()
                }
                this.f805q = 10.0.pow((waveA.toFloat() / 1000.0f).toDouble()).toInt()
                this.f806r = (waveDataList[(waveC.toFloat() * this.waveStrength).toInt() - 1]).y
                var pow = ((this.f805q.toDouble() / 1000.0).pow(0.5) * wave.ZY.toDouble()).toInt()
                if (pow < 1) {
                    pow = 1
                }
                val i7: Int = this.f805q - pow
                wavePlot(pow.toDouble(), i7, this.f806r.toInt())
                var binaryString: String = pow.toString(radix = 2)
                var binaryString2: String = i7.toString(radix = 2)
                var binaryString3: String = this.f806r.toInt().toString(radix = 2)
                while (binaryString.length < 5) {
                    binaryString = "0$binaryString"
                }
                while (binaryString2.length < 10) {
                    binaryString2 = "0$binaryString2"
                }
                while (binaryString3.length < 5) {
                    binaryString3 = "0$binaryString3"
                }
                val result: ByteArray =
                    KDataUtils.convertStringToByteArray("0000$binaryString3$binaryString2$binaryString") //发送的数据
                if (this.waveStrength >= 1.0f) {
                    this.waveStrength = 0.0f
                    this.waveTiming++
                    if (this.waveTiming > this.waveMaxTiming) {
                        this.waveTiming = 1
                        when {
                            this.waveConstructA == 0 -> {
                                return when {
                                    wave.JIE1 == 1 -> {
                                        this.waveConstructA = 1
                                        result
                                    }
                                    wave.JIE2 == 1 -> {
                                        this.waveConstructA = 2
                                        result
                                    }
                                    this.f800l == 0 -> {
                                        this.waveConstructB = 0
                                        this.waveConstructA = 0
                                        result
                                    }
                                    else -> {
                                        this.waveConstructB = 1
                                        result
                                    }
                                }
                            }
                            this.waveConstructA == 1 -> {
                                return when {
                                    wave.JIE2 == 1 -> {
                                        this.waveConstructA = 2
                                        result
                                    }
                                    f800l == 0 -> {
                                        this.waveConstructB = 0
                                        this.waveConstructA = 0
                                        result
                                    }
                                    else -> {
                                        this.waveConstructB = 1
                                        result
                                    }
                                }
                            }
                            this.waveConstructA != 2 -> {
                                return result
                            }
                            else -> {
                                if (f800l == 0) {
                                    this.waveConstructB = 0
                                    this.waveConstructA = 0
                                    return result
                                }
                                this.waveConstructB = 1
                                return result
                            }
                        }
                    } else {
                        return result
                    }
                } else {
                    return result
                }
            }
            1 -> {
                this.waveStrength = (this.waveStrength * f800l.toFloat() + 1.0f) / f800l.toFloat()
                this.f806r = 0.0f
                this.f805q = 10
                wavePlot(0.0,0,0)
                var binaryString4: String = (0).toString(radix = 2)
                var binaryString5: String = (0).toString(radix = 2)
                var binaryString6: String = (0).toString(radix = 2)
                while (binaryString4.length < 5) {
                    binaryString4 = "0$binaryString4"
                }
                while (binaryString5.length < 10) {
                    binaryString5 = "0$binaryString5"
                }
                while (binaryString6.length < 5) {
                    binaryString6 = "0$binaryString6"
                }
                val result: ByteArray =
                    KDataUtils.convertStringToByteArray("0000$binaryString6$binaryString5$binaryString4")
                if (this.waveStrength >= 1.0f) {
                    this.waveStrength = 0.0f
                    this.waveConstructB = 0
                    waveConstructA = 0
                    return result
                }
                return result
            }
            else -> return byteArrayOf()
        }
    }
    public fun fromTouchRawToByteArray(x: Double, y: Double) : ByteArray {
        var pow = 10.0.pow(y * 2.5 + 0.5)
        if (pow < 10.0) {
            pow = 10.0
        }
        val pow2 = 20.0 - abs(x * 2.0 - 1.0).pow(1.65) * 20.0
        var pow3 = ((pow / 1000.0).pow(0.5) * 8.0).toInt()
        if (pow3 < 1) {
            pow3 = 1
        }
        val i2 = (pow - pow3.toDouble()).toInt()
        val i3 = pow2.toInt()
        wavePlot(pow, i2, i3)
        var binaryString: String = pow3.toString(radix = 2)
        var binaryString2: String = i2.toString(radix = 2)
        var binaryString3: String = i3.toString(radix = 2)
        while (binaryString.length < 5) {
            binaryString = "0$binaryString"
        }
        while (binaryString2.length < 10) {
            binaryString2 = "0$binaryString2"
        }
        while (binaryString3.length < 5) {
            binaryString3 = "0$binaryString3"
        }
        return KDataUtils.convertStringToByteArray("0000$binaryString3$binaryString2$binaryString")
    }
    private fun fromTouchWaveToByteArray(wave: TouchWaveData) : ByteArray {
        val time = timeSeqTouch % wave.data.size
        val ax: Int = wave.data[time].ax
        val ay: Int = wave.data[time].ay
        val az: Int = wave.data[time].az
        wavePlot(ax.toDouble(), az, ay)
        var binaryString4: String = ax.toString(radix = 2)
        var binaryString5: String = ay.toString(radix = 2)
        var binaryString6: String = az.toString(radix = 2)
        while (binaryString4.length < 5) {
            binaryString4 = "0$binaryString4"
        }
        while (binaryString5.length < 10) {
            binaryString5 = "0$binaryString5"
        }
        while (binaryString6.length < 5) {
            binaryString6 = "0$binaryString6"
        }
        return KDataUtils.convertStringToByteArray("0000$binaryString6$binaryString5$binaryString4")
    }
    @JsName("resetWave")
    public fun resetWave() {
        timeSeqTouch = 0
        waveConstructA = 0
        waveMaxTiming = 0
        waveTiming = 0
        lastWaveMaxTiming = 0
        f800l = 0
        waveConstructB = 0
        waveStrength = 0.0f
        f805q = 0
        f806r = 0.0f
        f811w = 0
        f812x = 0
        f785A = 0
        f813y = IntArray(100)
        f814z = IntArray(100)
        touchRaw.x = 0.0
        touchRaw.y = 0.0
    }
    @JsName("inputTouch")
    public fun inputTouch(x: Double, y: Double) {
        if (x in 0.0..1.0 && y in 0.0..1.0) {
            touchRaw.x = x
            touchRaw.y = y
        }
    }
    @JsName("selectWave")
    public fun selectWave(select: BasicWave?) {
        wave = select
        timeSeqTouch = 0
        waveMaxTiming = 0
        waveTiming = 0
        lastWaveMaxTiming = 0
        f800l = 0
        waveConstructB = 0
        waveStrength = 0.0f
        f805q = 0
        f806r = 0.0f
        f811w = 0
        f812x = 0
        f785A = 0
        f813y = IntArray(100)
        f814z = IntArray(10)
        touchRaw.x = 0.0
        touchRaw.y = 0.0
    }

    @JsName("toDGWaveGen")
    fun toDGWaveGen() : String? {
        val waveCopy = wave
        if (waveCopy is BasicWaveData) {
            return toDGWaveGen(waveCopy)
        }
        return null
    }

    @JsName("getWavePlot")
    public fun getWavePlot() : IntArray {
        return this.f814z
    }

    private fun wavePlot(pow: Double, i2: Int, i3: Int) {
        for (i4 in 0..99) {
            if (this.f811w < pow && this.f812x == 0) {
                this.f813y[i4] = 1
                this.f811w++
            } else {
                this.f811w = 0
                if (this.f812x < i2) {
                    this.f813y[i4] = 0
                    this.f812x++
                } else {
                    if (pow == 0.0) {
                        this.f813y[i4] = 0
                    } else {
                        this.f813y[i4] = 1
                    }
                    this.f811w = 1
                    this.f812x = 0
                }
            }
        }
        for (i5 in 0..9) {
            val i6 = i5 * 10
            if (this.f813y[i6] == 0 && this.f813y[i6 + 1] == 0 && this.f813y[i6 + 2] == 0 && this.f813y[i6 + 3] == 0 && this.f813y[i6 + 4] == 0 && this.f813y[i6 + 5] == 0 && this.f813y[i6 + 6] == 0 && this.f813y[i6 + 7] == 0 && this.f813y.get(i6 + 8) == 0 && this.f813y[i6 + 9] == 0
            ) {
                this.f814z[i5] = 0
            } else {
                this.f814z[i5] = this.f785A + (i3 - this.f785A) * (i5 + 1) / 10
            }
        }
        this.f785A = i3
    }

    @JsName("waveTick")
    public fun waveTick() : ByteArray? {
        val waveCopy = wave
        if (waveCopy == null) {
            timeSeqTouch = 0
            waveMaxTiming = 0
            waveTiming = 0
            lastWaveMaxTiming = 0
            f800l = 0
            waveConstructB = 0
            waveStrength = 0.0f
            f805q = 0
            f806r = 0.0f
            f811w = 0
            f812x = 0
            f785A = 0
            touchRaw.x = 0.0
            touchRaw.y = 0.0
            f813y = IntArray(100)
            f814z = IntArray(100)
            return null
        }
        return when (waveCopy) {
            is TouchWaveData -> {
                fromTouchWaveToByteArray(waveCopy).apply { timeSeqTouch++ }
            }
            is BasicWaveData -> {
                fromBasicWaveToByteArray(waveCopy)
            }
            is TouchRaw -> {
                fromTouchRawToByteArray(waveCopy.x, waveCopy.y)
            }
            else -> {
                null
            }
        }
    }
}