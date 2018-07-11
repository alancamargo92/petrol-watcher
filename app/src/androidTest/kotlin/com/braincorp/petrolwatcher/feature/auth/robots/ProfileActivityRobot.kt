package com.braincorp.petrolwatcher.feature.auth.robots

import br.com.concretesolutions.kappuccino.actions.ClickActions.click
import com.braincorp.petrolwatcher.R

fun profile(func: ProfileActivityRobot.() -> Unit) = ProfileActivityRobot().apply(func)

class ProfileActivityRobot {

    infix fun clickCamera(func: ProfileResult.() -> Unit) {
        click {
            id(R.id.bt_camera)
        }

        applyResult(func)
    }

    infix fun clickGallery(func: ProfileResult.() -> Unit) {
        click {
            id(R.id.bt_gallery)
        }

        applyResult(func)
    }

    private fun applyResult(func: ProfileResult.() -> Unit) {
        ProfileResult().apply(func)
    }

}

class ProfileResult {



}