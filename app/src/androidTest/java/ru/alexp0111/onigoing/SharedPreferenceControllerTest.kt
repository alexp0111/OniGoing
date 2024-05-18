package ru.alexp0111.onigoing

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import ru.alexp0111.onigoing.utils.SharedPreferenceController


private const val SHARED_PREFS_TEST_FILE_NAME = "test_shared_pref"

@RunWith(AndroidJUnit4::class)
class SharedPreferenceControllerTest {

    private val sharedPreferenceController = SharedPreferenceController(
        InstrumentationRegistry.getInstrumentation().targetContext.getSharedPreferences(
            SHARED_PREFS_TEST_FILE_NAME,
            Context.MODE_PRIVATE
        )
    )

    @After
    fun clearSharedPreference() {
        val historyList = sharedPreferenceController.getSearchHistory().historyList
        repeat(historyList.size) {
            sharedPreferenceController.removeHistoryElementWithIndex(0)
        }
    }


    @Test
    fun getEmptyHistory() {
        Assert.assertTrue(sharedPreferenceController.getSearchHistory().historyList.isEmpty())
    }

    @Test
    fun insertOneElement() {
        val testString = "ONE PIECE"
        sharedPreferenceController.insertNewHistoryElement(testString)
        Assert.assertTrue(sharedPreferenceController.getSearchHistory().historyList.size == 1)
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.first(),
            testString
        )
    }

    @Test
    fun insertTooMuchElements() {
        val testString = "ONE PIECE"
        sharedPreferenceController.insertNewHistoryElement(testString + "1")
        sharedPreferenceController.insertNewHistoryElement(testString + "2")
        sharedPreferenceController.insertNewHistoryElement(testString + "3")
        sharedPreferenceController.insertNewHistoryElement(testString + "4")
        sharedPreferenceController.insertNewHistoryElement(testString + "5")
        sharedPreferenceController.insertNewHistoryElement(testString + "6")
        sharedPreferenceController.insertNewHistoryElement(testString + "7")
        Assert.assertTrue(sharedPreferenceController.getSearchHistory().historyList.size == 5)
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.first(),
            testString + "7"
        )
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.last(),
            testString + "3"
        )
    }

    @Test
    fun insertOneElementFewTimes() {
        val testString = "ONE PIECE"
        sharedPreferenceController.insertNewHistoryElement(testString + "1")
        sharedPreferenceController.insertNewHistoryElement(testString)
        sharedPreferenceController.insertNewHistoryElement(testString)
        sharedPreferenceController.insertNewHistoryElement(testString)
        Assert.assertTrue(sharedPreferenceController.getSearchHistory().historyList.size == 2)
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.first(),
            testString
        )
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.last(),
            testString + "1"
        )
    }

    @Test
    fun removeFirstElement() {
        val testString = "ONE PIECE"
        sharedPreferenceController.insertNewHistoryElement(testString + "4")
        sharedPreferenceController.insertNewHistoryElement(testString + "3")
        sharedPreferenceController.insertNewHistoryElement(testString + "2")
        sharedPreferenceController.insertNewHistoryElement(testString + "1")

        sharedPreferenceController.removeHistoryElementWithIndex(0)

        Assert.assertTrue(sharedPreferenceController.getSearchHistory().historyList.size == 3)
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.first(),
            testString + "2"
        )
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.last(),
            testString + "4"
        )
    }

    @Test
    fun removeLastElement() {
        val testString = "ONE PIECE"
        sharedPreferenceController.insertNewHistoryElement(testString + "4")
        sharedPreferenceController.insertNewHistoryElement(testString + "3")
        sharedPreferenceController.insertNewHistoryElement(testString + "2")
        sharedPreferenceController.insertNewHistoryElement(testString + "1")

        sharedPreferenceController.removeHistoryElementWithIndex(3)

        Assert.assertTrue(sharedPreferenceController.getSearchHistory().historyList.size == 3)
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.first(),
            testString + "1"
        )
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.last(),
            testString + "3"
        )
    }

    @Test
    fun removeMiddleElements() {
        val testString = "ONE PIECE"
        sharedPreferenceController.insertNewHistoryElement(testString + "4")
        sharedPreferenceController.insertNewHistoryElement(testString + "3")
        sharedPreferenceController.insertNewHistoryElement(testString + "2")
        sharedPreferenceController.insertNewHistoryElement(testString + "1")

        sharedPreferenceController.removeHistoryElementWithIndex(1)
        sharedPreferenceController.removeHistoryElementWithIndex(1)

        Assert.assertTrue(sharedPreferenceController.getSearchHistory().historyList.size == 2)
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.first(),
            testString + "1"
        )
        Assert.assertEquals(
            sharedPreferenceController.getSearchHistory().historyList.last(),
            testString + "4"
        )
    }

    @Test
    fun removeAllElements() {
        val testString = "ONE PIECE"
        sharedPreferenceController.insertNewHistoryElement(testString + "4")
        sharedPreferenceController.insertNewHistoryElement(testString + "3")
        sharedPreferenceController.insertNewHistoryElement(testString + "2")
        sharedPreferenceController.insertNewHistoryElement(testString + "1")

        repeat(4) {
            sharedPreferenceController.removeHistoryElementWithIndex(0)
        }

        Assert.assertTrue(sharedPreferenceController.getSearchHistory().historyList.isEmpty())
    }

}