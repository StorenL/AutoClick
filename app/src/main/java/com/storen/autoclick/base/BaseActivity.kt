package com.storen.autoclick.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.Exception
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateViewBinding()
        binding.beforeSetContent()
        setContentView(binding.root)
        binding.beforeInit()
        binding.initView()
        binding.initClick()
        binding.observeData()
        binding.afterCreate()
    }

    @Suppress("UNCHECKED_CAST")
    private fun inflateViewBinding(): VB = try {
        (findGenericType().actualTypeArguments[0] as Class<VB>).getMethod(
            "inflate",
            LayoutInflater::class.java
        ).invoke(null, layoutInflater) as VB
    } catch (ignore: Exception) {
        throw IllegalArgumentException("Please check if the parent class specifies the ViewBinding generic type.")
    }

    private fun findGenericType(): ParameterizedType {
        var clazz: Class<*> = javaClass
        while (clazz.genericSuperclass !is ParameterizedType) {
            clazz = clazz.superclass ?: throw IllegalArgumentException("Must have a generic parent class.")
        }
        return clazz.genericSuperclass as ParameterizedType
    }

    abstract fun VB.initView()
    abstract fun VB.initClick()
    abstract fun VB.observeData()
    open fun VB.beforeSetContent() {}
    open fun VB.afterCreate() {}
    open fun VB.beforeInit() {}

}