package com.plop.plopmessenger.domain.usecase.presence

import android.util.Log
import com.plop.plopmessenger.domain.repository.PresenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PutOnUseCase @Inject constructor(
    private val presenceRepository: PresenceRepository
) {
    operator fun invoke() {
        try {
            CoroutineScope(Dispatchers.Default).launch {
                val response = presenceRepository.setOn()
                if(response.isSuccessful) {
                    Log.d("PutOnUseCase", "시작 성공")
                } else {
                    Log.d("PutOnUseCase", "시작 실패")
                }
            }
        } catch (e: Exception) {
            Log.d("PutOnUseCase", e.message.toString())
        }
    }
}