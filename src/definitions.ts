export interface ScreenRecorderPlugin {
  /**
   * start the recording. Returns recording state.
   *
   */
  start(): Promise<RecordingStateResult>;
  /**
   * stop the recording
   *
   */
  stop(): Promise<void>;

  /**
   * Returns the current recording state.
   */
  recording_state(): Promise<RecordingStateResult>;
}


export interface RecordingStateResult {
  value: "idle" | "paused" | "recording" | "startDelay" | "error" | "unknown";
}
