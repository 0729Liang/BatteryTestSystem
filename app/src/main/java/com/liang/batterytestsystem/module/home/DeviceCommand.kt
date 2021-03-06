package com.liang.batterytestsystem.module.home

import android.graphics.Color
import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.module.socket.SendUtils
import com.liang.batterytestsystem.utils.DigitalTrans
import com.liang.liangutils.utils.LLogX
import kotlin.experimental.or

/**
 * @author : Amarao
 * CreateAt : 14:47 2019/3/14
 * Describe :
 *
 */
class DeviceCommand {

    // 命令格式（16位）： 帧头+高位节数+低位字节数+控制命令+设备号+通道号+校验和+帧尾
    companion object {
        // 帧头
        val FRAME_HEADER: Byte = 0x7B
        // 总字节数
        val ALL_HIGH_NUMBER: Byte = 0x00
        val ALL_LOW_NUMBER: Byte = 0x00
        // 控制命令
        val COMMAND_START_TEST: Byte = 0x8F.toByte() // 启动
        val COMMAND_PAUSE_TEST: Byte = 0x65 // 暂停
        val COMMAND_RESUME_TEST: Byte = 0x6A // 恢复
        val COMMAND_STOP_TEST: Byte = 0x60 // 停止

        val COMMAND_QUERY_DATA_TEST: Byte = 0x71 // 查询数据
        val COMMAND_QUERY_CHANNEL_STATUS_TEST: Byte = 0x80.toByte() // 查询通道状态

        // 设备号
        val DEVICE_1: Byte = 0x01
        val DEVICE_2: Byte = 0x02
        val DEVICE_3: Byte = 0x03
        val DEVICE_4: Byte = 0x04
        // 通道信息
        val CHANNEL_ARRAY = byteArrayOf(
                0x1, 0x2, 0x4, 0x8,
                0x10, 0x20, 0x40, 0x80.toByte(),
                0x100.toByte(), 0x200.toByte(), 0x400.toByte(), 0x800.toByte(),
                0x1000.toByte(), 0x2000.toByte(), 0x4000.toByte(), 0x8000.toByte()
        )

        val CHANNEL_1: Byte = 0x1
        val CHANNEL_2: Byte = 0x2
        val CHANNEL_3: Byte = 0x4
        val CHANNEL_4: Byte = 0x8

        val CHANNEL_5: Byte = 0x10
        val CHANNEL_6: Byte = 0x20
        val CHANNEL_7: Byte = 0x40
        val CHANNEL_8: Byte = 0x80.toByte()

        enum class CHANNEL(val channelId: Byte, val index: Byte) {
            CHANNEL_1(0x1, 0x01),
            CHANNEL_2(0x2, 0x02),
            CHANNEL_3(0x4, 0x03),
            CHANNEL_4(0x8, 0x04),

            CHANNEL_5(0x10, 0x05),
            CHANNEL_6(0x20, 0x06),
            CHANNEL_7(0x40, 0x07),
            CHANNEL_8(0x80.toByte(), 0x08)

        }


        val CHANNEL_9: Byte = 0x10
        val CHANNEL_10: Byte = 0x20
        val CHANNEL_11: Byte = 0x40
        val CHANNEL_12: Byte = 0x80.toByte()

        val CHANNEL_13: Byte = 0x1000.toByte()
        val CHANNEL_14: Byte = 0x20
        val CHANNEL_15: Byte = 0x40
        val CHANNEL_16: Byte = 0x80.toByte()


        // 校验和
        private val CHECK_SUM: Byte = 0x00
        // 帧尾
        private val FRAME_TAIL: Byte = 0x7D

        private var sCommand: ByteArray = byteArrayOf(0)

        @JvmStatic
        fun createCommandStartTest(deviceNumber: Byte, channel: Byte): ByteArray {
            sCommand = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    COMMAND_START_TEST, deviceNumber, channel, CHECK_SUM, FRAME_TAIL)
            return sCommand
        }

        @JvmStatic
        fun createCommandPauseTest(deviceNumber: Byte, channel: Byte): ByteArray {
            sCommand = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    COMMAND_PAUSE_TEST, deviceNumber, channel, CHECK_SUM, FRAME_TAIL)
            return sCommand
        }

        @JvmStatic
        fun createCommandResumeTest(deviceNumber: Byte, channel: Byte): ByteArray {
            sCommand = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    COMMAND_RESUME_TEST, deviceNumber, channel, CHECK_SUM, FRAME_TAIL)
            return sCommand
        }

        @JvmStatic
        fun createCommandQueryTest(deviceNumber: Byte, channel: Byte): ByteArray {
            sCommand = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    COMMAND_QUERY_DATA_TEST, deviceNumber, channel, CHECK_SUM, FRAME_TAIL)
            return sCommand
        }

        // 生成控制命令
        @JvmStatic
        fun createDeviceCommand(deviceId: Byte, channelId: Byte, command: Byte): ByteArray {
            sCommand = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    command, deviceId, 0x00, 0x00, 0x00, channelId, CHECK_SUM, FRAME_TAIL)
            return sCommand
        }


        // 根据测试通道列表创建一个命令列表
        @JvmStatic
        fun createDeviceTestCommandList(channelIdList: MutableList<DeviceItemChannelBean>, command: Byte): MutableList<ByteArray> {
            val commandList: MutableList<ByteArray> = arrayListOf()
            channelIdList.forEach {
                commandList.add(DeviceCommand.createDeviceCommand(it.deviceId, it.channelId, command))
            }
            return commandList
        }


        /**
         * 生成命令列表
         * 部分命令：同一设备通道号按位或
         *
         * @param deviceItemBeanList 选中的设置
         * @param command 命令
         * @param compose 是否合成（按位或）通道
         * */
        @JvmStatic
        fun createDeviceTestComposeCommandList(deviceItemBeanList: MutableList<DeviceItemBean>, command: Byte, compose: Boolean): MutableList<ByteArray> {

            // 命令列表
            val commandList: MutableList<ByteArray> = arrayListOf()

            deviceItemBeanList.forEach {

                // 合成通道，根据compose标记来判断，是否需要将通道号进行逻辑或
                var channel: Byte = 0x00
                if (compose) {
                    // 对选中的通道进行逻辑或操作
                    it.channeChooselList.forEach {
                        channel = channel.or(it.channelId)
                    }
                }

                // 生成命令，并添加到命令列表
                commandList.add(DeviceCommand.createDeviceCommand(it.deviceId, channel, command))
            }

            return commandList
        }

        // 发送命令列
        fun sendCommandList(commandList: MutableList<ByteArray>, threadName: String) {
            commandList.forEach {
                SendUtils.sendCommand(it, threadName)
            }
        }

        // 发送命令
        fun sendCommandList(commandByteArray: ByteArray, threadName: String) {
            //                LLogX.e("控制命令 = " + DigitalTrans.byte2hex(it))
            SendUtils.sendCommand(commandByteArray, threadName)
        }


    }
}