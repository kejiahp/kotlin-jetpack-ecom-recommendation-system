package com.example.mobile.core.utilites

sealed class ResourceState<T> {
    class Pending<T>: ResourceState<T>()
    class Loading<T>: ResourceState<T>()
    data class Success<T>(val data: T) : ResourceState<T>()
    data class Error<T>(val error: Any): ResourceState<T>()
}