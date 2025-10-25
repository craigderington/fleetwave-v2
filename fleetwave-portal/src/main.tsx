import React from 'react'
import { createRoot } from 'react-dom/client'
import { App } from './ui/App'
import { loadTheme } from './ui/theme'

loadTheme()
createRoot(document.getElementById('root')!).render(<App />)
