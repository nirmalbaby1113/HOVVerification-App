package com.nirmal.baby.hovverification.features.hovVerification.entity

data class FaceResponse(
    val request_id: String,
    val face_num: Int,
    val faces: List<Face>
)

data class Face(
    val face_rectangle: FaceRectangle,
    val attributes: Attributes
)

data class FaceRectangle(val top: Int, val left: Int, val width: Int, val height: Int)
data class Attributes(val gender: Gender, val age: Age)
data class Gender(val value: String)
data class Age(val value: Int)

