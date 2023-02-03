package com.dreamsoftware.artcollectibles.data.core.network

import android.util.Log
import com.dreamsoftware.artcollectibles.data.core.network.exception.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Some HTTP response codes that We could get when making something request
 */
const val BAD_REQUEST_CODE: Int = 400
const val UNAUTHORIZED_CODE: Int = 401
const val NOT_FOUND_CODE: Int = 404
const val INTERNAL_SERVER_ERROR_CODE: Int = 500
const val CONFLICT_ERROR_CODE: Int = 409
const val FORBIDDEN_CODE: Int = 403

internal abstract class SupportNetworkDataSource {

    /**
     * Wrap for safe Network Call
     * @param onExecuted
     */
    protected suspend fun <T> safeNetworkCall(onExecuted: suspend () -> T): T =
        withContext(Dispatchers.IO) {
            try {
                onExecuted()
            } catch (exception: IOException) {
                exception.printStackTrace()
                // map interrupted I/O to Network No Internet Exception
                throw NetworkNoInternetException()
            } catch (ex: NetworkException) {
                throw ex
            } catch (exception: Throwable) {
                exception.printStackTrace()
                val retrofitException = RetrofitException.asRetrofitException(exception)
                if (retrofitException.kind === RetrofitException.Kind.NETWORK) {
                    throw NetworkErrorException(cause = exception)
                } else {
                    try {
                        throw onApiException(retrofitException)
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                        throw NetworkErrorException(cause = e1)
                    }
                }
            }
        }

    /**
     * Map HTTP Error codes to exceptions to easy handler
     * @param apiException
     */
    /**
     * Map HTTP Error codes to exceptions to easy handler
     * @param apiException
     */
    open fun onApiException(apiException: RetrofitException): Exception =
        apiException.response?.let {
            Log.d("ART_COLL", "it.message() -> ${it.message()} CALLED!")
            apiException.printStackTrace()
            when (it.code()) {
                BAD_REQUEST_CODE -> NetworkBadRequestException(
                    message = it.message(),
                    cause = apiException
                )
                UNAUTHORIZED_CODE -> NetworkUnauthorizedException(
                    message = it.message(),
                    cause = apiException
                )
                FORBIDDEN_CODE -> NetworkForbiddenException(
                    message = it.message(),
                    cause = apiException
                )
                NOT_FOUND_CODE -> NetworkNoResultException(
                    message = it.message(),
                    cause = apiException
                )
                INTERNAL_SERVER_ERROR_CODE -> NetworkErrorException(
                    message = it.message(),
                    cause = apiException
                )
                CONFLICT_ERROR_CODE -> NetworkUnverifiedAccountException(
                    message = it.message(),
                    cause = apiException
                )
                else -> NetworkErrorException()
            }
        } ?: NetworkErrorException()
}