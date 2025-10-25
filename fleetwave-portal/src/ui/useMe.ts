import { useEffect, useState } from 'react'
export type Me = { id: string, email: string, name: string } | null
export function useMe(){
  const [me, setMe] = useState<Me>(null)
  useEffect(() => { fetch('/api/users/me',{credentials:'include'}).then(r=> r.ok ? r.json() : null).then(setMe).catch(()=>setMe(null)) }, [])
  return me
}
