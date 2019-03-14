package com.liang.batterytestsystem.constant

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
        private val ALL_LOW_NUMBER: Byte = 0x08
        // 控制命令
        private val COMMAND_START_TEST: Byte = 0x8F.toByte()
        private val COMMAND_PAUSE_TEST: Byte = 0x65
        private val COMMAND_RESUME_TEST: Byte = 0x6A
        private val COMMAND_QUERY_TEST: Byte = 0x71
        // 设备号
        val DEVICE_1: Byte = 0x01
        val DEVICE_2: Byte = 0x02
        val DEVICE_3: Byte = 0x03
        val DEVICE_4: Byte = 0x04
        // 通道信息
        val CHANNEL_1: Byte = 0x01
        val CHANNEL_2: Byte = 0x02
        val CHANNEL_3: Byte = 0x03
        val CHANNEL_4: Byte = 0x04
        // 校验和
        private val CHECK_SUM: Byte = 0x00
        // 帧尾
        private val FRAME_TAIL: Byte = 0x7D

        private var command: ByteArray = byteArrayOf(0)

        @JvmStatic
        fun createCommandStartTest(deviceNumber: Byte, channel: Byte): ByteArray {
            command = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    COMMAND_START_TEST, deviceNumber, channel, CHECK_SUM, FRAME_TAIL)
            return command
        }

        @JvmStatic
        fun createCommandPauseTest(deviceNumber: Byte, channel: Byte): ByteArray {
            command = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    COMMAND_PAUSE_TEST, deviceNumber, channel, CHECK_SUM, FRAME_TAIL)
            return command
        }

        @JvmStatic
        fun createCommandResumeTest(deviceNumber: Byte, channel: Byte): ByteArray {
            command = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    COMMAND_RESUME_TEST, deviceNumber, channel, CHECK_SUM, FRAME_TAIL)
            return command
        }

        @JvmStatic
        fun createCommandQueryTest(deviceNumber: Byte, channel: Byte): ByteArray {
            command = byteArrayOf(FRAME_HEADER, ALL_HIGH_NUMBER, ALL_LOW_NUMBER,
                    COMMAND_QUERY_TEST, deviceNumber, channel, CHECK_SUM, FRAME_TAIL)
            return command
        }
    }
}