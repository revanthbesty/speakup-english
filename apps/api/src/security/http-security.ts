import crypto from 'node:crypto';
import type http from 'node:http';
export function requestId(req:http.IncomingMessage){return typeof req.headers['x-request-id']==='string'&&req.headers['x-request-id'].length<128?req.headers['x-request-id']:crypto.randomUUID()}
export function applySecurityHeaders(res:http.ServerResponse,id:string){res.setHeader('x-request-id',id);res.setHeader('x-content-type-options','nosniff');res.setHeader('x-frame-options','DENY');res.setHeader('referrer-policy','no-referrer');res.setHeader('permissions-policy','microphone=(self)');res.setHeader('cache-control','no-store')}
