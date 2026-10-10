import type {SpeechProvider,SttResult} from "./speaking-types.js";

export class MockSttProvider implements SpeechProvider {
  async transcribe(input:{temporaryObjectKey:string;language:string}):Promise<SttResult>{
    if(input.temporaryObjectKey.includes("provider-error"))
      throw new Error("STT_PROVIDER_ERROR");

    if(input.temporaryObjectKey.includes("timeout"))
      throw new Error("STT_TIMEOUT");

    return {
      transcript:"I eat rice for breakfast.",
      confidence:0.96,
      language:input.language,
      durationMs:4200
    };
  }
}
