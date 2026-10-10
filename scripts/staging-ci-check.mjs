import { spawnSync } from 'node:child_process'; import process from 'node:process';
const run=(c,a)=>{const r=spawnSync(c,a,{stdio:'inherit',shell:process.platform==='win32'});if(r.error||r.status!==0)process.exit(r.status??1)};
run('node',['scripts/release-configuration-check.mjs']); run('node',['scripts/staging-preflight.mjs']);
