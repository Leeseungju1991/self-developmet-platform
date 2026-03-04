import { useEffect, useState } from 'react'
import { useRecoilValue } from 'recoil'
import { tokensState } from '../state/auth'
import { apiFetch } from '../lib/api'

type Habit = { id: number; title: string; description: string; active: boolean }

export default function HabitsPage() {
  const tokens = useRecoilValue(tokensState)
  const [items, setItems] = useState<Habit[]>([])
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [err, setErr] = useState<string | null>(null)

  const load = async () => {
    setErr(null)
    try {
      const data = await apiFetch('/api/habits', {}, tokens)
      setItems(data)
    } catch (e: any) {
      setErr(e.message)
    }
  }

  const add = async () => {
    setErr(null)
    try {
      await apiFetch('/api/habits', { method: 'POST', body: JSON.stringify({ title, description }) }, tokens)
      setTitle('')
      setDescription('')
      load()
    } catch (e: any) {
      setErr(e.message)
    }
  }

  useEffect(() => { load() }, [])

  return (
    <div className="card">
      <h3 style={{ marginTop: 0 }}>습관 관리</h3>

      <div className="card" style={{ padding: 12 }}>
        <div className="row">
          <input placeholder="습관 제목" value={title} onChange={(e) => setTitle(e.target.value)} style={{ flex: 1, minWidth: 220 }} />
          <button className="primary" onClick={add} disabled={!title.trim()}>추가</button>
        </div>
        <textarea placeholder="설명(선택)" value={description} onChange={(e) => setDescription(e.target.value)} style={{ width: '100%', marginTop: 10, minHeight: 70 }} />
        {err ? <div className="small" style={{ color: '#ffb4b4', marginTop: 8 }}>{err}</div> : null}
      </div>

      <div className="list" style={{ marginTop: 12 }}>
        {items.map((h) => (
          <div key={h.id} className="card" style={{ padding: 12 }}>
            <div style={{ fontWeight: 700 }}>{h.title}</div>
            <div className="small">{h.description}</div>
          </div>
        ))}
      </div>
    </div>
  )
}
