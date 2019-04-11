package com.kkamaraj.n26.models

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class StatusDeserializer: JsonDeserializer<Status> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Status {
        if (json != null && !json.isJsonNull && json.isJsonPrimitive) {
            return Status.deserialize(json.asString)
        }
        throw JsonParseException("Status is expected to be a non-null primitive")
    }
}
