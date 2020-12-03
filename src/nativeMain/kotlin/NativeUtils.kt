import kotlinx.cinterop.*

fun createByteArray(ptr: CPointer<ByteVar>, length: Int) =
     ptr.readBytes(length)

fun getByteArraySize(array: ByteArray) = array.size

fun toNativeByteArray(ptr: CPointer<ByteVar>, array: ByteArray) {
    array.forEachIndexed { index, byte -> ptr[index] = byte }
}

fun getIntArraySize(intArray: IntArray) = intArray.size

fun toNativeIntArray(ptr: CPointer<IntVar>, intArray: IntArray) {
    intArray.forEachIndexed { index, i -> ptr[index] = i }
}

fun getMapValue(map: Map<String, WaveCenter.BasicWave>, key: String) = map[key]

fun getMapKey(map: Map<String, WaveCenter.BasicWave>) = map.keys.toTypedArray()

fun getStringArraySize(array: Array<String>) = array.size

fun getStringArrayItemAt(array: Array<String>, index: Int) = array[index]

fun convertBasicWaveDataToBasicWave(wave: WaveCenter.BasicWaveData) = wave as WaveCenter.BasicWave
fun convertTouchWaveDataToBasicWave(wave: WaveCenter.TouchWaveData) = wave as WaveCenter.BasicWave
fun convertBasicWaveToBasicWaveDataUnsafe(wave: WaveCenter.BasicWave) = wave as WaveCenter.BasicWaveData
fun convertBasicWaveToTouchWaveDataUnsafe(wave: WaveCenter.BasicWave) = wave as WaveCenter.TouchWaveData
fun createNullBasicWave() : WaveCenter.BasicWave? = null