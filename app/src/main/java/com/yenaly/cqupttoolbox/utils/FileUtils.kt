package com.yenaly.cqupttoolbox.utils

import java.io.File

/**
 * Utils for File.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/17 017 10:56
 * @Description : Description...
 */
object FileUtils {

    fun getFolderSizeByte(dir: File?): Long {
        var size = 0L
        val files = dir?.listFiles()
        files?.forEach { file -> size += if (file.isDirectory) getFolderSizeByte(file) else file.length() }
        return size
    }
}