import { createContext, useContext, useReducer, useEffect } from 'react'

const AuthContext = createContext(null)

const initialState = {
  token: localStorage.getItem('token') || null,
  user: JSON.parse(localStorage.getItem('user') || 'null'),
  isAuthenticated: !!localStorage.getItem('token'),
}

function authReducer(state, action) {
  switch (action.type) {
    case 'LOGIN':
      return {
        token: action.payload.token,
        user: action.payload.user,
        isAuthenticated: true,
      }
    case 'LOGOUT':
      return { token: null, user: null, isAuthenticated: false }
    default:
      return state
  }
}

export function AuthProvider({ children }) {
  const [state, dispatch] = useReducer(authReducer, initialState)

  useEffect(() => {
    if (state.token) {
      localStorage.setItem('token', state.token)
      localStorage.setItem('user', JSON.stringify(state.user))
    } else {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }, [state.token, state.user])

  const login = (token, user) => {
    dispatch({ type: 'LOGIN', payload: { token, user } })
  }

  const logout = () => {
    dispatch({ type: 'LOGOUT' })
  }

  return (
    <AuthContext.Provider value={{ ...state, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be used within AuthProvider')
  return ctx
}
