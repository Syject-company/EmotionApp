package com.syject.eqally.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONObject

class GSONUtils {

    companion object {

        inline fun <reified T> fromJsonToObject(jsonString: String?): T? {
            jsonString?.let { return Gson().fromJson(it, T::class.java) }
            return null
        }

        inline fun <reified T, reified V> fromJsonArrayToMap(jsonString: String): HashMap<T, V> =
            Gson().fromJson(
                jsonString,
                TypeToken.getParameterized(HashMap::class.java, T::class.java, V::class.java).type
            )

        inline fun <reified T> fromJsonArrayToObjectList(jsonArrayString: String?): MutableList<T> {
            jsonArrayString?.let {
                return Gson().fromJson(
                    jsonArrayString,
                    TypeToken.getParameterized(MutableList::class.java, T::class.java).type
                )
            }
            return emptyList<T>().toMutableList()
        }

        fun objectToJsonString(value: Any) = Gson().toJson(value)!!
    }

    operator fun JSONArray.iterator(): Iterator<JSONObject> =
        (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()
}