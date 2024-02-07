package ee.forgr.plugin.screenrecorder;

import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;


import android.media.MediaRecorder;

import androidx.annotation.NonNull;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import dev.bmcreations.scrcast.ScrCast;
import dev.bmcreations.scrcast.config.ChannelConfig;
import dev.bmcreations.scrcast.config.Options;
import dev.bmcreations.scrcast.internal.config.dsl.NotificationConfigBuilder;
import dev.bmcreations.scrcast.internal.config.dsl.VideoConfigBuilder;
import dev.bmcreations.scrcast.internal.config.dsl.StorageConfigBuilder;
import dev.bmcreations.scrcast.recorder.RecordingCallbacks;
import dev.bmcreations.scrcast.recorder.RecordingState;

import com.getcapacitor.plugin.util.AssetUtil;
import com.getcapacitor.JSObject;

import java.io.File;


class MyRecordingCallback implements RecordingCallbacks {
    PluginCall call;

    public MyRecordingCallback(PluginCall call1){
        call = call1;

    }

    @Override
    public void onRecordingFinished(@NonNull File file) {
        //do nothing (yet)
    }

    @Override
    public void onStateChange(@NonNull RecordingState state) {

        if (call != null){
            String value;
            if (state.isRecording()){
                value = "recording";
            } else if (state.isInStartDelay()){
                value = "startDelay";
            } else if (state.isPaused()) {
                value = "paused";
            }else if (state.isIdle()){
                value = "idle";
            }else if (state.isError()){
                value = "error";
            } else{
                value = "unknown";
            }

            JSObject ret = new JSObject();
            ret.put("value", value);
            call.resolve(ret);
            call = null;
        }

    }
}

@CapacitorPlugin(name = "ScreenRecorder")
public class ScreenRecorderPlugin extends Plugin {

    private ScrCast recorder;

    @Override
    public void load() {


        Context context = getContext();

        recorder = ScrCast.use(this.bridge.getActivity());
        //https://github.com/bmcreations/scrcast/blob/trunk/library/src/main/java/dev/bmcreations/scrcast/internal/config/dsl/OptionsDSL.kt


        DisplayMetrics dm = context.getResources().getDisplayMetrics();

        // create configuration for video
        VideoConfigBuilder videoConfig = new VideoConfigBuilder();

        videoConfig.setWidth((dm.widthPixels / 2) * 2);
        videoConfig.setHeight((dm.heightPixels / 2) * 2);

        // create configuration for storage
        StorageConfigBuilder storageConfig = new StorageConfigBuilder();
        storageConfig.setDirectoryName("WordSalad");
        storageConfig.setDirectory(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
        storageConfig.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

        // create configuration for notification channel for recording
        ChannelConfig channelConfig = new ChannelConfig("1990", "Word Salad Recording");

        // create configuration for our notification

        NotificationConfigBuilder notificationConfig = new NotificationConfigBuilder();
        notificationConfig.setShowPause(false); //doesn't seem to be helpful
        notificationConfig.setShowStop(false); //stopping never gets back to word salad :)
        notificationConfig.setTitle("Word Salad Recording");
        notificationConfig.setDescription("Recording Word Salad video");
        notificationConfig.setUseMediaStyle(false);

        //String icon_name = AssetUtil.getResourceBaseName("splash");
        int iconId = AssetUtil.getResourceID(context, "ic_action_name", "drawable");

        int colorId = AssetUtil.getResourceID(context, "colorPrimary", "color");

        Drawable icon = context.getDrawable(iconId);
        notificationConfig.setIcon(drawableToBitmap(icon));
        notificationConfig.setAccentColor(colorId);


        // notificationConfig.setChannel(channelConfig);

        Options options = new Options(videoConfig.build(), storageConfig.build(), notificationConfig.build(), false, 0, false);
        recorder.updateOptions(options);
    }

    @PluginMethod
    public void start(PluginCall call) {

        recorder.setRecordingCallback(new MyRecordingCallback(call));
        recorder.record();
    }

    @PluginMethod
    public void recording_state(PluginCall call){
        RecordingState state = recorder.getState();
        String value;
        if (state.isRecording()){
            value = "recording";
        } else if (state.isInStartDelay()){
            value = "startDelay";
        } else if (state.isPaused()) {
            value = "paused";
        }else if (state.isIdle()){
            value = "idle";
        }else if (state.isError()){
            value = "error";
        } else{
            value = "unknown";
        }

        //System.out.println("Checking is recording: " + value);

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.resolve(ret);

        //return .isRecording();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        recorder.stopRecording();
        call.resolve();
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
