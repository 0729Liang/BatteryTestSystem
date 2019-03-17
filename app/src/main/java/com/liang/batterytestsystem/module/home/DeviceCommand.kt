package com.liang.batterytestsystem.module.home

import com.liang.batterytestsystem.module.item.DeviceItemChannelBean
import com.liang.batterytestsystem.module.socket.SendUtils

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
        private val FRAME_HEADER: Byte = 0x7B
        // 总字节数
        private val ALL_HIGH_NUMBER: Byte = 0x00
        private val ALL_LOW_NUMBER: Byte = 0x00
        // 控制命令
        val COMMAND_START_TEST: Byte = 0x8F.toByte()
        val COMMAND_PAUSE_TEST: Byte = 0x65
        val COMMAND_RESUME_TEST: Byte = 0x6A
        val COMMAND_QUERY_TEST: Byte = 0x71
        // 设备号
        val DEVICE_1: Byte = 0x0001
        val DEVICE_2: Byte = 0x0010
        val DEVICE_3: Byte = 0x03
        val DEVICE_4: Byte = 0x04
        // 通道信息
        val CHANNEL_1: Byte = 0x07
        val CHANNEL_2: Byte = 0x02
        val CHANNEL_3: Byte = 0x03
        val CHANNEL_4: Byte = 0x04

        val CHANNEL_5: Byte = 0x05
        val CHANNEL_6: Byte = 0x06
        val CHANNEL_7: Byte = 0x07
        val CHANNEL_8: Byte = 0x08

        val CHANNEL_9: Byte = 0x09
        val CHANNEL_10: Byte = 0x0A
        val CHANNEL_11: Byte = 0x0B
        val CHANNEL_12: Byte = 0x0C


        val CHANNEL_13: Byte = 0x0D
        val CHANNEL_14: Byte = 0x0E
        val CHANNEL_15: Byte = 0x0F
        val CHANNEL_16: Byte = 0x10

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
                    COMMAND_QUERY_TEST, deviceNumber, channel, CHECK_SUM, FRAME_TAIL)
            return sCommand
        }

        // 生成控制命令
        @JvmStatic
        fun createDeviceCommand(deviceId: Byte, channelId: Byte, command: Byte): ByteArray {
            sCommand = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    command, deviceId, 0x00, channelId, CHECK_SUM, FRAME_TAIL)
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

        // 发送命令
        fun sendCommandList(commandList: MutableList<ByteArray>, threadName: String) {
            commandList.forEach {
                //                LLogX.e("控制命令 = " + DigitalTrans.byte2hex(it))
                SendUtils.sendCommand(it, threadName)
            }
        }


    }
}