import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(ScreenRecorderPlugin)
public class ScreenRecorderPlugin: CAPPlugin {
    private let implementation = ScreenRecorder()

    @objc func start(_ call: CAPPluginCall) {
        implementation.startRecording(saveToCameraRoll: true, handler: { error in
            if let error = error {
                debugPrint("Error when start recording \(error)")
                call.reject("Cannot start recording")
            } else {
                let result = self.implementation.recording_state();
                call.resolve([
        "value": result
    ])
            }
        })
    }
    @objc func stop(_ call: CAPPluginCall) {
        implementation.stoprecording(handler: { error in
            if let error = error {
                debugPrint("Error when stop recording \(error)")
                call.reject("Cannot stop recording")
            } else {
                call.resolve()
            }
        })
    }


    @objc func recording_state(_ call: CAPPluginCall){
        let result = implementation.recording_state();
        call.resolve([
        "value": result
    ])
    }
}
