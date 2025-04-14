package com.example.mobile.product_cart_order.preference

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProdCartOrderSharedPreferenceViewModel @Inject constructor(private val prodCartOrderSharedPreferenceService: ProdCartOrderSharedPreferenceService) :
    ViewModel() {
    companion object {
        const val TAG = "ProdCartOrderSharedPreferenceViewModel"
    }


    fun addToRecentlyViewed(productId: String) {
        val recentViewList =
            prodCartOrderSharedPreferenceService.getRecentViewFromPreference().toMutableList()
        if (recentViewList.size < 3) {
            recentViewList.add(productId)
            prodCartOrderSharedPreferenceService.saveRecentViewToPreference(recentViewList)
        }
    }
}