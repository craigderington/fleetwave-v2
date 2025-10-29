# FleetWave Admin Dashboard

React-Admin based administrative dashboard for FleetWave radio fleet management system.

## Features

- **Radio Management** - Full CRUD for radio inventory
- **Personnel Management** - Manage persons with authentication
- **Workgroup Management** - Organize users into workgroups
- **Assignment Tracking** - Monitor radio assignments (coming soon)
- **Request Management** - Handle radio requests (coming soon)
- **Work Orders** - Maintenance tracking (coming soon)
- **Alerts** - System alerts and notifications (coming soon)

## Tech Stack

- **React 18.3.1** - UI framework
- **React-Admin 4.16** - Admin framework
- **Material-UI 5.15** - UI components
- **TypeScript 5.6** - Type safety
- **Vite 5.4** - Build tool
- **Recharts 2.10** - Data visualization

## Development

### Prerequisites

- Node.js 18+
- npm or yarn
- FleetWave backend running on `localhost:8080`

### Install Dependencies

```bash
npm install
```

### Run Development Server

```bash
npm run dev
```

The admin dashboard will be available at `http://localhost:3001`

### Build for Production

```bash
npm run build
```

Output will be in the `dist/` directory.

## Configuration

### API Proxy

The Vite dev server proxies API requests to the backend:

- `/api/*` → `http://localhost:8080/api/*`
- `/actuator/*` → `http://localhost:8080/actuator/*`

This is configured in `vite.config.ts`.

### Authentication

The admin dashboard uses session-based authentication provided by Spring Security. Users must log in via the `/login` page before accessing the admin interface.

### Multi-tenancy

All API requests include the `X-Tenant: ocps` header. This is currently hardcoded but can be made dynamic in the future.

## Project Structure

```
fleetwave-admin/
├── src/
│   ├── components/
│   │   └── Dashboard.tsx          # Main dashboard
│   ├── providers/
│   │   ├── dataProvider.ts        # REST API adapter
│   │   └── authProvider.ts        # Authentication adapter
│   ├── resources/
│   │   ├── radios/                # Radio CRUD components
│   │   ├── persons/               # Person CRUD components
│   │   └── workgroups/            # Workgroup CRUD components
│   ├── App.tsx                    # Main application
│   └── main.tsx                   # Entry point
├── index.html
├── package.json
├── tsconfig.json
├── vite.config.ts
└── README.md
```

## Deployment

For production, build the admin dashboard and serve it via Spring Boot:

```bash
# Build the admin dashboard
npm run build

# Copy to Spring Boot static resources
cp -r dist/* ../fleetwave-web/src/main/resources/static/admin/

# Access at http://localhost:8080/admin/
```

## License

Same as FleetWave parent project.
