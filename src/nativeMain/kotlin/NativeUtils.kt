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

fun getStringArraySize(array: Array<String>) = array.size

fun getStringArrayItemAt(array: Array<String>, index: Int) = array[index]
