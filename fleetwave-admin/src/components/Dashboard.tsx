import { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, Box, Typography, Grid } from '@mui/material';
import { Title } from 'react-admin';
import {
  PieChart,
  Pie,
  Cell,
  ResponsiveContainer,
  Legend,
  Tooltip,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
} from 'recharts';
import RadioIcon from '@mui/icons-material/Radio';
import PeopleIcon from '@mui/icons-material/People';
import GroupsIcon from '@mui/icons-material/Groups';
import AssignmentIcon from '@mui/icons-material/Assignment';

interface Stats {
  radios: {
    total: number;
    available: number;
    assigned: number;
    inRepair: number;
  };
  persons: {
    total: number;
    enabled: number;
  };
  workgroups: {
    total: number;
  };
  assignments: {
    total: number;
    active: number;
  };
}

const COLORS = ['#4caf50', '#2196f3', '#ff9800', '#9e9e9e'];

/**
 * Admin Dashboard - Main landing page for administrators
 * Shows key metrics, charts, and quick links
 */
export const Dashboard = () => {
  const [stats, setStats] = useState<Stats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Fetch stats from backend
    fetch('/api/stats/overview', {
      headers: {
        'X-Tenant': 'ocps',
      },
    })
      .then((res) => res.json())
      .then((data) => {
        setStats(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error('Failed to load stats:', err);
        setLoading(false);
      });
  }, []);

  if (loading) {
    return <div>Loading dashboard...</div>;
  }

  // Prepare chart data
  const radioStatusData = stats
    ? [
        { name: 'Available', value: stats.radios.available },
        { name: 'Assigned', value: stats.radios.assigned },
        { name: 'In Repair', value: stats.radios.inRepair },
      ]
    : [];

  const overviewData = stats
    ? [
        { name: 'Radios', count: stats.radios.total },
        { name: 'Persons', count: stats.persons.total },
        { name: 'Workgroups', count: stats.workgroups.total },
        { name: 'Assignments', count: stats.assignments.total },
      ]
    : [];

  return (
    <div>
      <Title title="FleetWave Admin Dashboard" />

      {/* Key Metrics Cards */}
      <Grid container spacing={2} sx={{ mt: 1 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography variant="h4" fontWeight="bold">
                    {stats?.radios.total || 0}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Total Radios
                  </Typography>
                  <Typography variant="caption" color="success.main">
                    {stats?.radios.available || 0} available
                  </Typography>
                </Box>
                <RadioIcon sx={{ fontSize: 48, color: 'primary.main', opacity: 0.3 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography variant="h4" fontWeight="bold">
                    {stats?.persons.total || 0}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Personnel
                  </Typography>
                  <Typography variant="caption" color="success.main">
                    {stats?.persons.enabled || 0} enabled
                  </Typography>
                </Box>
                <PeopleIcon sx={{ fontSize: 48, color: 'success.main', opacity: 0.3 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography variant="h4" fontWeight="bold">
                    {stats?.workgroups.total || 0}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Workgroups
                  </Typography>
                </Box>
                <GroupsIcon sx={{ fontSize: 48, color: 'warning.main', opacity: 0.3 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box display="flex" alignItems="center" justifyContent="space-between">
                <Box>
                  <Typography variant="h4" fontWeight="bold">
                    {stats?.assignments.active || 0}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Active Assignments
                  </Typography>
                  <Typography variant="caption" color="text.secondary">
                    {stats?.assignments.total || 0} total
                  </Typography>
                </Box>
                <AssignmentIcon sx={{ fontSize: 48, color: 'info.main', opacity: 0.3 }} />
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Charts Row */}
      <Grid container spacing={2} sx={{ mt: 2 }}>
        <Grid item xs={12} md={6}>
          <Card>
            <CardHeader title="Radio Fleet Status" />
            <CardContent>
              <ResponsiveContainer width="100%" height={300}>
                <PieChart>
                  <Pie
                    data={radioStatusData}
                    cx="50%"
                    cy="50%"
                    labelLine={false}
                    label={(entry) => `${entry.name}: ${entry.value}`}
                    outerRadius={80}
                    fill="#8884d8"
                    dataKey="value"
                  >
                    {radioStatusData.map((_entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                  <Legend />
                </PieChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={6}>
          <Card>
            <CardHeader title="System Overview" />
            <CardContent>
              <ResponsiveContainer width="100%" height={300}>
                <BarChart data={overviewData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="name" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="count" fill="#2196f3" />
                </BarChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Welcome Card */}
      <Card sx={{ mt: 2 }}>
        <CardHeader title="Welcome to FleetWave Admin" />
        <CardContent>
          <Typography paragraph>
            Manage your radio fleet inventory, personnel, workgroups, assignments, and more.
          </Typography>
          <Typography variant="body2" color="text.secondary">
            FleetWave v0.4.0-SNAPSHOT | Tenant: OCPS (Orange County Public Schools)
          </Typography>
        </CardContent>
      </Card>
    </div>
  );
};
