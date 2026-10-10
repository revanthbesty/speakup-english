import process from 'node:process';
const base=(process.env.STAGING_BASE_URL||'http://localhost').replace(/\/$/,'');
const checks=[['web',`${base}:3000/`],['admin',`${base}:3001/`],['api health',`${base}:4000/health`],['api readiness',`${base}:4000/ready`]];
let bad=0; for(const [name,url] of checks){try{const r=await fetch(url,{signal:AbortSignal.timeout(8000)}); if(!r.ok){console.error(`FAIL ${name}: HTTP ${r.status}`);bad++}else console.log(`PASS ${name}: HTTP ${r.status}`)}catch(e){console.error(`FAIL ${name}: ${e.message}`);bad++}}
if(bad)process.exit(1);
