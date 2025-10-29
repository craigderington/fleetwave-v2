import { Card, CardContent, CardHeader } from '@mui/material';
import { Title } from 'react-admin';

/**
 * Admin Dashboard - Main landing page for administrators
 * Shows key metrics and quick links
 */
export const Dashboard = () => {
  return (
    <div>
      <Title title="FleetWave Admin Dashboard" />

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '1rem', marginTop: '1rem' }}>
        <Card>
          <CardHeader title="Welcome to FleetWave Admin" />
          <CardContent>
            <p>
              Manage your radio fleet inventory, personnel, workgroups, assignments, and more.
            </p>
            <p>
              Use the navigation menu to access different sections:
            </p>
            <ul>
              <li><strong>Radios</strong> - Manage radio inventory</li>
              <li><strong>Persons</strong> - Manage personnel records</li>
              <li><strong>Workgroups</strong> - Manage organizational groups</li>
              <li><strong>Assignments</strong> - Track radio assignments</li>
              <li><strong>Requests</strong> - Handle radio requests</li>
              <li><strong>Work Orders</strong> - Manage maintenance</li>
              <li><strong>Alerts</strong> - Monitor system alerts</li>
            </ul>
          </CardContent>
        </Card>

        <Card>
          <CardHeader title="Quick Stats" />
          <CardContent>
            <p>Dashboard metrics will be displayed here:</p>
            <ul>
              <li>Total radios</li>
              <li>Available radios</li>
              <li>Active assignments</li>
              <li>Pending requests</li>
              <li>Open alerts</li>
            </ul>
            <p><em>Charts and visualizations coming soon!</em></p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader title="System Status" />
          <CardContent>
            <p>FleetWave v0.4.0-SNAPSHOT</p>
            <p>Tenant: <strong>OCPS</strong> (Orange County Public Schools)</p>
            <p>Status: <span style={{ color: 'green', fontWeight: 'bold' }}>OPERATIONAL</span></p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};
