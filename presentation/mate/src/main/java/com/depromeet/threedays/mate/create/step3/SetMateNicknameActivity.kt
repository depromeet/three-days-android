package com.depromeet.threedays.mate.create.step3

import android.graphics.Rect
import android.os.Bundle
import android.text.InputFilter
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.depromeet.threedays.core.BaseActivity
import com.depromeet.threedays.core.analytics.*
import com.depromeet.threedays.core.util.ThreeDaysToast
import com.depromeet.threedays.core.util.setOnSingleClickListener
import com.depromeet.threedays.mate.R
import com.depromeet.threedays.mate.create.step1.model.HabitUI
import com.depromeet.threedays.mate.databinding.ActivitySetMateNicknameBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class SetMateNicknameActivity : BaseActivity<ActivitySetMateNicknameBinding>(R.layout.activity_set_mate_nickname) {
    private val viewModel by viewModels<SetMateNicknameViewMoodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AnalyticsUtil.event(
            name = ThreeDaysEvent.MateMakingViewed.toString(),
            properties = mapOf(
                MixPanelEvent.ScreenName to "${Screen.MateMaking}3",
            )
        )

        if(intent.hasExtra("clickedHabit")) {
            val clickedHabit = intent.getParcelableExtra<HabitUI>("clickedHabit")
            val mateType = intent.getStringExtra("mateType")
            viewModel.setClickHabit(clickedHabit!!, mateType!!)
        }

        initEditText()
        initEvent()
        setUiStateObserver()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun initEditText() {
        val filterAlphaNumSpace = InputFilter { source, _, _, _, _, _ ->
            val ps = Pattern.compile("^[ㄱ-ㅣ가-힣a-zA-Z0-9]+$")
            if (!ps.matcher(source).matches()) {
                ""
            } else source
        }

        binding.etNickname.filters = arrayOf(filterAlphaNumSpace)
    }

    private fun initEvent() {
        binding.etNickname.doAfterTextChanged {
            viewModel.handleInputText(it.toString())
        }
        binding.ivOut.setOnSingleClickListener {
            finish()
        }
        binding.btnNext.setOnSingleClickListener {
            AnalyticsUtil.event(
                name = ThreeDaysEvent.ButtonClicked.toString(),
                properties = mapOf(
                    MixPanelEvent.ScreenName to "${Screen.MateMaking}3",
                    MixPanelEvent.ButtonType to ButtonType.Next.toString(),
                )
            )
            viewModel.createMate()
            finishAffinity()
        }
    }

    private fun setUiStateObserver() {
        viewModel.error
            .onEach { errorMessage -> ThreeDaysToast().error(this, errorMessage) }
            .launchIn(lifecycleScope)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    setGuideTextVisible(isGuideVisible = it.isGuideVisible)
                    setButtonView(
                        buttonClickable = it.buttonClickable,
                        buttonBackgroundRes = it.buttonBackgroundRes,
                        buttonTextColor = it.buttonTextColor,
                    )
                    setAvailableInputLength(it.inputTextLength)
                    binding.ivIllustrator.setBackgroundResource(it.boxImageResId)
                }
            }
        }
    }

    private fun setGuideTextVisible(isGuideVisible: Boolean) {
        binding.tvGuide.isVisible = isGuideVisible
    }

    private fun setButtonView(
        buttonClickable: Boolean,
        buttonBackgroundRes: Int,
        buttonTextColor: Int
    ) {
        binding.btnNext.apply {
            isClickable = buttonClickable
            setBackgroundResource(buttonBackgroundRes)
            setTextColor(getColor(buttonTextColor))
        }
    }

    private fun setAvailableInputLength(inputTextLength: String) {
        binding.tvCountNicknameLength.text = inputTextLength
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }
}
