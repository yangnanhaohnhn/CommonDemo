package com.sinfotek.lib.common

import android.util.Base64
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.Throws
import kotlin.experimental.and

/**
 * Create 2020/10/29
 *
 * @author N
 * desc: 加密工具
 */
object RxEncryptUtil {
    private val base64EncodeChars = charArrayOf(
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'X',
        'Y',
        'Z',
        'a',
        'b',
        'c',
        'd',
        'e',
        'f',
        'g',
        'h',
        'i',
        'j',
        'k',
        'l',
        'm',
        'n',
        'o',
        'p',
        'q',
        'r',
        's',
        't',
        'u',
        'v',
        'w',
        'x',
        'y',
        'z',
        '0',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        '+',
        '/'
    )
    private val base64DecodeChars = byteArrayOf(
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        62,
        -1,
        -1,
        -1,
        63,
        52,
        53,
        54,
        55,
        56,
        57,
        58,
        59,
        60,
        61,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        0,
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16,
        17,
        18,
        19,
        20,
        21,
        22,
        23,
        24,
        25,
        -1,
        -1,
        -1,
        -1,
        -1,
        -1,
        26,
        27,
        28,
        29,
        30,
        31,
        32,
        33,
        34,
        35,
        36,
        37,
        38,
        39,
        40,
        41,
        42,
        43,
        44,
        45,
        46,
        47,
        48,
        49,
        50,
        51,
        -1,
        -1,
        -1,
        -1,
        -1
    )

    /**
     * 加密
     *
     * @param data：传入的数据
     * @return ：返回值
     */
    private fun encodeBase64(data: ByteArray): String {
        val sb = StringBuffer()
        val len = data.size
        var i = 0
        var b1: Int
        var b2: Int
        var b3: Int
        while (i < len) {
            b1 = (data[i++] and 0xff.toByte()).toInt()
            if (i == len) {
                sb.append(base64EncodeChars[b1 ushr 2])
                sb.append(base64EncodeChars[b1 and 0x3 shl 4])
                sb.append("==")
                break
            }
            b2 = (data[i++] and 0xff.toByte()).toInt()
            if (i == len) {
                sb.append(base64EncodeChars[b1 ushr 2])
                sb.append(base64EncodeChars[b1 and 0x03 shl 4 or (b2 and 0xf0 ushr 4)])
                sb.append(base64EncodeChars[b2 and 0x0f shl 2])
                sb.append("=")
                break
            }
            b3 = (data[i++] and 0xff.toByte()).toInt()
            sb.append(base64EncodeChars[b1 ushr 2])
            sb.append(base64EncodeChars[b1 and 0x03 shl 4 or (b2 and 0xf0 ushr 4)])
            sb.append(base64EncodeChars[b2 and 0x0f shl 2 or (b3 and 0xc0 ushr 6)])
            sb.append(base64EncodeChars[b3 and 0x3f])
        }
        return sb.toString()
    }

