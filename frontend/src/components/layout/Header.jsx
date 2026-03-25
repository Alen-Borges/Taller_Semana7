import { useAuth } from '../../context/AuthContext'

export default function Header({ title, subtitle, action }) {
  return (
    <div className="flex items-center justify-between mb-6">
      <div>
        {subtitle && (
          <p className="text-xs text-slate-400 font-medium uppercase tracking-wider mb-0.5">{subtitle}</p>
        )}
        <h1 className="text-2xl font-bold text-slate-900">{title}</h1>
      </div>
      {action && <div>{action}</div>}
    </div>
  )
}
