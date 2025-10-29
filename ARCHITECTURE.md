# 🏗️ FleetWave Hybrid Architecture Plan

## 📋 Architecture Overview

FleetWave uses a **hybrid frontend architecture** that separates concerns based on user roles and use cases:

```
┌─────────────────────────────────────────────────────────────┐
│                     FleetWave Application                    │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  🌐 Thymeleaf (Server-Side Rendered)                        │
│  ├─ /login              - Authentication page               │
│  ├─ /logout             - Logout handler                    │
│  ├─ /                   - Landing/welcome page              │
│  ├─ /docs               - Public documentation              │
│  └─ /status             - System status page                │
│                                                               │
│  ⚛️  React Portal (End-User SPA)                            │
│  ├─ /portal/*           - Employee self-service             │
│  │   ├─ /my-assignments - View my active assignments        │
│  │   ├─ /request        - Submit radio request              │
│  │   └─ /queue          - Manager approval queue            │
│  │                                                            │
│  ⚛️  React-Admin (Admin SPA)                                │
│  └─ /admin/*            - Administrative dashboard          │
│      ├─ /radios         - Radio inventory management        │
│      ├─ /persons        - Personnel management              │
│      ├─ /workgroups     - Workgroup management              │
│      ├─ /assignments    - Assignment tracking               │
│      ├─ /requests       - Request management                │
│      ├─ /workorders     - Work order management             │
│      ├─ /alerts         - Alert management                  │
│      └─ /dashboard      - Stats and analytics               │
│                                                               │
│  🔌 REST API (Backend for All)                              │
│  └─ /api/*              - Spring Boot REST endpoints        │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

---

## 🎯 Design Principles

1. **Separation of Concerns**
   - Thymeleaf: Simple, server-rendered pages (auth, public content)
   - React Portal: End-user self-service workflows
   - React-Admin: Complex admin operations

2. **Single Source of Truth**
   - All frontends consume the same REST API
   - No duplicate business logic
   - Consistent data model

3. **Progressive Enhancement**
   - Public pages work without JavaScript (Thymeleaf)
   - Admin/Portal require modern browsers (React)

4. **Security First**
   - Spring Security handles authentication
   - Session-based auth for browser apps
   - X-Tenant header for multi-tenancy

---

## 📦 Technology Stack by Component

### 🌐 Thymeleaf Layer
- **Framework**: Thymeleaf 3.1.x
- **CSS**: Bootstrap 5.3.x (via Webjars)
- **JS Enhancement**: htmx 1.9.x (optional, for interactive elements)
- **Icons**: Bootstrap Icons
- **Forms**: Spring Form tags + Bean Validation

### ⚛️ React Portal (End-User)
- **Framework**: React 18.3.1 + TypeScript 5.6.3
- **Bundler**: Vite 5.4.10
- **Routing**: React Router 6.26.1
- **Styling**: CSS Modules or Tailwind CSS
- **State**: React Query (for API caching)

### ⚛️ React-Admin (Admin Dashboard)
- **Framework**: React-Admin 4.x
- **UI Library**: Material-UI 5.x
- **Data Provider**: Custom REST adapter
- **Auth Provider**: Cookie/session based
- **Charts**: Recharts or Chart.js

### 🔌 Backend (Shared)
- **API**: Spring Boot 3.3.x REST
- **Security**: Spring Security 6.x
- **Sessions**: Redis or in-memory (dev)

---

## 🗺️ Implementation Roadmap

### 📍 Phase 1: Thymeleaf Foundation (Week 1)

**Goal**: Set up server-side rendered public pages and authentication

#### Tasks:
- ✅ Add Thymeleaf dependencies to `fleetwave-web/pom.xml`
- ✅ Add Webjars (Bootstrap 5, Bootstrap Icons)
- ✅ Create Thymeleaf templates directory structure
- ✅ Create base layout template (`layout.html`)
- ✅ Implement login page (`/login`)
- ✅ Implement logout handler (`/logout`)
- ✅ Create landing page (`/`)
- ✅ Create system status page (`/status`)
- ✅ Add Thymeleaf controller (`WebController.java`)

#### Deliverables:
```
fleetwave-web/src/main/resources/
├─ templates/
│  ├─ layout.html              # Base layout with nav/footer
│  ├─ index.html               # Landing page
│  ├─ login.html               # Login form
│  ├─ status.html              # System status
│  └─ fragments/
│     ├─ header.html           # Navigation header
│     └─ footer.html           # Footer
```

---

### 📍 Phase 2: Spring Security Configuration (Week 1-2)

**Goal**: Implement secure authentication and authorization

#### Tasks:
- ✅ Add Spring Security dependencies
- ✅ Create `SecurityConfig.java` with session-based auth
- ✅ Implement `UserDetailsService` (load from Person entity)
- ✅ Configure login/logout flows
- ✅ Set up CSRF protection
- ✅ Create role-based access control (ROLE_USER, ROLE_ADMIN, ROLE_MANAGER)
- ✅ Add tenant resolution filter (extract from subdomain or session)
- ✅ Secure REST API endpoints (require auth)
- ✅ Add "Remember Me" functionality

#### Security Rules:
```java
// Public (no auth required)
/login, /logout, /error, /static/**

// Authenticated users
/portal/**, /api/assignments, /api/requests

// Managers
/portal/queue, /api/requests/{id}/approve

// Admins only
/admin/**, /api/radios, /api/persons, /api/workgroups
```

#### Deliverables:
- `SecurityConfig.java` - Spring Security configuration
- `TenantFilter.java` - Multi-tenant resolution
- `CustomUserDetails.java` - User details with tenant info
- Login/logout flows working

---

### 📍 Phase 3: React-Admin Foundation (Week 2)

**Goal**: Set up React-Admin framework and basic layout

#### Tasks:
- ✅ Create new `fleetwave-admin` module (or subfolder in portal)
- ✅ Install React-Admin + Material-UI dependencies
- ✅ Create custom REST data provider for FleetWave API
- ✅ Create auth provider (session-based, check `/api/me`)
- ✅ Set up layout with navigation menu
- ✅ Configure routing for all resources
- ✅ Add X-Tenant header to all requests
- ✅ Create dashboard skeleton

#### Directory Structure:
```
fleetwave-admin/
├─ package.json
├─ vite.config.ts
├─ src/
│  ├─ main.tsx
│  ├─ App.tsx
│  ├─ providers/
│  │  ├─ dataProvider.ts        # REST adapter
│  │  └─ authProvider.ts        # Auth adapter
│  ├─ resources/
│  │  ├─ radios/                # Radio CRUD
│  │  ├─ persons/               # Person CRUD
│  │  ├─ workgroups/            # Workgroup CRUD
│  │  ├─ assignments/           # Assignment CRUD
│  │  ├─ requests/              # Request management
│  │  ├─ workorders/            # Work order CRUD
│  │  └─ alerts/                # Alert management
│  └─ components/
│     ├─ Dashboard.tsx          # Main dashboard
│     └─ Layout.tsx             # Custom layout
```

#### Deliverables:
- React-Admin app with empty resources
- Data provider connecting to `/api/*`
- Auth provider using session cookies
- Basic navigation working

---

### 📍 Phase 4: Core Admin CRUD (Week 3)

**Goal**: Implement full CRUD for core entities

#### 4.1 Radios Management
- ✅ List view with filtering (status, model, serial)
- ✅ Create form with validation
- ✅ Edit form
- ✅ Delete with confirmation
- ✅ Status update (AVAILABLE → ASSIGNED → IN_REPAIR → RETIRED)
- ✅ Bulk actions (bulk status update)

#### 4.2 Persons Management
- ✅ List view with search (name, email, phone)
- ✅ Create form with validation
- ✅ Edit form
- ✅ Delete with confirmation
- ✅ Show related assignments
- ✅ Show workgroup memberships

#### 4.3 Workgroups Management
- ✅ List view with search
- ✅ Create form
- ✅ Edit form
- ✅ Delete with confirmation
- ✅ Show members list
- ✅ Quick add/remove members

#### Deliverables:
- 3 fully functional admin resources
- Proper validation and error handling
- Responsive tables and forms
- Search and filtering working

---

### 📍 Phase 5: Advanced Admin Features (Week 4)

**Goal**: Implement complex workflows and relationships

#### 5.1 Assignments Management
- ✅ List view with filters (status, person, workgroup, radio)
- ✅ Create assignment (assign radio to person/workgroup)
- ✅ Return assignment
- ✅ View assignment history
- ✅ Overdue assignments highlighted

#### 5.2 Requests Management
- ✅ Request approval workflow
- ✅ Approve/reject actions
- ✅ Fulfill request (create assignment)
- ✅ Request status tracking

#### 5.3 Work Orders
- ✅ List view with status filter
- ✅ Create work order for radio
- ✅ Update status (OPEN → IN_PROGRESS → DONE)
- ✅ Assign technician
- ✅ Close work order

#### 5.4 Alerts
- ✅ Alert list with severity indicators
- ✅ Acknowledge alert
- ✅ Close alert
- ✅ Alert history

#### Deliverables:
- 4 advanced admin resources
- Workflow actions working
- Status transitions validated
- Relationship management (radio ↔ work order)

---

### 📍 Phase 6: Dashboard & Analytics (Week 4-5)

**Goal**: Build admin dashboard with key metrics

#### Dashboard Widgets:
- 📊 **Fleet Overview**
  - Total radios by status (pie chart)
  - Radios available vs assigned (gauge)

- 📋 **Active Assignments**
  - Total active assignments
  - Overdue assignments (red alert)

- 🚨 **Alerts Summary**
  - Open alerts count
  - Recent alerts timeline

- 📝 **Pending Requests**
  - Requests awaiting approval
  - Recent requests list

- 🔧 **Work Orders**
  - Open work orders
  - In-progress work orders

#### Deliverables:
- Dashboard with 5-6 key metrics
- Charts using Recharts
- Real-time updates (optional: polling or WebSocket)

---

### 📍 Phase 7: Polish End-User Portal (Week 5)

**Goal**: Improve existing React portal with better UX

#### Enhancements:
- ✅ Add proper UI component library (Material-UI or Ant Design)
- ✅ Add React Query for API caching
- ✅ Improve forms with validation
- ✅ Add loading states and error handling
- ✅ Add toast notifications for actions
- ✅ Make responsive for mobile
- ✅ Add profile page
- ✅ Show assignment history

#### Deliverables:
- Polished end-user portal
- Consistent styling with admin
- Better error handling
- Mobile-friendly layout

---

## 🔐 Authentication Flow

### Login Flow:
```
1. User visits /login (Thymeleaf page)
2. Submits credentials
3. Spring Security validates
4. Creates session with tenant info
5. Redirects based on role:
   - ROLE_ADMIN → /admin
   - ROLE_MANAGER → /portal/queue
   - ROLE_USER → /portal
```

### Session Management:
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) {
  return http
    .authorizeHttpRequests(auth -> auth
      .requestMatchers("/login", "/logout", "/static/**").permitAll()
      .requestMatchers("/admin/**").hasRole("ADMIN")
      .requestMatchers("/portal/queue/**").hasAnyRole("ADMIN", "MANAGER")
      .requestMatchers("/portal/**").authenticated()
      .requestMatchers("/api/**").authenticated()
    )
    .formLogin(form -> form
      .loginPage("/login")
      .defaultSuccessUrl("/portal", true)
    )
    .logout(logout -> logout
      .logoutUrl("/logout")
      .logoutSuccessUrl("/login?logout")
    )
    .build();
}
```

---

## 📁 Project Structure (Final State)

```
fleetwave-v2/
├─ fleetwave-domain/          # Domain entities & repos (unchanged)
├─ fleetwave-app/             # Services & business logic (unchanged)
├─ fleetwave-infra/           # Infrastructure (SES, Twilio) (unchanged)
│
├─ fleetwave-web/             # Spring Boot backend
│  ├─ src/main/
│  │  ├─ java/org/fleetwave/web/
│  │  │  ├─ api/              # REST controllers (unchanged)
│  │  │  ├─ web/              # NEW: Thymeleaf controllers
│  │  │  │  ├─ WebController.java
│  │  │  │  └─ SecurityConfig.java
│  │  │  └─ WebApplication.java
│  │  └─ resources/
│  │     ├─ templates/        # NEW: Thymeleaf templates
│  │     │  ├─ layout.html
│  │     │  ├─ index.html
│  │     │  ├─ login.html
│  │     │  └─ status.html
│  │     ├─ static/           # NEW: Static assets
│  │     │  ├─ css/
│  │     │  ├─ js/
│  │     │  └─ images/
│  │     └─ application.yml
│
├─ fleetwave-portal/          # React end-user portal (existing)
│  ├─ src/
│  │  └─ ui/
│  │     ├─ pages/
│  │     │  ├─ MyAssignments.tsx
│  │     │  ├─ SubmitRequest.tsx
│  │     │  └─ ManagerQueue.tsx
│  │     └─ App.tsx
│  └─ package.json
│
└─ fleetwave-admin/           # NEW: React-Admin dashboard
   ├─ src/
   │  ├─ resources/
   │  │  ├─ radios/
   │  │  ├─ persons/
   │  │  ├─ workgroups/
   │  │  ├─ assignments/
   │  │  ├─ requests/
   │  │  ├─ workorders/
   │  │  └─ alerts/
   │  ├─ providers/
   │  │  ├─ dataProvider.ts
   │  │  └─ authProvider.ts
   │  ├─ Dashboard.tsx
   │  └─ App.tsx
   └─ package.json
```

---

## 🚀 Deployment Strategy

### Development:
```bash
# Terminal 1: Backend
cd fleetwave-web
mvn spring-boot:run

# Terminal 2: End-user portal
cd fleetwave-portal
npm run dev

# Terminal 3: Admin dashboard
cd fleetwave-admin
npm run dev
```

### Production:
```bash
# Build React apps
cd fleetwave-portal && npm run build
cd fleetwave-admin && npm run build

# Copy builds to Spring Boot static resources
cp -r fleetwave-portal/dist/* fleetwave-web/src/main/resources/static/portal/
cp -r fleetwave-admin/dist/* fleetwave-web/src/main/resources/static/admin/

# Build Spring Boot with embedded frontends
cd fleetwave-web
mvn clean package

# Deploy single JAR
java -jar fleetwave-web/target/fleetwave-web-*.jar
```

All frontends served from single Spring Boot app on port 8080.

---

## 📊 Success Metrics

### Phase 1-2 Complete:
- ✅ Users can log in via Thymeleaf
- ✅ Landing page accessible
- ✅ Spring Security configured
- ✅ Sessions working

### Phase 3-4 Complete:
- ✅ Admin can manage Radios, Persons, Workgroups
- ✅ Full CRUD operations working
- ✅ Validation and error handling in place

### Phase 5 Complete:
- ✅ Assignments workflow functional
- ✅ Request approval working
- ✅ Work orders manageable
- ✅ Alerts trackable

### Phase 6-7 Complete:
- ✅ Dashboard showing key metrics
- ✅ Charts and visualizations working
- ✅ End-user portal polished
- ✅ Mobile-responsive

---

## 🎯 Next Steps

Ready to begin? Here's the recommended starting order:

1. **Immediate**: Add Thymeleaf dependencies and create basic login page
2. **Week 1**: Complete Thymeleaf foundation + Spring Security
3. **Week 2**: Set up React-Admin structure
4. **Week 3-4**: Build out admin resources
5. **Week 5**: Polish and deploy

Want me to start with Phase 1 (Thymeleaf setup)?