    /**
     * 解密
     *
     * @param str：传入的字符串
     * @return ：返回值
     */
    private fun decodeBase64(str: String): ByteArray {
        try {
            return decodePrivate(str)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return byteArrayOf()
    }

    @Throws(UnsupportedEncodingException::class)
    private fun decodePrivate(str: String): ByteArray {
        val sb = StringBuffer()
        var data: ByteArray? = null
        data = str.toByteArray(charset("US-ASCII"))
        val len = data.size
        var i = 0
        var b1: Int
        var b2: Int
        var b3: Int
        var b4: Int
        while (i < len) {
            do {
                b1 = base64DecodeChars[data[i++].toInt()].toInt()
            } while (i < len && b1 == -1)
            if (b1 == -1) {
                break
            }
            do {
                b2 = base64DecodeChars[data[i++].toInt()].toInt()
            } while (i < len && b2 == -1)
            if (b2 == -1) {
                break
            }
            sb.append((b1 shl 2 or (b2 and 0x30 ushr 4)).toChar())
            do {
                b3 = data[i++].toInt()
                if (b3 == 61) {
                    return sb.toString().toByteArray(charset("iso8859-1"))
                }
                b3 = base64DecodeChars[b3].toInt()
            } while (i < len && b3 == -1)
            if (b3 == -1) {
                break
            }
            sb.append((b2 and 0x0f shl 4 or (b3 and 0x3c ushr 2)).toChar())
            do {
                b4 = data[i++].toInt()
                if (b4 == 61) {
                    return sb.toString().toByteArray(charset("iso8859-1"))
                }
                b4 = base64DecodeChars[b4].toInt()
            } while (i < len && b4 == -1)
            if (b4 == -1) {
                break
            }
            sb.append((b3 and 0x03 shl 6 or b4).toChar())
        }
        return sb.toString().toByteArray(charset("iso8859-1"))
    }
    //TODO-------------------------------AES 256的加密算法
    /**
     * 加密 Aes256
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encodeAes256(sSrc: String, sKey: String?): String {
        if (sKey == null) {
            throw Exception("Key为空null")
        }
        // 判断Key是否为16位
        if (sKey.length != 16) {
            throw Exception("Key长度不是16位")
        }
        val raw = sKey.toByteArray()
        val sKeySpec = SecretKeySpec(raw, "AES")
        // "算法/模式/补码方式"
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        val iv = IvParameterSpec("0102030405060708".toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv)
        val encrypted = cipher.doFinal(sSrc.toByteArray())
        // 此处使用BASE64做转码功能，同时能起到2次加密的作用。
        return encodeBase64(encrypted)
    }

    /**
     * 解密 Aes256
     *
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decryptAes256(sSrc: String, sKey: String?): String {
        // 判断Key是否正确
        if (sKey == null) {
            throw Exception("Key为空null")
        }
        // 判断Key是否为16位
        if (sKey.length != 16) {
            throw Exception("Key长度不是16位")
        }
        val raw = sKey.toByteArray(StandardCharsets.UTF_8)
        val sKeySpec = SecretKeySpec(raw, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val iv = IvParameterSpec("0102030405060708".toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv)
        //先用base64解密
        val encrypted1 = decodeBase64(sSrc)
        val original = cipher.doFinal(encrypted1)
        return String(original)
    }

    //TODO ------------------------------Rsa 加密算法
    private const val GY =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDNhCDiLy0CG5jYN/HgCTj19DplFbL+Mh5dd8q9lmcSzVqQGK4vQzzvET1zS+zKUTNPqzIovPlqTKbZA1E5SDJSc0u3suQXIHPJ5V2EXfNf7d3atl6NynwAwiFtPh65bNTHWy0dkM4j5j9vo9rx4G9bb2qTXu81nSh6e5Zm63WTQIDAQAB"

    /**
     * RSA公钥加密
     *
     * @param str 加密字符串
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    @Throws(Exception::class)
    private fun encryptRsa(str: String): String {
        //base64编码的公钥
        val decoded = Base64.decode(GY, Base64.DEFAULT)
        val pubKey = KeyFactory.getInstance("RSA")
            .generatePublic(X509EncodedKeySpec(decoded)) as RSAPublicKey
        //RSA加密
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, pubKey)
        return Base64.encodeToString(
            cipher.doFinal(str.toByteArray(charset("UTF-8"))),
            Base64.DEFAULT
        )
    }

    /**
     * 对数据进行加密
     *
     * @param str
     * @return
     */
    fun encryptRes(str: String): String {
        return try {
            val resRsa = encryptRsa(str)
            URLEncoder.encode(resRsa, "UTF-8").replace("/+/g", "%2B")
        } catch (e: Exception) {
            ""
        }
    } //    /**
    //     * RSA私钥解密
    //     *
    //     * @param str 加密字符串
    //     *            私钥
    //     * @return 铭文
    //     * @throws Exception 解密过程中的异常信息
    //     */
    //    public static String decrypt(String str) throws Exception {
    //        String privateKey = SY;
    //        str = URLDecoder.decode(str, "UTF-8");
    //        //64位解码加密后的字符串
    //        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
    //        //base64编码的私钥
    //        byte[] decoded = Base64.decodeBase64(privateKey);
    //        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    //        //RSA解密
    //        Cipher cipher = Cipher.getInstance("RSA");
    //        cipher.init(Cipher.DECRYPT_MODE, priKey);
    //        return new String(cipher.doFinal(inputByte));
    //    }
}