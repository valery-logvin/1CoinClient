package com.finance_tracker.finance_tracker.core.common

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.StringDesc

actual fun StringResource.localizedString(): String {
    return localized()
}

actual fun StringDesc.localizedString(): String {
    return localized()
}