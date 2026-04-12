package com.example.campussafeapplication.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HazardReport(
    val id: String? = null,
    @SerialName("user_id")
    val userId: String,
    @SerialName("hazard_type")
    val hazardType: String, // "Fire", "Flood", "Structural", "Medical", "Security", "Other"
    val description: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val status: String = "Pending", // "Pending", "In Progress", "Resolved"
    val severity: String = "Medium", // "Low", "Medium", "High", "Critical"
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)
