export async function api<T=any>(path: string, init?: RequestInit): Promise<T>{
  const res = await fetch(path, { credentials: 'include', headers: {'Content-Type':'application/json', ...(init?.headers||{})}, ...init })
  if(!res.ok) throw new Error(await res.text())
  return res.json()
}
