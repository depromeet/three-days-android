package com.depromeet.threedays.domain.usecase.member

import com.depromeet.threedays.domain.repository.AuthRepository
import com.depromeet.threedays.domain.repository.MemberRepository
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke() {
        memberRepository.withdraw()
        authRepository.removeTokensFromLocal()
        // TODO: notification permission 삭제
    }
}
