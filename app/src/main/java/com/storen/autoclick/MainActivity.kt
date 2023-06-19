package com.storen.autoclick

import com.storen.autoclick.base.BaseActivity
import com.storen.autoclick.databinding.ActivityMainBinding
import com.storen.autoclick.util.openAccessibilitySetting
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun ActivityMainBinding.initView() {
        root.setOnClickListener {
            openAccessibilitySetting()
        }
    }

    override fun ActivityMainBinding.initClick() {
    }

    override fun ActivityMainBinding.observeData() {
    }

}