export async function loadTheme(){
  try {
    const cfg = await (await fetch('/api/v1/tenant/ui-config', { credentials:'include' })).json()
    document.documentElement.style.setProperty('--primary', cfg.primaryColor || '#0d6efd')
    const link = document.getElementById('brandLogo') as HTMLImageElement | null
    if (link) link.src = cfg.logoUrl
  } catch {}
}
