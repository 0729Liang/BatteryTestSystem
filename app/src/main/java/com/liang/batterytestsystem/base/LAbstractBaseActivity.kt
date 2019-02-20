package com.liang.batterytestsystem.base

import android.support.v7.app.AppCompatActivity

/**
 * @author : Amarao
 * CreateAt : 14:42 2019/2/20
 * Describe :
 */
abstract class LAbstractBaseActivity : AppCompatActivity() {
    abstract fun initView()
    abstract fun clicEvent()
}
