package com.example.neoradar.network

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseNeoJsonResult(jsonResult: JSONObject): ArrayList<NeoDTO> {
    val neoJson = jsonResult.getJSONObject("near_earth_objects")

    val neoList = ArrayList<NeoDTO>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = neoJson.optJSONArray(formattedDate)

        for (i in 0 until dateAsteroidJsonArray!!.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                .getJSONObject("kilometers").getDouble("estimated_diameter_max")

            val closeApproachData = asteroidJson
                .getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                .getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")

            val neo = NeoDTO(
                id, codename, formattedDate, absoluteMagnitude,
                estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous
            )
            neoList.add(neo)
        }
    }
    return neoList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..ApiConstants.DEFAULT_END_DATE_DAYS) {
        formattedDateList.add(calculateCurrentDate(calendar))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
    return formattedDateList
}

/**
 * Calculating the date is needed in a couple places in the app, such as setting the start date for
 * the NeoWS api call.  Figured it'd be easier to put it in a function here for reuse.
 */
fun calculateCurrentDate(calendar: Calendar): String {
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(ApiConstants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(currentTime)
}

object ApiConstants {
    const val DEFAULT_END_DATE_DAYS = 7
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"
}