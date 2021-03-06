package com.liang.batterytestsystem.module.data

import com.liang.batterytestsystem.module.home.DeviceCommand
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

            for (x in 0..3) {
                LLogX.e(x)
            }
//            val a = 1
//
//            LLogX.e(" al = " + DigitalTrans.byte2hex(DigitalTrans.int2LittleEndian(a)))
//            LLogX.e(" ab = " + DigitalTrans.byte2hex(DigitalTrans.int2BigEndian(a)))
//
//            val testFloat = 1f
//            val floatToByteArray = DigitalTrans.floatToByteArray(testFloat)
//
//            LLogX.e(" f2b = " + DigitalTrans.byte2hex(floatToByteArray))
//            LLogX.e(" b2f = " + DigitalTrans.byteArrayToFloat(floatToByteArray, 0))
//
//            val byteArray = byteArrayOf(0x80.toByte(), 0x3F)
//            val result = DigitalTrans.byte2Float(byteArray, 0)
//
//            LLogX.e("b2f2 = " + result)
//            LLogX.e(" f2b2 = " + DigitalTrans.byte2hex(DigitalTrans.float2byte(result)))
//
//            val array = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
//            print(array.copyOfRange(0, 3))
//            print(array.copyOfRange(2, 4))
//            print(array)
//
//            /*
//            * * 常用数据偏移量如下
//            * 步时间：offset=53 位数：8位   单位 S    索引：63-70
//            * 电流：offset=9   位数：4位   单位：A    索引：19-22
//            * 电压：offset=5   位数：4位   单位：V    索引：15-18
//            * 功率：offset=13  位数：4位   单位：W    索引：23-26
//            * 温度：offset=25  位数：2位   单位：℃    索引：35-36
//            * 安时：offset=17  位数：4位   单位：Ah   索引：27-30*/
//            LLogX.e("步时间 = " + calcStepTimeIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index) + "-" + calcStepTimeEndIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index))
//            LLogX.e("电流： = " + calcElectricIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index) + "-" + calcElectricEndIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index))
//            LLogX.e("电压： = " + calcVoltageIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index) + "-" + calcVoltageEndIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index))
//            LLogX.e("功率： = " + calcPowerIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index) + "-" + calcPowerEndIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index))
//            LLogX.e("温度： = " + calcTempertureIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index) + "-" + calcTempertureEndIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index))
//            LLogX.e("安时： = " + calcAmperehourIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index) + "-" + calcAmperehourEndIndex(DeviceCommand.Companion.CHANNEL.CHANNEL_1.index))
//
//
//            val s = "7B0000710100000000017B010172FD000000670000016D000000D600000347000000CCC377C3B30062004C0015000B000002FF000000E9000001E40000038F000000000000032900000000000001B900E60000013800000270000000000000067D007D"
//            var bytes = DigitalTrans.hexString2Bytes(s)
//
//            /*
//            * 7B0000 71 01（设备号）00000000 01（包含的通道数）
//            * 7B、01（是否保存）、：01（通道号）72FD
//            * 00000067（电压）0000016D（电流）000000D6（功率）00000347（总安时）000000CC（总瓦时）C377（温度1）C3B3（温度2）
//            * 0062（循环次数）004C（循环1）0015（循环2）000B（循环3）000002FF000000E9000001E40000038F
//            * 0000000000000329（步时间）00000000000001B9（总时间）00E6 00000138 00000270（容量）0000 0000 00（采）00 06 7D（通道帧尾）007D
//            * */
//            LLogX.e("设备号 = "+DigitalTrans.byte2hex(bytes[4])+ " s = "+bytes.size)
//            LLogX.e("步时间 = "+ DigitalTrans.byte2hex(getStepTime(bytes,0)))
//            LLogX.e("电流 = "+DigitalTrans.byte2hex(getElectric(bytes,0)))
//            LLogX.e("电压 = "+DigitalTrans.byte2hex(getVoltage(bytes,0)))
//            LLogX.e("功率 = "+DigitalTrans.byte2hex(getPower(bytes,0)))
//            LLogX.e("温度 = "+DigitalTrans.byte2hex(getTemperture(bytes,0)))
//            LLogX.e("安时 = "+DigitalTrans.byte2hex(getAmperehour(bytes,0)))

            /*
            * 00000067（电压）6.04
            * 0000016D（电流） 2.49
            * 000000D6（功率）-3.51
            * 00000347（总安时）33536.0
            * C377（温度1）7.91 /100
            * 0000000000000329（步时间）2.9
            * */
            var elec = DigitalTrans.hexString2Bytes("00000329")//
            LLogX.e("r1 = " + DigitalTrans.byte2hex(elec))
            LLogX.e(" f1 = " + DigitalTrans.byte2Float(elec, 0))
            elec.reverse()
            LLogX.e("r2 = " + DigitalTrans.byte2hex(elec))
            LLogX.e(" f2 = " + DigitalTrans.byte2Float(elec, 0))


        }

        fun print(array: IntArray) {
            LLogX.e(" s = " + array.size)
            array.forEach {
                LLogX.v(" " + it)
            }
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

        /**
         * 功能：更新选中设备的数据，并通知adapter更新
         * 一条通道字节：174/2=87
         * 一条通道命令模板：7B***7D
         * 最多16通道
         * 数据范围：
         *  通道1：10-96，
         *  通道2：97-183、
         *  通道3：184-270
         *  *
         *  *
         *  *
         *  通道x：10+87*x+offset  ~ 96+87*x+offset
         *
         * 通用公式：n(0-14)=10+87*x+offset
         * offset=数据索引-基数索引，范围10-96，基数索引是10，如isSave的索引是11，则offset=11-10=1
         *
         * 常用数据偏移量如下
         * 步时间：offset=53 位数：8位   单位 S    索引：63-70
         * 电流：offset=9   位数：4位   单位：A    索引：19-22
         * 电压：offset=5   位数：4位   单位：V    索引：15-18
         * 功率：offset=13  位数：4位   单位：W    索引：23-26
         * 温度：offset=25  位数：2位   单位：℃    索引：35-36
         * 安时：offset=17  位数：4位   单位：Ah   索引：27-30
         */

        private val STEPTIME_OFFSET = 57 //53 // 步时间s
        private val ELECTRIC_OFFSET = 9 // 电流a
        private val VOLTAGE_OFFSET = 5 // 电压v
        private val POWER_OFFSET = 13   // 功率w
        private val TEMPERTURE_OFFSET: Int = 25 // 温度℃
        private val AMPEREHOUR_OFFSET: Int = 17 // 安时ah

        private val CHANNELID_OFFSET = 2// 通道号


        private val DATA_2_BYTE = 2
        private val DATA_4_BYTE = 4
        private val DATA_8_BYTE = 8


        /**
         * 功能：步时间：offset=53 位数：8位   单位 S
         */
        fun calcStepTimeIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, STEPTIME_OFFSET)
        }

        fun calcStepTimeEndIndex(channelIndex: Int): Int {
            return calcCommandEndIndex(channelIndex, STEPTIME_OFFSET, DATA_4_BYTE)
        }

        fun getStepTime(byteArray: ByteArray, channelIndex: Int): ByteArray {
            return byteArray.copyOfRange(calcStepTimeIndex(channelIndex), calcStepTimeEndIndex(channelIndex))
        }

        /**
         * 功能：电流：offset=9   位数：4位   单位：A
         */
        fun calcElectricIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, ELECTRIC_OFFSET)
        }

        fun calcElectricEndIndex(channelIndex: Int): Int {
            return calcCommandEndIndex(channelIndex, ELECTRIC_OFFSET, DATA_4_BYTE)
        }

        fun getElectric(byteArray: ByteArray, channelIndex: Int): ByteArray {
            return byteArray.copyOfRange(calcElectricIndex(channelIndex), calcElectricEndIndex(channelIndex))
        }

        /**
         * 功能：电压：offset=5   位数：4位   单位：V
         */
        fun calcVoltageIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, VOLTAGE_OFFSET)
        }

        fun calcVoltageEndIndex(channelIndex: Int): Int {
            return calcCommandEndIndex(channelIndex, VOLTAGE_OFFSET, DATA_4_BYTE)
        }

        fun getVoltage(byteArray: ByteArray, channelIndex: Int): ByteArray {
            return byteArray.copyOfRange(calcVoltageIndex(channelIndex), calcVoltageEndIndex(channelIndex))
        }

        /**
         * 功能：功率：offset=13  位数：4位   单位：W
         */
        fun calcPowerIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, POWER_OFFSET)
        }

        fun calcPowerEndIndex(channelIndex: Int): Int {
            return calcCommandEndIndex(channelIndex, POWER_OFFSET, DATA_4_BYTE)
        }

        fun getPower(byteArray: ByteArray, channelIndex: Int): ByteArray {
            return byteArray.copyOfRange(calcPowerIndex(channelIndex), calcPowerEndIndex(channelIndex))
        }

        /**
         * 功能：温度：offset=25  位数：2位   单位：℃
         */
        fun calcTempertureIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, TEMPERTURE_OFFSET)
        }

        fun calcTempertureEndIndex(channelIndex: Int): Int {
            return calcCommandEndIndex(channelIndex, TEMPERTURE_OFFSET, DATA_2_BYTE)
        }

        fun getTemperture(byteArray: ByteArray, channelIndex: Int): ByteArray {
            return byteArray.copyOfRange(calcTempertureIndex(channelIndex), calcTempertureEndIndex(channelIndex))
        }


        /**
         * 功能：安时：offset=17  位数：4位   单位：Ah
         */
        fun calcAmperehourIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, AMPEREHOUR_OFFSET)
        }

        fun calcAmperehourEndIndex(channelIndex: Int): Int {
            return calcCommandEndIndex(channelIndex, AMPEREHOUR_OFFSET, DATA_4_BYTE)
        }

        fun getAmperehour(byteArray: ByteArray, channelIndex: Int): ByteArray {
            return byteArray.copyOfRange(calcAmperehourIndex(channelIndex), calcAmperehourEndIndex(channelIndex))
        }

        /**
         * 功能：得到通道号在数组中的索引
         */

        fun getChannelIdIndex(channelIndex: Int): Int {
            return calcCommandIndex(channelIndex, CHANNELID_OFFSET)
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

        fun calcCommandEndIndex(channelIndex: Int, offset: Int, length: Int): Int {
            return channelIndex * 87 + 10 + offset + length
        }
    }


}