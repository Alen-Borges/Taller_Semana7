import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

const ShieldIcon = () => (
  <svg viewBox="0 0 24 24" fill="currentColor" className="w-6 h-6 text-primary-600">
    <path fillRule="evenodd" d="M12 1.5a.75.75 0 01.696.47l2.748 6.43 6.903.633a.75.75 0 01.416 1.318l-5.256 4.57 1.573 6.835a.75.75 0 01-1.116.813L12 18.265l-5.964 3.304a.75.75 0 01-1.116-.813l1.573-6.835-5.256-4.57a.75.75 0 01.416-1.318l6.903-.633L11.304 1.97A.75.75 0 0112 1.5z" clipRule="evenodd" />
  </svg>
)

const DashboardIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
  </svg>
)

const UsersIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z" />
  </svg>
)

const CarIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 17a2 2 0 11-4 0 2 2 0 014 0zM19 17a2 2 0 11-4 0 2 2 0 014 0z" />
    <path strokeLinecap="round" strokeLinejoin="round" d="M13 16V6a1 1 0 00-1-1H4a1 1 0 00-1 1v10l3.83-3.83a4 4 0 015.66 0L17 16zm0 0h2a2 2 0 002-2v-5a2 2 0 00-2-2h-2" />
  </svg>
)

const DocumentIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
  </svg>
)

const ClipboardIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
  </svg>
)

const PlusIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M12 4v16m8-8H4" />
  </svg>
)

const SearchIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={2}>
    <path strokeLinecap="round" strokeLinejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
  </svg>
)

export default function Sidebar() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const isGestor = user?.rol === 'GESTOR'

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <aside className="w-44 shrink-0 bg-white border-r border-slate-200 flex flex-col h-screen sticky top-0 overflow-y-auto">
      <div className="px-4 py-5 border-b border-slate-100">
        <div className="flex items-center gap-2 mb-0.5">
          <ShieldIcon />
          <span className="font-bold text-slate-900 text-base leading-none">InsurTech</span>
        </div>
        <p className="text-[10px] text-slate-400 font-medium tracking-widest pl-8">EVALUADOR DE SINIESTROS</p>
      </div>

      <nav className="flex-1 px-3 py-4 space-y-1">
        <NavLink to="/dashboard" className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}>
          <DashboardIcon /> Dashboard
        </NavLink>

        {isGestor ? (
          <>
            <NavLink to="/asegurados" className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}>
              <UsersIcon /> Asegurados
            </NavLink>
            <NavLink to="/vehiculos" className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}>
              <CarIcon /> Vehículos
            </NavLink>
            <NavLink to="/polizas" className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}>
              <DocumentIcon /> Pólizas
            </NavLink>
            <NavLink to="/reclamos" className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}>
              <ClipboardIcon /> Reclamos
            </NavLink>
          </>
        ) : (
          <>
            <NavLink to="/nuevo-reclamo" className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}>
              <PlusIcon /> Nuevo Reclamo
            </NavLink>
            <NavLink to="/estado-reclamo" className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}>
              <SearchIcon /> Consultar Estado
            </NavLink>
          </>
        )}
      </nav>

      {!isGestor && (
        <div className="px-3 py-3 border-t border-slate-100">
          <button
            onClick={() => navigate('/nuevo-reclamo')}
            className="w-full flex items-center justify-center gap-2 bg-primary-600 text-white text-xs font-medium py-2 px-3 rounded-lg hover:bg-primary-700 transition-colors"
          >
            <PlusIcon /> Nuevo Reclamo
          </button>
        </div>
      )}

      <div className="px-3 py-3 border-t border-slate-100">
        <div className="flex items-center gap-2 mb-2">
          <div className="w-7 h-7 rounded-full bg-primary-600 flex items-center justify-center text-white text-xs font-bold shrink-0">
            {user?.username?.[0]?.toUpperCase() || 'U'}
          </div>
          <div className="min-w-0">
            <p className="text-xs font-semibold text-slate-800 truncate">{user?.username || 'Usuario'}</p>
            <p className="text-[10px] text-slate-400">{user?.rol}</p>
          </div>
        </div>
        <button
          onClick={handleLogout}
          className="w-full text-xs text-slate-500 hover:text-red-500 transition-colors text-left py-1"
        >
          Cerrar sesión
        </button>
      </div>
    </aside>
  )
}
