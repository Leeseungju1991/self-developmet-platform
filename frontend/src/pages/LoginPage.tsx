import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useRecoilState } from 'recoil'
import { meState, tokensState } from '../state/auth'
import { apiFetch } from '../lib/api'

export default function LoginPage() {
  const [mode, setMode] = useState<'login' | 'signup'>('login')
  const [email, setEmail] = useState('user@example.com')
  const [password, setPassword] = useState('password123')
  const [displayName, setDisplayName] = useState('사용자')
  const [err, setErr] = useState<string | null>(null)
  const [, setTokens] = useRecoilState(tokensState)
  const [, setMe] = useRecoilState(meState)
  const nav = useNavigate()

  const submit = async () => {
    setErr(null)
    try {
      const data = await apiFetch(`/api/auth/${mode === 'login' ? 'login' : 'signup'}`, {
        method: 'POST',
        body: JSON.stringify(mode === 'login' ? { email, password } : { email, password, displayName }),
      })
      setTokens({ accessToken: data.accessToken, refreshToken: data.refreshToken })
      setMe(data.me)
      nav('/')
    } catch (e: any) {
      setErr(e.message ?? '오류')
    }
  }

  return (
    <div className="card" style={{ marginTop: 16 }}>
      <div className="row" style={{ justifyContent: 'space-between' }}>
        <h3 style={{ margin: 0 }}>{mode === 'login' ? '로그인' : '회원가입'}</h3>
        <button onClick={() => setMode(mode === 'login' ? 'signup' : 'login')}>
          {mode === 'login' ? '회원가입으로' : '로그인으로'}
        </button>
      </div>

      <div className="list" style={{ marginTop: 12 }}>
        <label>
          <div className="small">이메일</div>
          <input value={email} onChange={(e) => setEmail(e.target.value)} />
        </label>
        <label>
          <div className="small">비밀번호</div>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </label>

        {mode === 'signup' ? (
          <label>
            <div className="small">표시 이름</div>
            <input value={displayName} onChange={(e) => setDisplayName(e.target.value)} />
          </label>
        ) : null}

        {err ? <div className="small" style={{ color: '#ffb4b4' }}>{err}</div> : null}

        <button className="primary" onClick={submit}>
          {mode === 'login' ? '로그인' : '회원가입'}
        </button>

        <div className="small">
          백엔드 기본값: <code>http://localhost:8080</code>
        </div>
      </div>
    </div>
  )
}
