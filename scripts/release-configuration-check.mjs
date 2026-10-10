import fs from 'node:fs'; import path from 'node:path';
const migrations=fs.readdirSync('database/migrations').filter(x=>x.endsWith('.sql'));
if(migrations.length!==16){console.error(`FAIL: expected 16 migrations, found ${migrations.length}`);process.exit(1)}
const compose=fs.readFileSync('infrastructure/staging/docker-compose.staging.yml','utf8');
for(const service of ['postgres','migrate','api','web','admin']) if(!new RegExp('^\\s{2}'+service+':','m').test(compose)){console.error(`FAIL: missing staging service ${service}`);process.exit(1)}
console.log('PASS: release configuration consistency'); console.log(`PASS: migrations=${migrations.length}`); console.log('PASS: staging services=postgres,migrate,api,web,admin');
