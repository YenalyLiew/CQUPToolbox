package com.yenaly.cqupttoolbox.logic.model

/**
 * The model of sports resume.
 *
 * @ProjectName : CQUPTDox
 * @Author : Yenaly Liew
 * @Time : 2022/03/05 005 13:27
 * @Description : Description...
 */
data class StudentSportsResumeModel(
    val code: String,
    val total: Int,
    val rows: List<Rows>
) {
    data class Rows(
        val termShouldSportCount: Int,
        val termShouldRunCount: Int,
        val termShouldOtherCount: Int,
        val currentShouldSportCount: Int,
        val currentShouldRunCount: Int,
        val currentShouldOtherCount: Int,
        val sportCount: Int,
        val examSportCount: Int,
        val runCount: Int,
        val runDura: Int,
        val runMile: Int,
        val otherCount: Int,
        val otherDura: Int,
        val extSportCount: Int,
        val extRunCount: Int,
        val extRunDura: Int,
        val extRunMile: Int,
        val extOtherCount: Int,
        val extOtherDura: Int,
        val runProgress: Float,
        val otherProgress: Float,
        val progress: Float,
        val termProgress: Float,
        val isExam: String,
        val weekly: Int
    )
}
