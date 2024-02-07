import { WebPlugin } from "@capacitor/core";

import type { ScreenRecorderPlugin, RecordingStateResult } from "./definitions";

export class ScreenRecorderWeb
  extends WebPlugin
  implements ScreenRecorderPlugin
{
  async start(): Promise<RecordingStateResult> {
    throw new Error("Method not implemented.");
  }
  async stop(): Promise<void> {
    throw new Error("Method not implemented.");
  }

  async recording_state(): Promise<RecordingStateResult> {
    return {value: "unknown"};
  }
}
