package com.braincorp.petrolwatcher.feature.auth

import android.content.Intent
import android.support.test.runner.AndroidJUnit4
import com.braincorp.petrolwatcher.base.BaseActivityTest
import com.braincorp.petrolwatcher.R
import com.braincorp.petrolwatcher.feature.auth.model.AuthErrorType
import com.braincorp.petrolwatcher.feature.auth.robots.authenticationError
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthenticationErrorActivityTest : BaseActivityTest<AuthenticationErrorActivity>(
        AuthenticationErrorActivity::class.java, autoLaunch = false) {

    private lateinit var errorType: AuthErrorType

    @Test
    fun whenClickingOnTryAgain_shouldCloseScreen() {
        errorTypeIs(AuthErrorType.CONNECTION)

        authenticationError {
        } clickTryAgain {
            screenIsClosed()
        }
    }

    @Test
    fun withConnectionError_shouldDisplayCorrectImage() {
        errorTypeIs(AuthErrorType.CONNECTION)

        authenticationError {
            imageIs(R.drawable.ic_disconnected)
        }
    }

    @Test
    fun withConnectionError_shouldDisplayCorrectMessage() {
        errorTypeIs(AuthErrorType.CONNECTION)

        authenticationError {
            messageIs(R.string.error_connection)
        }
    }

    @Test
    fun withFacebookError_shouldDisplayCorrectImage() {
        errorTypeIs(AuthErrorType.FACEBOOK)

        authenticationError {
            imageIs(R.drawable.ic_error)
        }
    }

    @Test
    fun withFacebookError_shouldDisplayCorrectMessage() {
        errorTypeIs(AuthErrorType.FACEBOOK)

        authenticationError {
            messageIs(R.string.error_facebook)
        }
    }

    @Test
    fun withGoogleError_shouldDisplayCorrectImage() {
        errorTypeIs(AuthErrorType.GOOGLE)

        authenticationError {
            imageIs(R.drawable.ic_error)
        }
    }

    @Test
    fun withGoogleError_shouldDisplayCorrectMessage() {
        errorTypeIs(AuthErrorType.GOOGLE)

        authenticationError {
            messageIs(R.string.error_google)
        }
    }

    override fun intent(): Intent {
        val intent = super.intent()
        intent.putExtra("error_type", errorType)
        return intent
    }

    private fun errorTypeIs(errorType: AuthErrorType) {
        this.errorType = errorType
        launch()
    }

}