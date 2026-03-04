import { Tokens } from '../state/auth'
const API_BASE = import.meta.env.VITE_API_BASE ?? 'http://localhost:8080'

export async function apiFetch(path: string, opts: RequestInit = {}, tokens?: Tokens | null) {
  const headers = new Headers(opts.headers)
  headers.set('Content-Type', 'application/json')
  if (tokens?.accessToken) headers.set('Authorization', `Bearer ${tokens.accessToken}`)

  const res = await fetch(`${API_BASE}${path}`, { ...opts, headers })
  const data = await res.json().catch(() => ({}))
  if (!res.ok) throw new Error(data?.message ?? 'API Error')
  return data
}
