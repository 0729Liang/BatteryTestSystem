package com.liang.batterytestsystem.module.data

import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.liangutils.utils.LLogX


/**
 * @author : Amarao
 * CreateAt : 10:02 PM 2019/5/4
 * Describe : 解析服务器返回的数据
 *
 */
class DeviceDataAnalysisUtils {


    companion object {

        fun test() {

            val a = 1

            LLogX.e(" al = " + DigitalTrans.byte2hex(DigitalTrans.int2LittleEndian(a)))
            LLogX.e(" ab = " + DigitalTrans.byte2hex(DigitalTrans.int2BigEndian(a)))

            val testFloat = 1f
            val floatToByteArray = DigitalTrans.floatToByteArray(testFloat)

            LLogX.e(" f2b = " + DigitalTrans.byte2hex(floatToByteArray))
            LLogX.e(" b2f = " + DigitalTrans.byteArrayToFloat(floatToByteArray, 0))

            val byteArray = byteArrayOf(0x80.toByte(), 0x3F)
            val result = DigitalTrans.byte2Float(byteArray, 0)

            LLogX.e("b2f2 = " + result)
            LLogX.e(" f2b2 = " + DigitalTrans.byte2hex(DigitalTrans.float2byte(result)))

        }


        /**
         * 功能：根据通道索引得到通道状态索引
         */
        fun calcChannelStatusIndex(channelIndex: Int): Int {
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
             * 第一次：查询设备1
             *  发送：7B0000800100000000007D
             *  读取：7B0000800100 010000000100000009000000090000000900000009000000090000000900000009000000090000000900000009000000090000000900000009000000 007D
             * 第二次：查询设备2
             *  发送：7B0000800200000000007D
             *  读取：7B0000800200 010000000900000009000000090000000900000009000000090000000900000009000000090000000900000009000000090000000900000009000000 007D
             * --------------------
             *
             * */
            return channelIndex * 4 + 6
        }


        private val STEPTIME_OFFSET = 53 // 步时间s
        private val ELECTRIC_OFFSET = 9 // 电流a
        private val VOLTAGE_OFFSET = 5 // 电压v
        private val POWER_OFFSET = 13   // 功率w
        private val TEMPERTURE_OFFSET: Int = 25 // 温度℃
        private val AMPEREHOUR_OFFSET: Int = 17 // 安时ah

        fun calcStepTimeIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, STEPTIME_OFFSET)
        }

        fun calcElectricIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, ELECTRIC_OFFSET)
        }

        fun calcVoltageIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, VOLTAGE_OFFSET)
        }

        fun calcPowerIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, POWER_OFFSET)
        }

        fun calcTempertureIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, TEMPERTURE_OFFSET)
        }

        fun calcAmperehourIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, AMPEREHOUR_OFFSET)
        }

        /**
         * 功能：根据通道索引得到，和偏移量得到对应的数据索引
         *
         * 通用公式：n(0-14)=10+87*x+offset
         * offset=数据索引-基数索引，范围10-96，基数索引是10，如isSave的索引是11，则offset=11-10=1
         */
        fun calcCommandIndex(channelIndex: Int, offset: Int): Int {
            return channelIndex * 87 + 10 + offset
        }
    }


}