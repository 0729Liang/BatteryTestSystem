package com.liang.batterytestsystem.module.data

import com.blankj.utilcode.util.ConvertUtils
import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.liangutils.utils.LLogX
import org.java_websocket.util.ByteBufferUtils

/**
 * @author : Amarao
 * CreateAt : 10:02 PM 2019/5/4
 * Describe : 解析服务器返回的数据
 *
 */
class DeviceDataAnalysisUtils {
    /**
     * --------------------
     * 情景描述
     *
     * 当前状态：设备1 打开通道1，2 ；设备2 打开通道1
     * 发送两次查询命令
     * 接受两次数据
     *--------------------
     * 查询通道返回的数据格式
     *
     * 帧头 |  总字节数  | 命令 | 设备号 |  设备模式 | 通道状态* | 是否并机(高位)* | 是否并机(低位)* | 错误代码* | 重复*标记为|校验和 |帧尾
     * ----|-----------|------|-------|----------|----------|---------------|---------------|----------|:---------:|------|----
     * 7BH | 0x00 0x00 | 0x80 | 0x01  |    0x00  |   0x01   |   0x00        |      0x00     |    0x00  |     ***   | 0x00 |7D
     *
     * -------------------
     * 样本数据
     *
     * 第一次：
     *  发送：7B0000800100000000007D
     *  读取：7B0000800100 010000000100000009000000090000000900000009000000090000000900000009000000090000000900000009000000090000000900000009000000 007D
     * 第二次：
     *  发送：7B0000800200000000007D
     *  读取：7B0000800200 010000000900000009000000090000000900000009000000090000000900000009000000090000000900000009000000090000000900000009000000 007D
     * --------------------
     * */

    companion object {
        fun test() {

//            val b = 0x80.toByte()
//            LLogX.e(" 1="+b+" 2="+DigitalTrans.byte2hex(byteArrayOf(b))+" 3="+DigitalTrans.byte2hex(b))
//            val s = "80"
//            LLogX.e(" 1="+s+" 2="+DigitalTrans.byte2hex(DigitalTrans.hexStringToByte(s))+" 3="+DigitalTrans.byte2hex(ConvertUtils.hexString2Bytes(s)))

            val command = "7B0000800100010000000100000009000000090000000900000009000000090000000900000009000000090000000900000009000000090000000900000009000000007D"
            val comman2 = "7B0000800100010000000100000009000000090000000900000009000000090000000900000009000000090000000900000009000000090000000900000009000000007D"

            var bytes = DigitalTrans.hexString2Bytes(command)

            var bytes2: ByteArray = ByteArray(68)
            bytes.forEachIndexed { index, byte ->
                LLogX.e("byte size = " + bytes.size + " " + index + " = " + DigitalTrans.byte2hex(byte))
                bytes2.set(index, byte)
            }

            LLogX.e("bytes = " + DigitalTrans.byte2hex(bytes2))
            LLogX.e(" isequeue = " + DigitalTrans.byte2hex(bytes2).equals(command))
        }
    }


}