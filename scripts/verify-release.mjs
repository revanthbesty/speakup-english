import { spawnSync } from 'node:child_process';
import process from 'node:process';
const run=(cmd,args)=>{const r=spawnSync(cmd,args,{stdio:'inherit',shell:process.platform==='win32'}); if(r.error){console.error(`FAIL: ${cmd}: ${r.error.message}`);process.exit(1)} if(r.status!==0) process.exit(r.status??1)};
if(Number(process.versions.node.split('.')[0])<22){console.error('FAIL: Node.js 22+ is required');process.exit(1)}
run('pnpm',['--version']);
run('pnpm',['install','--no-frozen-lockfile']);
for(const s of ['lint','typecheck','test','build']) run('pnpm',[s]);
console.log('PASS: full release verification');
