package ru.alexp0111.onigoing.ui.utils

import androidx.core.text.isDigitsOnly

object SeriesInputVerifier {

    const val INCORRECT_SERIES_ET_INPUT_CODE = -1
    private const val MAX_SERIES_LENGTH = 8

    fun verifiedInput(input: String): Int {
        return when {
            input == "0" || isCorrectDigit(input) -> input.toInt()
            else -> INCORRECT_SERIES_ET_INPUT_CODE
        }
    }

    private fun isCorrectDigit(input: String): Boolean {
        return input.isNotEmpty()
                && input.length <= MAX_SERIES_LENGTH
                && input.isDigitsOnly()
                && !input.startsWith('0')
    }
}