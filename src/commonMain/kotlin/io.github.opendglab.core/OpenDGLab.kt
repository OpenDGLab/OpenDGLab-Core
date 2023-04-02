package io.github.opendglab.core

import kotlin.math.*

object OpenDGLab {
    enum class DGChannel {
        CHANNEL_A, CHANNEL_B
    }
    fun calcXYZ(frequency: Double, z: Int): DGLabStruct.WaveData {
        if (frequency < 10 || frequency > 1000) throw Exceptions.DataOverflowException()
        if (frequency < 0) throw Exceptions.DataOverflowException()
        if (z > 31 || z < 0) throw Exceptions.DataOverflowException()
        var x = (frequency / 1000).pow(0.5) * 15
        var y = frequency - x
        if (x > 31) x = 31.0
        if (y > 1023) y = 1023.0
        return DGLabStruct.WaveData(x = x.roundToInt(), y = y.roundToInt(), z = z)
    }

    fun calcTouchWave(x: Double, y: Double) : DGLabStruct.WaveData {
        if (x > 1.0f || x < 0.0f || y > 1.0f || y < 0.0f) return DGLabStruct.WaveData(0,0,0)
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
        return DGLabStruct.WaveData(x = pow3, y = i2, z = i3)
    }

    fun calcTouchWave(timeSeq: Int, wave: Array<Waves.TouchWaveData>) : DGLabStruct.WaveData {
        val time = timeSeq % wave.size
        val ax: Int = wave[time].ax
        val ay: Int = wave[time].ay
        val az: Int = wave[time].az
        return DGLabStruct.WaveData(x = ax, y = ay, z = az)
    }

