package com.example.jobflick.features.jobseeker.discover.domain
import androidx.annotation.DrawableRes

data class JobPosting(
    val company: String,
    @DrawableRes val logoRes: Int,
    val location: String,
    val title: String,
    val scorePct: Int,
    val workType: String,
    val skills: String,
    val level: String,
    val salary: String,
    val about: String
)
