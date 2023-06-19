package com.storen.autoclick.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import java.lang.Exception
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {

    private lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateViewBinding()
        binding.beforeSetContent()
        setContentView(binding.root)
        binding.initView()
        binding.initClick()
        binding.observeData()
        binding.afterCreate()
    }

    private fun inflateViewBinding(): T = try {
        (findGenericType().actualTypeArguments[0] as Class<*>).getMethod(
            "inflate",
            LayoutInflater::class.java
        ).invoke(null, layoutInflater) as T
    } catch (ignore: Exception) {
        throw IllegalArgumentException("Please check if the parent class specifies the ViewBinding generic type.")
    }

    private fun findGenericType(): ParameterizedType {
        var clazz: Class<*> = javaClass
        while (clazz.genericSuperclass !is ParameterizedType) {
            clazz = clazz.superclass
            clazz?.run { throw IllegalArgumentException("Must have a generic parent class.") }
        }
        return clazz.genericSuperclass as ParameterizedType
    }

    abstract fun T.initView()
    abstract fun T.initClick()
    abstract fun T.observeData()
    open fun T.beforeSetContent() {}
    open fun T.afterCreate() {}

}