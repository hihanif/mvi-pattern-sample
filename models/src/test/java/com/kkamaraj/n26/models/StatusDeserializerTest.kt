package com.kkamaraj.n26.models

import com.google.gson.JsonNull
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import java.lang.IllegalArgumentException

class StatusDeserializerTest {
    private lateinit var statusDeserializer : StatusDeserializer

    @Before
    fun setUp() {
        statusDeserializer = StatusDeserializer()
    }

    @Test
    fun `verify that status is correct enum`() {
        "ok" isParsedAs Status.OK
        "not_found" isParsedAs Status.NOT_FOUND
    }

    @Test
    fun `verify invalid value is not parsed`() {
        try {
            "invalid" isParsedAs Status.OK
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("Unknown Status [invalid]", e.message)

        }
    }

    @Test
    fun `verify that null value is throws exception`() {
        try {
            (null as String?) isParsedAs Status.OK
            fail()
        } catch (e: JsonParseException) {
            assertEquals("Status is expected to be a non-null primitive", e.message)
        }
    }

    private infix fun String?.isParsedAs(statusEnum: Status) {
        val jsonElement = if (this == null) JsonNull.INSTANCE else JsonPrimitive(this)
        val status =  statusDeserializer.deserialize(jsonElement, Status::class.java, null)
        assertThat(status).isEqualTo(statusEnum)
    }
}
