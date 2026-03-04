import { atom, selector } from 'recoil'

export type Me = { id: number; email: string; displayName: string }
export type Tokens = { accessToken: string; refreshToken: string }

const LS_KEY = 'selfdev_tokens'

export const tokensState = atom<Tokens | null>({
  key: 'tokensState',
  default: (() => {
    const raw = localStorage.getItem(LS_KEY)
    return raw ? (JSON.parse(raw) as Tokens) : null
  })(),
  effects: [
    ({ onSet }) => {
      onSet((val) => {
        if (!val) localStorage.removeItem(LS_KEY)
        else localStorage.setItem(LS_KEY, JSON.stringify(val))
      })
    },
  ],
})

export const meState = atom<Me | null>({ key: 'meState', default: null })

export const isAuthedState = selector<boolean>({
  key: 'isAuthedState',
  get: ({ get }) => !!get(tokensState)?.accessToken,
})
