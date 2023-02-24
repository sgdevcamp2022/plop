package com.plop.plopmessenger.domain.usecase.user

import android.util.Log
import com.plop.plopmessenger.data.dto.response.user.toPeople
import com.plop.plopmessenger.domain.model.People
import com.plop.plopmessenger.domain.model.toPeople
import com.plop.plopmessenger.domain.repository.UserRepository
import com.plop.plopmessenger.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(
        query: String
    ): Flow<Resource<List<People>>> = flow {
        try {
            val response = userRepository.getSearchUser(query)
            Log.d("SearchUserUseCase", query)
            if(response.isSuccessful) {
                if(response.body() != null) {
                    emit(Resource.Success(response.body()?.people?.map{it.toPeople()}?: emptyList()))
                }
                Log.d("SearchUserUseCase", "success")
            } else  {
                Log.d("SearchUserUseCase", "error + ${response.code()} + ${response.message()}")
                emit(Resource.Error("error"))
            }
        }catch (e: Exception) {
            Log.d("SearchUserUseCase", "error")
            emit(Resource.Error("error"))
        }
    }
}