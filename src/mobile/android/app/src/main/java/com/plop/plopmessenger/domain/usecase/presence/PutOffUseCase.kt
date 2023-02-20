package com.plop.plopmessenger.domain.usecase.presence

import android.util.Log
import com.plop.plopmessenger.domain.repository.PresenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PutOffUseCase @Inject constructor(
    private val presenceRepository: PresenceRepository
) {
    operator fun invoke() {
        try {
            CoroutineScope(Dispatchers.Default).launch {
                val response = presenceRepository.setOff()
                if(response.isSuccessful) {
                    Log.d("PutOffUseCase", "종료 성공")
                } else {
                    Log.d("PutOffUseCase", "종료 실패")
                }
            }
        } catch (e: Exception) {
            Log.d("PutOffUseCase", e.message.toString())
        }
    }
}