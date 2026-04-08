import { useNavigate } from 'react-router-dom'
import Button from '../components/ui/Button'

export default function NotFoundPage() {
  const navigate = useNavigate()
  return (
    <div className="min-h-screen bg-slate-50 flex items-center justify-center p-4">
      <div className="text-center">
        <p className="text-7xl font-black text-slate-200 mb-2">404</p>
        <h1 className="text-2xl font-bold text-slate-800 mb-2">Página no encontrada</h1>
        <p className="text-sm text-slate-500 mb-6">La ruta que buscas no existe en el sistema.</p>
        <Button onClick={() => navigate('/dashboard')}>← Volver al inicio</Button>
      </div>
    </div>
  )
}
