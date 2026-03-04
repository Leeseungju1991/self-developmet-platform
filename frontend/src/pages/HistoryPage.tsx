import { useEffect, useState } from 'react'
import { useRecoilValue } from 'recoil'
import { tokensState } from '../state/auth'
import { apiFetch } from '../lib/api'

type Completion = { id: number; habitId: number; date: string; note: string }

export default function HistoryPage() {
  const tokens = useRecoilValue(tokensState)
  const [items, setItems] = useState<Completion[]>([])
  const [err, setErr] = useState<string | null>(null)

  const load = async () => {
    setErr(null)
    try {
      const data = await apiFetch('/api/habits/history', {}, tokens)
      setItems(data)
    } catch (e: any) {
      setErr(e.message)
    }
  }

  useEffect(() => { load() }, [])

  return (
    <div className="card">
      <h3 style={{ marginTop: 0 }}>완료 기록</h3>
      {err ? <div className="small" style={{ color: '#ffb4b4' }}>{err}</div> : null}
      <div className="list">
        {items.map((c) => (
          <div key={c.id} className="card" style={{ padding: 12 }}>
            <div className="row" style={{ justifyContent: 'space-between' }}>
              <div>
                <div style={{ fontWeight: 700 }}>habitId: {c.habitId}</div>
                <div className="small">{c.date}</div>
              </div>
              <span className="badge">{c.note ? '메모 있음' : '메모 없음'}</span>
            </div>
          </div>
        ))}
        {items.length === 0 ? <div className="small">아직 완료 기록이 없습니다.</div> : null}
      </div>
    </div>
  )
}
