@file:Suppress("MemberVisibilityCanBePrivate")

package dev.bmcreations.scrcast.internal.config.dsl

import android.graphics.Bitmap
import androidx.annotation.ColorRes
import androidx.annotation.RestrictTo
import dev.bmcreations.scrcast.config.*
import java.io.File

@RestrictTo(RestrictTo.Scope.LIBRARY)
class OptionsBuilder {
    private var video = VideoConfig()
    private var storage = StorageConfig()
    private var notification = NotificationConfig()
    var moveTaskToBack: Boolean = false
    var startDelayMs: Long = 0
    var stopOnScreenOff: Boolean = false

    @JvmSynthetic
    fun video(config: VideoConfigBuilder.() -> Unit) {
        video = VideoConfigBuilder().apply(config).build()
    }

    @JvmSynthetic
    fun storage(config: StorageConfigBuilder.() -> Unit) {
        storage = StorageConfigBuilder().apply(config).build()
    }

    @JvmSynthetic
    fun notification(config: NotificationConfigBuilder.() -> Unit) {
        notification = NotificationConfigBuilder()
            .apply(config).build()
    }

    fun build(): Options =
        Options(
            video,
            storage,
            notification,
            moveTaskToBack,
            startDelayMs,
            stopOnScreenOff
        )
}

class VideoConfigBuilder {
    private val defaultConfig = VideoConfig()
    var width: Int = defaultConfig.width
    var height: Int = defaultConfig.height
    var videoEncoder: Int = defaultConfig.videoEncoder
    var bitrate: Int = defaultConfig.bitrate
    var frameRate: Int = defaultConfig.frameRate
    var maxLengthSecs: Int = defaultConfig.maxLengthSecs

    fun build(): VideoConfig =
        VideoConfig(
            width,
            height,
            videoEncoder,
            bitrate,
            frameRate,
            maxLengthSecs
        )
}

class StorageConfigBuilder {
    private val defaultConfig = StorageConfig()

    var directoryName: String = defaultConfig.directoryName
    var directory: File = defaultConfig.directory
    var fileNameFormatter: FileFormatter = defaultConfig.fileNameFormatter
    var outputFormat: Int = defaultConfig.outputFormat
    var maxSizeMB: Float = defaultConfig.maxSizeMB

    fun build(): StorageConfig =
        StorageConfig(
            directoryName,
            directory,
            fileNameFormatter,
            outputFormat,
            maxSizeMB
        )
}


class NotificationConfigBuilder {
    private val defaultConfig = NotificationConfig()

    var title: String = defaultConfig.title
    var description: String = defaultConfig.description
    var icon: Bitmap? = defaultConfig.icon
    var id: Int = defaultConfig.id
    var showStop: Boolean = defaultConfig.showStop
    var showPause: Boolean = defaultConfig.showPause
    var showTimer: Boolean = defaultConfig.showTimer
    var useMediaStyle: Boolean = defaultConfig.useMediaStyle

    @ColorRes
    var accentColor: Int = defaultConfig.accentColor
    var colorAsBackground: Boolean = defaultConfig.colorAsBackground

    var channel: ChannelConfig = ChannelConfig()

    @JvmSynthetic
    fun channel(config: ChannelConfigBuilder.() -> Unit) {
        channel = ChannelConfigBuilder().apply(config).build()
    }

    fun build() = NotificationConfig(
        title = title,
        description = description,
        icon = icon,
        id = id,
        showStop = showStop,
        showPause = showPause,
        showTimer = showTimer,
        useMediaStyle = useMediaStyle,
        accentColor = accentColor,
        colorAsBackground = colorAsBackground,
        channel = channel
    )
}

class ChannelConfigBuilder {
    private val defaultConfig = ChannelConfig()

    var id: String = defaultConfig.id
    var name: String = defaultConfig.name
    var lightColor: Int = defaultConfig.lightColor
    var lockscreenVisibility: Int = defaultConfig.lockscreenVisibility

    fun build() = ChannelConfig(
        id,
        name,
        lightColor,
        lockscreenVisibility
    )
}
