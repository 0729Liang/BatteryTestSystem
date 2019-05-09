package com.liang.batterytestsystem.module.device;

import android.support.annotation.NonNull;

/**
 * @author : Amarao
 * CreateAt : 17:11 2019/3/4
 * Describe : 无参数
 */
public interface Customer<T> {
    void accept(@NonNull T t);
}
