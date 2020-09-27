import kotlin.js.JsName

object KDataUtils {
    fun parseSetupData(bArr: ByteArray?): IntArray {
        if (bArr == null || bArr.isEmpty()) {
            return intArrayOf(0, 0)
        }
        val g = byteArrayToString(parseByteArray(bArr))
//        val substring: String = g.substring(5, 16)
        return intArrayOf(g.substring(5, 16).toInt(2), g.substring(16).toInt(2))
    }
    @JsName("byteArrayToString")
    public fun byteArrayToString(bArr: ByteArray): String {
        return bArr.asList().map { commonBitwiseParse(it) }.joinToString("")
//        var str = ""
//        for (i in bArr.indices) {
//            str += commonBitwiseParse(bArr[i])
//        }
//        return str
    }
    private fun commonBitwiseParse(b: Byte): String {
        return "" + (b.toInt() shr 7 and 1).toByte() + (b.toInt() shr 6 and 1).toByte() + (b.toInt() shr 5 and 1).toByte() + (b.toInt() shr 4 and 1).toByte() + (b.toInt() shr 3 and 1).toByte() + (b.toInt() shr 2 and 1).toByte() + (b.toInt() shr 1 and 1).toByte() + (b.toInt() shr 0 and 1).toByte()
    }
    private fun parseByteArray(bArr: ByteArray): ByteArray {
        return when {
            bArr.size == 3 -> {
                val b = bArr[0]
                bArr[0] = bArr[2]
                bArr[2] = b
                bArr
            }
            bArr.size != 2 -> {
                bArr
            }
            else -> {
                val b2 = bArr[0]
                bArr[0] = bArr[1]
                bArr[1] = b2
                bArr
            }
        }
    }
    //修改强度
    fun changePower(i: Int, i2: Int): ByteArray {
        var binaryString = i.toString(radix = 2)
        var binaryString2 = i2.toString(radix = 2)
        while (binaryString.length < 11) {
            binaryString = "0$binaryString"
        }
        while (binaryString2.length < 11) {
            binaryString2 = "0$binaryString2"
        }
//        val str = "00$binaryString2$binaryString"
        //println("test A:$binaryString , B:$binaryString2 ,apower:$i ,bpower:$i2")
        return convertStringToByteArray("00$binaryString2$binaryString")
    }
    @JsName("convertStringToByteArray")
    fun convertStringToByteArray(str: String): ByteArray {
        val bArr = ByteArray(3)
        for (i in 0..2) {
            val i2 = i * 8
            bArr[i] = bitStringToByte(str.substring(i2, i2 + 8))
        }
        return parseByteArray(bArr)
    }
    fun bitStringToByte(str: String?): Byte {
        return when {
            str == null -> {
                throw RuntimeException("when bit string convert to byte, Object can not be null!")
            }
            8 != str.length -> {
                throw RuntimeException("bit string'length must be 8")
            }
            else -> {
                try {
                    if (str[0] == '0') {
                        return str.toInt(2).toByte()
                    }
                    if (str[0] == '1') {
                        return (str.toInt(2) - 256).toByte()
                    }
                    0
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
        val g = byteArrayToString(parseByteArray(bArr))
        val substring: String = g.substring(2, 13)
        val parseInt: Int = g.substring(13).toInt(2)
        val parseInt2 = substring.toInt(2)
        //println("strength realA = " + parseInt2 / f907j + " realB = " + parseInt / f907j)
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
        val f: ByteArray = parseByteArray(bArr)
        if (byteArrayToString(f).toInt(2) == 1) {
            return intArrayOf(0, 1)
        }
        val g: String = byteArrayToString(f)
        val substring = g.substring(0, 4)
        val substring2 = g.substring(4)
        return intArrayOf(substring.toInt(2), substring2.toInt(2))
    }
}