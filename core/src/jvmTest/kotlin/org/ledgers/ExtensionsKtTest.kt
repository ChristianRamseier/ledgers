package org.ledgers

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ExtensionsKtTest {

    @Test
    fun givenAnEmptyList_whenReplacingAtIndex3_theListIsCorrectlyExtended() {
        val list = emptyList<String>()
        val updated = list.replaceAtIndex(3, "42", "")
        assertThat(updated).containsExactly("", "", "", "42")
    }

    @Test
    fun givenAList_whenReplacingAtIndex3_theElementIsReplaced() {
        val list = listOf("1", "2", "3")
        val updated = list.replaceAtIndex(2, "42", "")
        assertThat(updated).containsExactly("1", "2", "42")
    }

}
