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
        val A0: Int,
        val A1: Int,
        val A2: Int,
        val B0: Int,
        val B1: Int,
        val B2: Int,
        val C0: Int,
        val C1: Int,
        val C2: Int,
        val J0: Int,
        val J1: Int,
        val J2: Int,
        val PC0: Int,
        val PC1: Int,
        val PC2: Int,
        val JIE1: Int,
        val JIE2: Int,
        val L: Int,
        val ZY: Int,
        val points1: Array<BasicDataBean>,
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
    data class BasicDataBean(val x: Int, val y: Float, val anchor: Int)
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
        val basic = mapOf<String, BasicWaveData>(
            "Breath" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 20,
                B1 = 20,
                B2 = 20,
                C0 = 8,
                C1 = 2,
                C2 = 2,
                J0 = 0,
                J1 = 20,
                J2 = 20,
                PC0 = 1,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 0,
                JIE2 = 0,
                L = 35,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 4f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 8f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 12f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 16f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Tide" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 32,
                B1 = 20,
                B2 = 20,
                C0 = 11,
                C1 = 2,
                C2 = 2,
                J0 = 20,
                J1 = 20,
                J2 = 20,
                PC0 = 2,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 0,
                JIE2 = 0,
                L = 5,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 3.3333333f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 6.6666665f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 10f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 13.333333f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 16.666666f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 18.402824f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 8,
                        y = 16.805649f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 9,
                        y = 15.208473f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 10,
                        y = 13.611298f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "KeepClick" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 34,
                B1 = 20,
                B2 = 20,
                C0 = 8,
                C1 = 2,
                C2 = 2,
                J0 = 20,
                J1 = 20,
                J2 = 20,
                PC0 = 1,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 0,
                JIE2 = 0,
                L = 0,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 13.333333f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 6.6666665f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 0.2399439f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 0.4798878f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "QuickPress" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 29,
                B1 = 20,
                B2 = 20,
                C0 = 2,
                C1 = 2,
                C2 = 2,
                J0 = 44,
                J1 = 20,
                J2 = 20,
                PC0 = 1,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 0,
                JIE2 = 0,
                L = 16,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Stronger" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 20,
                B1 = 20,
                B2 = 20,
                C0 = 11,
                C1 = 2,
                C2 = 2,
                J0 = 20,
                J1 = 20,
                J2 = 20,
                PC0 = 1,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 0,
                JIE2 = 0,
                L = 5,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 5.719211f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 10.499579f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 0.3826058f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 14.682558f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 17.454998f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 8,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 9,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 10,
                        y = 0f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Heartbeat" to BasicWaveData(
                A0 = 65,
                A1 = 0,
                A2 = 0,
                B0 = 20,
                B1 = 20,
                B2 = 20,
                C0 = 2,
                C1 = 14,
                C2 = 2,
                J0 = 6,
                J1 = 20,
                J2 = 20,
                PC0 = 1,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 1,
                JIE2 = 0,
                L = 5,
                ZY = 16,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 0f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 0f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 0f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 14.972563f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 16.648375f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 18.324188f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 8,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 9,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 10,
                        y = 0f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 11,
                        y = 0f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 12,
                        y = 0f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 13,
                        y = 0f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Compress" to BasicWaveData(
                A0 = 52,
                A1 = 0,
                A2 = 0,
                B0 = 16,
                B1 = 20,
                B2 = 20,
                C0 = 11,
                C1 = 10,
                C2 = 2,
                J0 = 0,
                J1 = 0,
                J2 = 20,
                PC0 = 2,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 1,
                JIE2 = 0,
                L = 0,
                ZY = 16,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 8,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 9,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 10,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 8,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 9,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Rhythmic" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 20,
                B1 = 20,
                B2 = 20,
                C0 = 26,
                C1 = 2,
                C2 = 2,
                J0 = 20,
                J1 = 20,
                J2 = 20,
                PC0 = 1,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 0,
                JIE2 = 0,
                L = 5,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 4f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 8f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 12f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 16f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 5f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 8,
                        y = 10f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 9,
                        y = 15f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 10,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 11,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 12,
                        y = 6.6666665f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 13,
                        y = 13.333333f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 14,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 15,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 16,
                        y = 10f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 17,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 18,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 19,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 20,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 21,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 22,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 23,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 24,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 25,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "GrainTouch" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 38,
                B1 = 20,
                B2 = 20,
                C0 = 4,
                C1 = 2,
                C2 = 2,
                J0 = 25,
                J1 = 20,
                J2 = 20,
                PC0 = 2,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 0,
                JIE2 = 0,
                L = 0,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 0f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Spring" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 30,
                B1 = 20,
                B2 = 20,
                C0 = 4,
                C1 = 2,
                C2 = 2,
                J0 = 45,
                J1 = 20,
                J2 = 20,
                PC0 = 2,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 0,
                JIE2 = 0,
                L = 20,
                ZY = 16,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0.18084228f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 6.7872286f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 13.393615f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 20.0f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "WaveRipple" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 60,
                B1 = 20,
                B2 = 20,
                C0 = 4,
                C1 = 2,
                C2 = 2,
                J0 = 53,
                J1 = 20,
                J2 = 20,
                PC0 = 4,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 0,
                JIE2 = 0,
                L = 5,
                ZY = 16,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 10f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 14.669602f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "RainSwept" to BasicWaveData(
                A0 = 4,
                A1 = 44,
                A2 = 0,
                B0 = 0,
                B1 = 54,
                B2 = 20,
                C0 = 3,
                C1 = 2,
                C2 = 2,
                J0 = 39,
                J1 = 35,
                J2 = 20,
                PC0 = 1,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 1,
                JIE2 = 0,
                L = 25,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 6.7057176f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 13.3528595f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Knock" to BasicWaveData(
                A0 = 14,
                A1 = 65,
                A2 = 0,
                B0 = 20,
                B1 = 20,
                B2 = 20,
                C0 = 7,
                C1 = 4,
                C2 = 2,
                J0 = 41,
                J1 = 40,
                J2 = 20,
                PC0 = 1,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 1,
                JIE2 = 0,
                L = 15,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 0f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 0f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 0f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Signal" to BasicWaveData(
                A0 = 78,
                A1 = 0,
                A2 = 0,
                B0 = 64,
                B1 = 20,
                B2 = 20,
                C0 = 4,
                C1 = 4,
                C2 = 2,
                J0 = 20,
                J1 = 20,
                J2 = 20,
                PC0 = 1,
                PC1 = 3,
                PC2 = 1,
                JIE1 = 1,
                JIE2 = 0,
                L = 0,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 20f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 6.6666665f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 13.333333f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Flirt 1" to BasicWaveData(
                A0 = 0,
                A1 = 0,
                A2 = 0,
                B0 = 20,
                B1 = 20,
                B2 = 20,
                C0 = 10,
                C1 = 2,
                C2 = 2,
                J0 = 36,
                J1 = 22,
                J2 = 20,
                PC0 = 3,
                PC1 = 1,
                PC2 = 1,
                JIE1 = 1,
                JIE2 = 0,
                L = 5,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 5f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 10f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 15f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 8,
                        y = 0f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 9,
                        y = 0f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
            "Flirt 2" to BasicWaveData(
                A0 = 27,
                A1 = 0,
                A2 = 0,
                B0 = 7,
                B1 = 20,
                B2 = 20,
                C0 = 10,
                C1 = 2,
                C2 = 2,
                J0 = 33,
                J1 = 40,
                J2 = 20,
                PC0 = 3,
                PC1 = 2,
                PC2 = 1,
                JIE1 = 1,
                JIE2 = 0,
                L = 18,
                ZY = 8,
                points1 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0.2853297f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 2.4758487f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 2,
                        y = 4.6663675f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 3,
                        y = 6.8568864f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 4,
                        y = 9.047405f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 5,
                        y = 11.237925f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 6,
                        y = 13.428443f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 7,
                        y = 15.618962f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 8,
                        y = 17.80948f,
                        anchor = 0
                    ),
                    BasicDataBean(
                        x = 9,
                        y = 20f,
                        anchor = 1
                    ),
                ),
                points2 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 20f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 0f,
                        anchor = 1
                    ),
                ),
                points3 = arrayOf(
                    BasicDataBean(
                        x = 0,
                        y = 0f,
                        anchor = 1
                    ),
                    BasicDataBean(
                        x = 1,
                        y = 20f,
                        anchor = 1
                    ),
                ),
            ),
        )
        val touch = mapOf<String, TouchWaveData>(
            "Extrusion" to TouchWaveData(
                arrayOf(
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 20)
                )
            ),
            "Bubble" to TouchWaveData(
                arrayOf(
                    TouchDataBean(3, 42, 0),
                    TouchDataBean(3, 42, 20)
                )
            ),
            "Signal" to TouchWaveData(
                arrayOf(
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 10),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 10),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(3, 42, 0),
                    TouchDataBean(2, 23, 20),
                    TouchDataBean(2, 28, 20),
                    TouchDataBean(3, 35, 20),
                    TouchDataBean(3, 43, 20),
                    TouchDataBean(3, 54, 20),
                    TouchDataBean(0, 0, 0),
                    TouchDataBean(0, 0, 0)
                )
            ),
            "Climb" to TouchWaveData(
                arrayOf(
                    TouchDataBean(3, 53, 10),
                    TouchDataBean(3, 36, 12),
                    TouchDataBean(2, 26, 14),
                    TouchDataBean(2, 17, 16),
                    TouchDataBean(1, 13, 18),
                    TouchDataBean(1, 9, 20),
                )
            ),
            "Rhythm" to TouchWaveData(
                arrayOf(
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 20)
                )
            ),
            "KeepClick" to TouchWaveData(
                arrayOf(
                    TouchDataBean(5, 95, 20)
                )
            ),
            "Pulse" to TouchWaveData(
                arrayOf(
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 11, 20),
                    TouchDataBean(1, 14, 20),
                    TouchDataBean(2, 16, 20),
                    TouchDataBean(2, 20, 20),
                    TouchDataBean(2, 26, 20),
                    TouchDataBean(2, 32, 20),
                    TouchDataBean(3, 39, 20),
                    TouchDataBean(3, 49, 20),
                    TouchDataBean(4, 60, 20),
                    TouchDataBean(4, 75, 20),
                    TouchDataBean(4, 93, 20),
                    TouchDataBean(5, 115, 20),
                    TouchDataBean(6, 141, 20),
                    TouchDataBean(6, 175, 20),
                    TouchDataBean(7, 216, 20)
                )
            ),
            "AirWaves" to TouchWaveData(
                arrayOf(
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(2, 15, 20),
                    TouchDataBean(2, 28, 20),
                    TouchDataBean(3, 49, 20),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(1, 9, 0),
                    TouchDataBean(1, 9, 20),
                    TouchDataBean(0, 0, 0),
                    TouchDataBean(0, 0, 0)
                )
            )
        )
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
                val result: ByteArray = KDataUtils.convertStringToByteArray("0000$binaryString3$binaryString2$binaryString") //发送的数据
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
                val result: ByteArray = KDataUtils.convertStringToByteArray("0000$binaryString6$binaryString5$binaryString4")
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
        touchRaw.x = 0.0
        touchRaw.y = 0.0
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
            touchRaw.x = 0.0
            touchRaw.y = 0.0
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