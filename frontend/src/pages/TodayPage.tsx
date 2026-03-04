import { useEffect, useState } from 'react'
import { useRecoilValue } from 'recoil'
import { tokensState } from '../state/auth'
import { apiFetch } from '../lib/api'

type Habit = { id: number; title: string; description: string; active: boolean }

export default function TodayPage() {
  const tokens = useRecoilValue(tokensState)
  const [items, setItems] = useState<Habit[]>([])
  const [err, setErr] = useState<string | null>(null)

  const load = async () => {
    setErr(null)
    try {
      const data = await apiFetch('/api/habits/today', {}, tokens)
      setItems(data)
    } catch (e: any) {
      setErr(e.message)
    }
  }

  const complete = async (habitId: number) => {
    try {
      await apiFetch(`/api/habits/${habitId}/complete`, { method: 'POST', body: JSON.stringify({ note: '' }) }, tokens)
      alert('완료 처리되었습니다.')
    } catch (e: any) {
      alert(e.message)
    }
  }

  useEffect(() => { load() }, [])

  return (
    <div className="card">
      <h3 style={{ marginTop: 0 }}>오늘의 습관</h3>
      {err ? <div className="small" style={{ color: '#ffb4b4' }}>{err}</div> : null}
      <div className="list">
        {items.map((h) => (
          <div key={h.id} className="card" style={{ padding: 12 }}>
            <div className="row" style={{ justifyContent: 'space-between' }}>
              <div>
                <div style={{ fontWeight: 700 }}>{h.title}</div>
                <div className="small">{h.description}</div>
              </div>
              <button className="primary" onClick={() => complete(h.id)}>완료</button>
            </div>
          </div>
        ))}
        {items.length === 0 ? <div className="small">등록된 습관이 없습니다. “습관” 탭에서 추가해 주세요.</div> : null}
      </div>
    </div>
  )
}
