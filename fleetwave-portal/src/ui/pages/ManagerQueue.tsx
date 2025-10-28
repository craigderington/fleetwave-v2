import React, { useEffect, useState } from 'react'
import { api } from '../api'
import { useMe } from '../useMe'

export function ManagerQueue(){
  const me = useMe()
  const [items, setItems] = useState<any[]>([])
  const load = () => api('/api/requests?status=PENDING').then(setItems).catch(()=>setItems([]))
  useEffect(() => { load() }, [])
  const act = (id:string, decision:'approve'|'reject') => api(`/api/requests/${id}/${decision}`, { method:'POST', body: JSON.stringify({ }) }).then(load)
  const fulfill = (id:string) => api(`/api/requests/${id}/fulfill`, { method:'POST' }).then(load)
  return (
    <div>
      <h2>Manager Queue</h2>
      {!me && <p>Please sign in.</p>}
      <table>
        <thead><tr><th>ID</th><th>Requester</th><th>Workgroup</th><th>Pref</th><th>Status</th><th>Actions</th></tr></thead>
        <tbody>
          {items.map((r:any) => (
            <tr key={r.id}>
              <td>{r.id}</td><td>{r.requesterId}</td><td>{r.workgroup?.id || r.workgroupId}</td><td>{r.radioModelPref||'-'}</td><td>{r.status}</td>
              <td>
                <button onClick={()=>act(r.id,'approve')}>Approve</button>
                <button onClick={()=>act(r.id,'reject')}>Reject</button>
                <button onClick={()=>fulfill(r.id)} disabled={r.status!=='APPROVED'}>Fulfill</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
