package com.example.quizzy.model_class

import android.os.Parcelable
import com.example.quizzy.R
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class DashboardResponse(
    val student: Student? = null,
    val todaySummary: TodaySummary? = null,
    val weeklyOverview: WeeklyOverview? = null
) : Parcelable

@Parcelize
@Serializable
data class Student(
    val name: String? = null,
    @SerializedName("class")
    val classOfStudent: String? = null,
    val availability: Availability? = null,
    val quiz: Quiz? = null,
    val accuracy: Accuracy? = null
) : Parcelable

@Parcelize
@Serializable
data class Availability(
    val status: String? = null
) : Parcelable

@Parcelize
@Serializable
data class Quiz(
    val attempts: Int? = null
) : Parcelable

@Parcelize
@Serializable
data class Accuracy(
    val current: String? = null
) : Parcelable

@Parcelize
@Serializable
data class TodaySummary(
    val mood: String? = null,
    val description: String? = null,
    val recommendedVideo: RecommendedVideo? = null,
    val characterImage: String? = null
) : Parcelable

@Parcelize
@Serializable
data class RecommendedVideo(
    val title: String? = null,
    val actionText: String? = null
) : Parcelable

@Parcelize
@Serializable
data class WeeklyOverview(
    val quizStreak: List<QuizStreak>? = null,
    val overallAccuracy: OverallAccuracy? = null,
    val performanceByTopic: List<PerformanceTopic>? = null
) : Parcelable

@Parcelize
@Serializable
data class QuizStreak(
    val day: String? = null,
    val status: StreakStatus? = null
) : Parcelable
@Parcelize
enum class StreakStatus (val value: String) : Parcelable {
    COMPLETED("done"),
    PENDING("pending") ;
    companion object {
        val map = entries.associateBy(StreakStatus::value)
        fun fromValue(value: String) = map[value]
    }
}


@Parcelize
@Serializable
data class OverallAccuracy(
    val percentage: Int? = null,
    val label: String? = null
) : Parcelable

@Parcelize
@Serializable
data class PerformanceTopic(
    val topic: String? = null,
    val trend: PerformanceTrend? = null
) : Parcelable

@Parcelize
enum class PerformanceTrend (val value: String,val icon : Int) : Parcelable {
    UP("up", R.drawable.performance_up),
    DOWN("down", R.drawable.performance_down) ;
    companion object {
        val map = entries.associateBy(PerformanceTrend::value)
        fun fromValue(value: String) = map[value]
    }
}