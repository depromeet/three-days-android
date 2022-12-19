package com.depromeet.threedays.mypage.archived_habit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.threedays.core.extensions.gone
import com.depromeet.threedays.core.extensions.invisible
import com.depromeet.threedays.core.extensions.visible
import com.depromeet.threedays.mypage.archived_habit.archived_mate.ArchivedMateUI
import com.depromeet.threedays.mypage.databinding.ItemHabitArchivedBinding
import com.depromeet.threedays.core_design_system.R as CoreDesignSystemResources

class ArchivedHabitViewHolder(
    private val binding: ItemHabitArchivedBinding,
    private val closeMateUIFunction: (ArchivedHabitUI) -> Unit,
    private val openMateUIFunction: (ArchivedHabitUI) -> Unit,
    private val toggleSelectedFunction: (ArchivedHabitUI) -> Unit,
    private val openMateDialogFunction: (ArchivedMateUI) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    /**
     * 데이터 연결해주는 작업
     */
    fun onBind(archivedHabitUI: ArchivedHabitUI) {
        binding.tvTitle.text = archivedHabitUI.title
        if (archivedHabitUI.hasMate()) {
            if (archivedHabitUI.mateOpened) {
                binding.clMateOpened.visible()
                binding.clMateClosed.gone()
            } else {
                binding.clMateOpened.gone()
                binding.clMateClosed.visible()
            }
            with(archivedHabitUI.mate!!) {
                binding.tvMateNickname.text = nickname
                binding.tvMateLevelDescription.text = getLevelDescription()
            }
        } else {
            binding.clMateOpened.gone()
            binding.clMateClosed.gone()
        }
        binding.ivHabitArchivedIconCheck.apply {
            // editable 일때만 체크 아이콘 보이기
            if (archivedHabitUI.editable) {
                visible()
            } else {
                invisible()
            }
            // 선택 여부 따라 색 바꾸기
            setImageResource(
                if (archivedHabitUI.selected) {
                    CoreDesignSystemResources.drawable.ic_check_gray700_circle
                } else {
                    CoreDesignSystemResources.drawable.ic_check_gray400_circle
                }
            )
        }
        initEvent(archivedHabitUI)
    }

    private fun initEvent(archivedHabitUI: ArchivedHabitUI) {
        binding.clMateOpenedButtonClose.setOnClickListener {
            closeMateUIFunction.invoke(archivedHabitUI)
        }
        binding.clMateClosed.setOnClickListener {
            openMateUIFunction.invoke(archivedHabitUI)
        }
        binding.ivHabitArchivedIconCheck.setOnClickListener {
            toggleSelectedFunction.invoke(archivedHabitUI)
        }
        binding.ivMateProfileImage.setOnClickListener {
            archivedHabitUI.mate?.run {
                openMateDialogFunction.invoke(this)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            attachToParent: Boolean,
            closeMateUIFunction: (ArchivedHabitUI) -> Unit,
            openMateUIFunction: (ArchivedHabitUI) -> Unit,
            toggleSelectedFunction: (ArchivedHabitUI) -> Unit,
            openMateDialogFunction: (ArchivedMateUI) -> Unit,
        ) = ArchivedHabitViewHolder(
            binding = ItemHabitArchivedBinding.inflate(
                LayoutInflater.from(parent.context), parent, attachToParent
            ),
            closeMateUIFunction = closeMateUIFunction,
            openMateUIFunction = openMateUIFunction,
            toggleSelectedFunction = toggleSelectedFunction,
            openMateDialogFunction = openMateDialogFunction,
        )
    }
}
