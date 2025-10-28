import React, { useEffect, useState } from 'react'
import { api } from '../api'

export function MyAssignments(){
  const [items, setItems] = useState<any[]>([])
  useEffect(() => {
    api('/api/assignments?status=ACTIVE&my=true').then(setItems).catch(()=>setItems([]))
  }, [])
  return (
    <div>
      <h2>My Assignments</h2>
      <table>
        <thead><tr><th>ID</th><th>Radio</th><th>Ends</th><th>Action</th></tr></thead>
        <tbody>
          {items.map((a:any) => (
            <tr key={a.id}>
              <td>{a.id}</td><td>{a.radio?.serial || a.radioId}</td><td>{a.expectedEnd || '-'}</td>
              <td><button onClick={() => api(`/api/assignments/${a.id}/return`, { method:'POST'}).then(()=>location.reload())}>Return</button></td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
