package com.depromeet.threedays.home

import android.animation.Animator
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.depromeet.threedays.core.BaseActivity
import com.depromeet.threedays.history.HistoryFragment
import com.depromeet.threedays.home.databinding.ActivityMainBinding
import com.depromeet.threedays.home.home.HomeFragment
import com.depromeet.threedays.mate.MateFragment
import com.depromeet.threedays.mypage.MyPageFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigationBar()
        initEvent()
    }

    private fun initNavigationBar() {
        binding.bnvMain.run {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.homeFragment -> {
                        changeFragment(HomeFragment())
                    }
                    R.id.historyFragment -> {
                        changeFragment(HistoryFragment())
                    }
                    R.id.mateFragment -> {
                        changeFragment(MateFragment())
                    }
                    R.id.mypageFragment -> {
                        changeFragment(MyPageFragment())
                    }
                }
                true
            }
            selectedItemId = R.id.homeFragment
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.flMain.id, fragment).commit()
    }

    private fun initEvent() {
        binding.lottieClap.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
                binding.congratulationAnimationGroup.isVisible = true
            }

            override fun onAnimationEnd(p0: Animator) {
                binding.congratulationAnimationGroup.isVisible = false
            }

            override fun onAnimationCancel(p0: Animator) {

            }

            override fun onAnimationRepeat(p0: Animator) {

            }
        })
    }

    fun startCongratulateThirdClapAnimation() {
        binding.lottieClap.playAnimation()
    }
}
