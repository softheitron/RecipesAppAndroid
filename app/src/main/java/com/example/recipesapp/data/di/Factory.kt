package com.example.recipesapp.data.di

interface Factory<T> {
    fun create(): T
}