    fun calcAutoWave(state: Waves.AutoWaveState) : Pair<DGLabStruct.WaveData, Waves.AutoWaveState> {
        val waveMaxTiming = state.wave.waveMaxTimingSection[state.section]
        state.waveTiming = (round(waveMaxTiming.toDouble() * 1.0 * (state.waveTiming - 1).toDouble() / state.lastWaveMaxTiming.toDouble()) + 1).toInt()
        if (state.waveTiming < 1) state.waveTiming = 1
        state.lastWaveMaxTiming = state.waveTiming
        when (state.stateMachine) {
            Waves.AutoWaveStateMachine.SEND -> {
                state.waveStrength = (state.waveStrength * state.c.toFloat() + 1.0f) / state.c.toFloat()
                when (state.pc) {
                    4 -> {
                        if (waveMaxTiming > 1)
                            state.a += (state.b - state.a) * (state.waveTiming - 1) / (waveMaxTiming - 1)
                    }
                    3 -> {
                        state.a = (state.a.toFloat() + (state.b - state.a).toFloat() * (state.c.toFloat() * state.waveStrength - 1.0f) / (state.c - 1).toFloat()).toInt()
                    }
                    2 -> {
                        state.a =
                            (state.a.toDouble() + (state.b - state.a).toDouble() * 1.0 * (state.waveTiming.toFloat() + (state.c.toFloat() * state.waveStrength - 1.0f) / (state.c - 1).toFloat() - 1.0f).toDouble() / waveMaxTiming.toDouble()).toInt()
                    }
                }
                val f805q = 10.0.pow((state.a.toFloat() / 1000.0f).toDouble()).toInt()
                // Magic Fix
                var pf806r = (state.c.toFloat() * state.waveStrength).toInt() - 1
                if (pf806r < 0) pf806r = 0
                if (pf806r >= state.points.size) pf806r = state.points.size - 1
                // Magic Fix End
                val f806r = (state.points[pf806r]).y
                var pow = ((f805q.toDouble() / 1000.0).pow(0.5) * state.wave.zy.toDouble()).toInt()
                if (pow < 1) pow = 1
                val i7 = f805q - pow
                state.pow = pow
                state.i7 = i7
                state.i3 = f806r.toInt()
                if (state.waveStrength >= 1.0f) {
                    state.waveStrength = 0.0f
                    state.waveTiming = state.waveTiming + 1
                    if (state.waveTiming > waveMaxTiming) {
                        state.waveTiming = 1
                        state.section = state.section + 1
                        if (state.section >= state.wave.sections.size) {
                            state.section = 0
                            if (state.wave.l == 0) {
                                state.a = state.wave.sections[state.section].a
                                state.b = state.wave.sections[state.section].b
                                state.c = state.wave.sections[state.section].c
                                state.j = state.wave.sections[state.section].j
                                state.pc = state.wave.sections[state.section].pc
                                state.points = state.wave.sections[state.section].points
                                state.stateMachine = Waves.AutoWaveStateMachine.SEND
                            } else {
                                state.stateMachine = Waves.AutoWaveStateMachine.SLEEP
                            }
                        } else {
                            state.a = state.wave.sections[state.section].a
                            state.b = state.wave.sections[state.section].b
                            state.c = state.wave.sections[state.section].c
                            state.j = state.wave.sections[state.section].j
                            state.pc = state.wave.sections[state.section].pc
                            state.points = state.wave.sections[state.section].points
                        }
                    }
                }
                return Pair(DGLabStruct.WaveData(pow, i7, f806r.toInt()), state)
            }
            Waves.AutoWaveStateMachine.SLEEP -> {
                val f800l = ceil(state.wave.l.toDouble() / 10.0).toInt()
                state.waveStrength = (state.waveStrength * f800l.toFloat() + 1.0f) / f800l.toFloat()
                state.pow = 0
                state.i7 = 0
                state.i3 = 0
                if (state.waveStrength >= 1.0f) {
                    state.waveStrength = 0.0f
                    state.stateMachine = Waves.AutoWaveStateMachine.SEND
                    state.section = 0
                    state.a = state.wave.sections[state.section].a
                    state.b = state.wave.sections[state.section].b
                    state.c = state.wave.sections[state.section].c
                    state.j = state.wave.sections[state.section].j
                    state.pc = state.wave.sections[state.section].pc
                    state.points = state.wave.sections[state.section].points
                    return Pair(DGLabStruct.WaveData(0,0,0),state)
                }
            }
        }
        return Pair(DGLabStruct.WaveData(0,0,0), state)
    }

    fun calcWavePlot(waveState: Waves.AutoWaveState): IntArray {
        for (i4 in 0..99) {
            if (waveState.f811w < waveState.pow && waveState.f812x == 0) {
                waveState.f813y[i4] = 1
                waveState.f811w++
            } else {
                waveState.f811w = 0
                if (waveState.f812x < waveState.i2) {
                    waveState.f813y[i4] = 0
                    waveState.f812x++
                } else {
                    if (waveState.pow == 0) {
                        waveState.f813y[i4] = 0
                    } else {
                        waveState.f813y[i4] = 1
                    }
                    waveState.f811w = 1
                    waveState.f812x = 0
                }
            }
        }
        for (i5 in 0..9) {
            val i6 = i5 * 10
            if (waveState.f813y[i6] == 0 && waveState.f813y[i6 + 1] == 0 && waveState.f813y[i6 + 2] == 0 && waveState.f813y[i6 + 3] == 0 && waveState.f813y[i6 + 4] == 0 && waveState.f813y[i6 + 5] == 0 && waveState.f813y[i6 + 6] == 0 && waveState.f813y[i6 + 7] == 0 && waveState.f813y.get(i6 + 8) == 0 && waveState.f813y[i6 + 9] == 0
            ) {
                waveState.wavePlot[i5] = 0
            } else {
                waveState.wavePlot[i5] = waveState.f785A + (waveState.i3 - waveState.f785A) * (i5 + 1) / 10
            }
        }
        waveState.f785A = waveState.i3
        return waveState.wavePlot
    }
}