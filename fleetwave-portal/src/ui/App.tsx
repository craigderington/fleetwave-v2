import React, { useEffect, useState } from 'react'

export function App(){
  const [me, setMe] = useState<any>(null)
  useEffect(() => {
    fetch('/api/auth/me', { credentials: 'include'}).then(r => r.ok ? r.json() : null).then(setMe).catch(() => setMe(null))
  }, [])
  return (
    <div style={{fontFamily:'system-ui', padding: 24}}>
      <h1>FleetWave Portal</h1>
      {me ? <p>Signed in as <b>{me.username || 'user'}</b></p> : <p>Please sign in at <a href="/login">/login</a></p>}
      <p>API base is same origin. Ensure portal is hosted behind the app or configure CORS.</p>
    </div>
  )
}
