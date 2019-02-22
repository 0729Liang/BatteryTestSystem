package com.liang.batterytestsystem.base

import com.liang.liangutils.msg.Exts

/**
 * @author : Amarao
 * CreateAt : 14:42 2019/2/20
 * Describe :
 */
abstract class LAbstractBaseActivity : LBaseActivity() {
    abstract fun initData()/*数据初始化*/
    abstract fun initView()/*view初始化*/
    abstract fun clicEvent()/*点击事件*/

    init {
        Exts.registerEvent(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Exts.unRegisterEvent(this)
    }
}
