import { NavLink, Route, Routes, useNavigate } from 'react-router-dom'
import { useRecoilState, useRecoilValue } from 'recoil'
import { isAuthedState, meState, tokensState } from './state/auth'
import LoginPage from './pages/LoginPage'
import TodayPage from './pages/TodayPage'
import HabitsPage from './pages/HabitsPage'
import HistoryPage from './pages/HistoryPage'

export default function App() {
  const isAuthed = useRecoilValue(isAuthedState)
  const [tokens, setTokens] = useRecoilState(tokensState)
  const [me, setMe] = useRecoilState(meState)
  const nav = useNavigate()

  const logout = () => {
    setTokens(null)
    setMe(null)
    nav('/login')
  }

  return (
    <div className="container">
      <div className="row" style={{ justifyContent: 'space-between' }}>
        <h2 style={{ margin: 0 }}>자기개발 플랫폼</h2>
        <div className="row">
          {isAuthed && me ? <span className="badge">{me.displayName}</span> : <span className="badge">Guest</span>}
          {isAuthed ? <button onClick={logout}>로그아웃</button> : null}
        </div>
      </div>

      {isAuthed ? (
        <div className="nav">
          <NavLink to="/" end className={({ isActive }) => (isActive ? 'active' : '')}>오늘</NavLink>
          <NavLink to="/habits" className={({ isActive }) => (isActive ? 'active' : '')}>습관</NavLink>
          <NavLink to="/history" className={({ isActive }) => (isActive ? 'active' : '')}>기록</NavLink>
        </div>
      ) : null}

      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/" element={isAuthed ? <TodayPage /> : <LoginPage />} />
        <Route path="/habits" element={isAuthed ? <HabitsPage /> : <LoginPage />} />
        <Route path="/history" element={isAuthed ? <HistoryPage /> : <LoginPage />} />
      </Routes>

      <p className="small" style={{ marginTop: 24 }}>
        로컬 데모 UI입니다. API URL은 <code>VITE_API_BASE</code>로 설정할 수 있습니다.
      </p>
    </div>
  )
}
