package dev.polek.episodetracker

import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import dev.polek.episodetracker.utils.findChildOfType
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FindChildOfTypeTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val layout = FrameLayout(context)

    @Test
    fun findChildOfType_finds_direct_child() {
        val textView = TextView(context)
        layout.addView(textView)

        assertThat(layout.findChildOfType(TextView::class.java)).isEqualTo(textView)
    }

    @Test
    fun findChildOfType_returns_null_if_there_is_no_child_of_specified_type() {
        val textView = TextView(context)
        layout.addView(textView)

        assertThat(layout.findChildOfType(Button::class.java)).isNull()
    }

    @Test
    fun findChildOfType_returns_null_for_empty_ViewGroup() {
        assertThat(layout.findChildOfType(Button::class.java)).isNull()
    }

    @Test
    fun findChildOfType_finds_indirect_child() {
        val childLayout = LinearLayout(context)
        val textView = TextView(context)
        childLayout.addView(textView)
        layout.addView(childLayout)

        assertThat(layout.findChildOfType(TextView::class.java)).isEqualTo(textView)
    }

    @Test
    fun findChildOfType_finds_ViewGroup() {
        val linearLayout = LinearLayout(context)
        val frameLayout = FrameLayout(context)
        linearLayout.addView(frameLayout)
        layout.addView(linearLayout)

        assertThat(layout.findChildOfType(FrameLayout::class.java)).isEqualTo(frameLayout)
    }

    @Test
    fun findChildOfType_finds_supertype() {
        val textView = TextView(context)
        layout.addView(textView)

        assertThat(layout.findChildOfType(View::class.java)).isEqualTo(textView)
    }

    @Test
    fun findChildOfType_does_not_return_itself() {
        val layout = FrameLayout(context)
        layout.addView(TextView(context))

        assertThat(layout.findChildOfType(FrameLayout::class.java)).isNull()
    }
}
