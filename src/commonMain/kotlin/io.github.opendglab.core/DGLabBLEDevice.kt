package io.github.opendglab.core

class DGLabBLEDevice(val waveSender: (a: DGLabStruct.WaveData, b: DGLabStruct.WaveData) -> Unit, val powerSender: (power: ByteArray) -> Unit, val powerCallback: (a: Int, b: Int) -> Unit, val batteryCallback: (level: Int) -> Unit) {
    companion object {
        const val name = "D-LAB ESTIM01"
        const val serviceBattery = "955a180a-0fe2-f5aa-a094-84b8d4f3e8ad"
        const val characteristicBattery = "955a1500-0fe2-f5aa-a094-84b8d4f3e8ad"
        const val serviceEStim = "955a180b-0fe2-f5aa-a094-84b8d4f3e8ad"
        const val characteristicEStimPower = "955a1504-0fe2-f5aa-a094-84b8d4f3e8ad"
        const val characteristicEStimA = "955a1505-0fe2-f5aa-a094-84b8d4f3e8ad"
        const val characteristicEStimB = "955a1506-0fe2-f5aa-a094-84b8d4f3e8ad"
    }

    private var channelAPower = 0
    private var channelBPower = 0

    private lateinit var channelAWave: Waves.AutoWaveState
    private lateinit var channelBWave: Waves.AutoWaveState

    init {
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_A, Waves.AutoWave.off())
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_B, Waves.AutoWave.off())
    }

    fun selectAutoWave(channel: OpenDGLab.DGChannel, wave: Waves.AutoWaveData) {
        when (channel) {
            OpenDGLab.DGChannel.CHANNEL_A -> {
                channelAWave = Waves.AutoWaveState(wave=wave, wave.sections[0].a,wave.sections[0].b,wave.sections[0].c,wave.sections[0].pc,wave.sections[0].j, wave.sections[0].points, 0)
            }
            OpenDGLab.DGChannel.CHANNEL_B -> {
                channelBWave = Waves.AutoWaveState(wave=wave, wave.sections[0].a,wave.sections[0].b,wave.sections[0].c,wave.sections[0].pc,wave.sections[0].j, wave.sections[0].points, 0)
            }
        }
    }

    fun selectPower(a: Int = channelAPower, b: Int = channelBPower) {
        if (a < 0 || a > 2047) throw Exceptions.DataOverflowException()
        if (b < 0 || b > 2047) throw Exceptions.DataOverflowException()
        powerSender(DGLabStruct.Power(a, b).power)
    }

    fun stopAll() {
        powerSender(DGLabStruct.Power(0, 0).power)
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_A, Waves.AutoWave.off())
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_B, Waves.AutoWave.off())
    }

    fun stop(channel: OpenDGLab.DGChannel) {
        when(channel) {
            OpenDGLab.DGChannel.CHANNEL_A -> {
                powerSender(DGLabStruct.Power(0, channelBPower).power)
                selectAutoWave(OpenDGLab.DGChannel.CHANNEL_A, Waves.AutoWave.off())
            }
            OpenDGLab.DGChannel.CHANNEL_B -> {
                powerSender(DGLabStruct.Power(channelAPower, 0).power)
                selectAutoWave(OpenDGLab.DGChannel.CHANNEL_B, Waves.AutoWave.off())
            }
        }
    }

    // 100ms (0.1s) 调用一次
    fun callAutoWaveTimer() {
        val (aWave, waveAState) = OpenDGLab.calcAutoWave(channelAWave)
        channelAWave = waveAState

        val (bWave, waveBState) = OpenDGLab.calcAutoWave(channelBWave)
        channelBWave = waveBState
        if (channelAPower == 0 && channelBPower == 0) return
        waveSender(aWave, bWave)
    }

    fun callbackBattery(level: ByteArray) {
        batteryCallback(DGLabStruct.BatteryLevel(level).getLevel())
    }

    fun callbackPower(power: ByteArray) {
        val sPower = DGLabStruct.Power(power)
        channelAPower = sPower.a
        channelBPower = sPower.b
        powerCallback(sPower.a, sPower.b)
    }
}