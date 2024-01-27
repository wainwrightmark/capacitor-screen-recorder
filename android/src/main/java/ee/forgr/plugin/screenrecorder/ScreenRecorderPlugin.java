package ee.forgr.plugin.screenrecorder;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import dev.bmcreations.scrcast.ScrCast;
import dev.bmcreations.scrcast.config.Options;

import android.media.MediaRecorder;

import dev.bmcreations.scrcast.config.Options;
import dev.bmcreations.scrcast.config.StorageConfig;
import dev.bmcreations.scrcast.config.VideoConfig;
import dev.bmcreations.scrcast.config.ChannelConfig;
import dev.bmcreations.scrcast.internal.config.dsl.NotificationConfigBuilder;




@CapacitorPlugin(name = "ScreenRecorder")
public class ScreenRecorderPlugin extends Plugin {

  private ScrCast recorder;

  @Override
  public void load() {
    recorder = ScrCast.use(this.bridge.getActivity());
  //todo change all these options

    // create configuration for video
    VideoConfig videoConfig = new VideoConfig(
      1920,
      1080,
      MediaRecorder.VideoEncoder.H264,
      8_000_000,
      360
);

// create configuration for storage
StorageConfig storageConfig = new StorageConfig("scrcast-sample");

// create configuration for notification channel for recording
ChannelConfig channelConfig = new ChannelConfig("1337", "Recording Service");

// create configuration for our notification

NotificationConfigBuilder  notificationConfig = new NotificationConfigBuilder();
notificationConfig.setShowPause(true);
notificationConfig.setShowStop(true);
notificationConfig.setShowTimer(true);
notificationConfig.setChannel(channelConfig);

Options options = new Options(
      videoConfig,
      storageConfig,
      notificationConfig.build(),
      false,
      5000,
      true
);
    recorder.updateOptions(options);
  }

  @PluginMethod
  public void start(PluginCall call) {
    recorder.record();
    call.resolve();
  }

  @PluginMethod
  public void stop(PluginCall call) {
    recorder.stopRecording();
    call.resolve();
  }
}
