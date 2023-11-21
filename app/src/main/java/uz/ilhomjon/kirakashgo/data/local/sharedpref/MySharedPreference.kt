package uz.ilhomjon.kirakashgo.data.local.sharedpref

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse

object MySharedPreference {

    private const val NAME = "PhoneLists"
    private const val MODE = Context.MODE_PRIVATE

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var token: GetDriveTokenResponse
        get() = jsonToToken(preferences.getString("token", "{}")!!)
        set(value) = preferences.edit {
            it.putString("token", tokenToJson(value))
        }


    private fun tokenToJson(getDriveTokenResponse: GetDriveTokenResponse): String {
        return Gson().toJson(getDriveTokenResponse)
    }

    private fun jsonToToken(str: String): GetDriveTokenResponse {
        val gson = Gson()
        val type = object : TypeToken<GetDriveTokenResponse>() {}.type
        return gson.fromJson(str, type)
    }
}