package com.mobicule.tatasky_bb.service.model.response

import android.os.Parcelable
import com.bookxpert.assignment.service.model.response.Data
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
/**
 * Created by Poonamchand Sahu 11 Nov 2022
 */
@Parcelize
open class User(

    @field:SerializedName("id"   )
    var id   : String? = null,
    @field:SerializedName("name" )
    var name : String? = null,
    @field:SerializedName("data" )
    var data : Data?   = Data()

): Parcelable

