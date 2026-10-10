import test from "node:test";
import assert from "node:assert/strict";
import { MockSttProvider } from "../src/modules/speaking/mock-stt-provider.js";

test("mock STT returns deterministic transcript for MVP", async () => {
  const provider=new MockSttProvider();
  const result=await provider.transcribe({
    temporaryObjectKey:"tmp/speaking-1",
    language:"en"
  });
  assert.equal(result.transcript,"I eat rice for breakfast.");
  assert.equal(result.language,"en");
  assert.ok(result.confidence > .9);
});

test("STT provider failure is represented as a technical failure", async () => {
  const provider=new MockSttProvider();
  await assert.rejects(
    provider.transcribe({temporaryObjectKey:"tmp/provider-error",language:"en"}),
    /STT_PROVIDER_ERROR/
  );
});
