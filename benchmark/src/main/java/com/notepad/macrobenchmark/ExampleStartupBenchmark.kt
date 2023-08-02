package com.notepad.macrobenchmark

import androidx.benchmark.macro.*
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

//    @Test
//    fun startUpCompilationModeNone() = startup(CompilationMode.None())
//
//    @Test
//    fun startUpCompilationModePartial() = startup(CompilationMode.Partial())

    @Test
    fun scrollCompilationModeNone() = scrollAndNavigate(CompilationMode.None())

    @Test
    fun scrollCompilationModePartial() = scrollAndNavigate(CompilationMode.Partial())

    fun startup(mode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.jobik.shkiper",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = mode
    ) {
        pressHome()
        startActivityAndWait()
    }

    fun scrollAndNavigate(mode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.jobik.shkiper",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = mode,
        setupBlock = {
            pressHome()
            startActivityAndWait()
        }
    ) {
        finishOnboarding()
        createNote()
        scrollNoteListJourney()
    }
}

fun MacrobenchmarkScope.finishOnboarding() {
    val createNoteButton = device.findObject(By.res("button_next"))
    repeat(3) {
        try {
            Thread.sleep(1_000)
            createNoteButton.click()
        } catch (e: Exception) {
            return@repeat
        }
    }
}

fun MacrobenchmarkScope.createNote() {
    device.wait(Until.hasObject(By.res("create_note_button")), 5_000)
    Thread.sleep(1_000)

    val createNoteButton = device.findObject(By.res("create_note_button"))
    createNoteButton.click()

    device.wait(Until.hasObject(By.res("note_header_input")), 5_000)
    val noteHeaderInput = device.findObject(By.res("note_header_input"))
    val noteBodyInput = device.findObject(By.res("note_body_input"))
    noteHeaderInput.text = generateSentence(Random.nextInt(6, 67))
    noteBodyInput.text = generateSentence(Random.nextInt(6, 200))

    device.pressBack()
}

fun MacrobenchmarkScope.scrollNoteListJourney() {
    device.wait(Until.hasObject(By.res("notes_list")), 5_000)
    Thread.sleep(1_000)
    val noteList = device.findObject(By.res("notes_list"))
    // Set gesture margin to avoid triggering gesture navigation.
    noteList.setGestureMargin(device.displayWidth / 5)
    noteList.fling(Direction.DOWN)
    device.waitForIdle()
    noteList.fling(Direction.UP)
    device.waitForIdle()
}

fun generateSentence(length: Int): String {
    if (length <= 0) return ""

    val words = listOf("The", "quick", "brown", "fox", "jumps", "over", "the", "lazy", "dog")
    val sentence = StringBuilder()

    for (i in 1..length) {
        val randomIndex = (words.indices).random()
        sentence.append(words[randomIndex]).append(" ")
    }

    sentence.trimEnd()
    sentence.setCharAt(0, sentence[0].uppercaseChar())
    sentence.append(".")

    return sentence.toString()
}