import { useState } from 'react'

export function usePagination(initialPage = 0, initialSize = 10) {
  const [page, setPage] = useState(initialPage)
  const [size] = useState(initialSize)

  const goToPage = (p) => setPage(p)
  const nextPage = () => setPage((prev) => prev + 1)
  const prevPage = () => setPage((prev) => Math.max(0, prev - 1))
  const reset = () => setPage(0)

  return { page, size, goToPage, nextPage, prevPage, reset }
}
