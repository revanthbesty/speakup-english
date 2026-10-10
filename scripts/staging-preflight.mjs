import { spawnSync } from 'node:child_process'; import fs from 'node:fs'; import process from 'node:process';
const fail=m=>{console.error(`FAIL: ${m}`);process.exit(1)};
const cmd=(c,a)=>{const r=spawnSync(c,a,{stdio:'ignore',shell:process.platform==='win32'});return !r.error&&r.status===0};
if(Number(process.versions.node.split('.')[0])<22) fail('Node.js 22+ is required');
if(!cmd('docker',['--version'])) fail('Docker is required for containerized staging');
if(!cmd('docker',['compose','version'])) fail('Docker Compose v2 is required');
for(const f of ['package.json','pnpm-workspace.yaml','infrastructure/staging/.env.staging.example','infrastructure/staging/docker-compose.staging.yml','infrastructure/staging/docker/Dockerfile.api','infrastructure/staging/docker/Dockerfile.web','infrastructure/staging/docker/Dockerfile.admin']) if(!fs.existsSync(f)) fail(`${f} missing`);
console.log('PASS: staging preflight');
