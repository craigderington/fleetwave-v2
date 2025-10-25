import React, { useState } from 'react'
import { api } from '../api'
import { useMe } from '../useMe'

export function SubmitRequest(){
  const me = useMe()
  const [workgroupId, setWorkgroupId] = useState('')
  const [radioModelPref, setPref] = useState('')
  const [reason, setReason] = useState('')
  const [ok, setOk] = useState<string|undefined>(undefined)

  const submit = async (e:React.FormEvent) => {
    e.preventDefault()
    if (!me) return alert('Sign in required')
    const body = { workgroupId, radioModelPref, reason } // requester inferred on server
    await api('/api/v1/requests', { method:'POST', body: JSON.stringify(body)})
    setOk('Request submitted')
  }

  return (
    <div>
      <h2>Submit Request</h2>
      {!me && <p>Please sign in.</p>}
      <form onSubmit={submit}>
        <div><label>Workgroup ID</label><input value={workgroupId} onChange={e=>setWorkgroupId(e.target.value)} required /></div>
        <div><label>Model Preference</label><input value={radioModelPref} onChange={e=>setPref(e.target.value)} /></div>
        <div><label>Reason</label><textarea value={reason} onChange={e=>setReason(e.target.value)} required /></div>
        <button type="submit" disabled={!me}>Submit</button>
        {ok && <p>{ok}</p>}
      </form>
    </div>
  )
}
