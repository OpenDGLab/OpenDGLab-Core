import kotlin.js.JsName

object KDataUtils {
    fun parseSetupData(bArr: ByteArray?): IntArray {
        if (bArr == null || bArr.isEmpty()) {
            return intArrayOf(0, 0)
        }
        val g = byteArrayToString(bArr.reversedArray())
        return intArrayOf(g.substring(5, 16).toInt(2), g.substring(16).toInt(2))
    }
    @JsName("byteArrayToString")
    public fun byteArrayToString(bArr: ByteArray): String {
        return bArr.asList().joinToString("") { commonBitwiseParse(it) }
    }
    private fun commonBitwiseParse(b: Byte): String {
        return (7 downTo 0).joinToString("") { (b.toInt() shr it and 1).toByte().toString() }
    }
    //修改强度
    fun changePower(i: Int, i2: Int): ByteArray {
        var binaryString = i.toString(radix = 2)
        var binaryString2 = i2.toString(radix = 2)
        binaryString = binaryString.padStart(11, '0')
        binaryString2 = binaryString2.padStart(11, '0')
        return convertStringToByteArray("00$binaryString2$binaryString")
    }
    @JsName("convertStringToByteArray")
    fun convertStringToByteArray(str: String): ByteArray {
        return ByteArray(3).mapIndexed { index, _ ->
            val i2 = index * 8
            bitStringToByte(str.substring(i2, i2 + 8))
        }.toByteArray().reversedArray()
    }
    private fun bitStringToByte(str: String?): Byte {
        return when {
            str == null -> {
                throw RuntimeException("when bit string convert to byte, Object can not be null!")
            }
            str.length != 8 -> {
                throw RuntimeException("bit string'length must be 8")
            }
            else -> {
                try {
                    when (str[0]) {
                        '0' -> str.toInt(2).toByte()
                        '1' -> (str.toInt(2) - 256).toByte()
                        else -> 0
                    }
                } catch (unused: NumberFormatException) {
                    throw RuntimeException("bit string convert to byte failed, byte String must only include 0 and 1!")
                }
            }
        }
    }
    //获取强度
    fun getABPower(bArr: ByteArray?, f907j: Int): IntArray {
        if (bArr == null || bArr.isEmpty()) {
            return intArrayOf(0, 0)
        }
        val g = byteArrayToString(bArr.reversedArray())
        val parseInt = g.substring(13).toInt(2)
        val parseInt2 = g.substring(2, 13).toInt(2)
        return intArrayOf(parseInt / f907j, parseInt2 / f907j)
    }
    //获取电量
    fun getElectric(bArr: ByteArray): Int {
        return if (bArr.size == 1) {
            commonBitwiseParse(bArr[0]).toInt(2)
        } else -1
    }
    //DFU
    fun dfu(bArr: ByteArray?): IntArray {
        if (bArr == null || bArr.isEmpty()) {
            return intArrayOf(0, 1)
        }
        val f: ByteArray = bArr.reversedArray()
        if (byteArrayToString(f).toInt(2) == 1) {
            return intArrayOf(0, 1)
        }
        val g: String = byteArrayToString(f)
        val substring = g.substring(0, 4)
        val substring2 = g.substring(4)
        return intArrayOf(substring.toInt(2), substring2.toInt(2))
    }
}