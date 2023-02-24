package com.plop.plopmessenger.domain.usecase.fcm

import android.util.Log
import com.plop.plopmessenger.data.dto.request.fcm.FcmRequest
import com.plop.plopmessenger.domain.repository.FCMRepository
import com.plop.plopmessenger.domain.util.Resource
import javax.inject.Inject

class FCMUseCase @Inject constructor(
    private val fcmRepository: FCMRepository
) {
    suspend operator fun invoke(tokenId: String): Resource<Boolean> {
        try {
            val response = fcmRepository.postFCM(FcmRequest(tokenId))

            if(response.isSuccessful) {
                Log.d("FCMUseCase", "성공 ${tokenId}")
                return Resource.Success(true)
            }else {
                Log.d("FCMUseCase", "실패 ${response.message()}")
                return Resource.Error(response.message())
            }
        }catch (e: Exception){
            Log.d("FCMUseCase", "실패 ${e.message.toString()}")
            return Resource.Error(e.message.toString())
        }
    }
}