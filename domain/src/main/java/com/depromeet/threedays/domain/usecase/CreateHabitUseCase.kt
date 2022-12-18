package com.depromeet.threedays.domain.usecase

import com.depromeet.threedays.domain.entity.habit.CreateHabit
import com.depromeet.threedays.domain.repository.HabitRepository
import javax.inject.Inject

class CreateHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habit: CreateHabit) {
        return habitRepository.createHabit(habit)
    }
}
