package com.bookxpert.assignment.service.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data (
  @field:SerializedName("Generation")
  val generation: String?= null,
  @field:SerializedName("Price")
  val price: String?= null,
  @field:SerializedName("color")
  var color    : String? = null,
  @field:SerializedName("capacity")
  var capacity : String? = null
): Parcelable