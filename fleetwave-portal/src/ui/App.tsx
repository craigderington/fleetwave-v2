import React from 'react'
import { BrowserRouter, Routes, Route, Link } from 'react-router-dom'
import { MyAssignments } from './pages/MyAssignments'
import { SubmitRequest } from './pages/SubmitRequest'
import { ManagerQueue } from './pages/ManagerQueue'

export function App(){
  return (
    <BrowserRouter>
      <div style={{fontFamily:'system-ui', padding: 24}}>
        <h1>FleetWave Portal</h1>
        <nav style={{display:'flex', gap:12, marginBottom:16}}>
          <Link to="/">My Assignments</Link>
          <Link to="/request">Submit Request</Link>
          <Link to="/queue">Manager Queue</Link>
        </nav>
        <Routes>
          <Route path="/" element={<MyAssignments/>} />
          <Route path="/request" element={<SubmitRequest/>} />
          <Route path="/queue" element={<ManagerQueue/>} />
        </Routes>
      </div>
    </BrowserRouter>
  )
}
