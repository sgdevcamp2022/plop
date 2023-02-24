package com.plop.plopmessenger.presentation.navigation


import android.os.Build
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.plop.plopmessenger.domain.model.People

@Parcelize
data class PeopleParcelableModel(
    val peopleList: @RawValue List<People>
): Parcelable {
    companion object NavigationType : NavType<PeopleParcelableModel>(isNullableAllowed = false) {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun get(bundle: Bundle, key: String): PeopleParcelableModel? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): PeopleParcelableModel {
            return Gson().fromJson(value, PeopleParcelableModel::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: PeopleParcelableModel) {
            bundle.putParcelable(key, value)
        }
    }
}
