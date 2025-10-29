import { Admin, Resource } from 'react-admin';
import { dataProvider } from './providers/dataProvider';
import { authProvider } from './providers/authProvider';
import { Dashboard } from './components/Dashboard';

// Radio resources
import { RadioList, RadioEdit, RadioCreate, RadioShow } from './resources/radios';

// Person resources
import { PersonList, PersonEdit, PersonCreate, PersonShow } from './resources/persons';

// Workgroup resources
import { WorkgroupList, WorkgroupEdit, WorkgroupCreate, WorkgroupShow } from './resources/workgroups';

// Assignment resources
import { AssignmentList, AssignmentEdit, AssignmentCreate, AssignmentShow } from './resources/assignments';

// Request resources
import { RequestList, RequestEdit, RequestCreate, RequestShow } from './resources/requests';

// WorkOrder resources
import { WorkOrderList, WorkOrderEdit, WorkOrderCreate, WorkOrderShow } from './resources/workorders';

// Alert resources
import { AlertList, AlertEdit, AlertCreate, AlertShow } from './resources/alerts';

// Material-UI Icons
import RadioIcon from '@mui/icons-material/Radio';
import PeopleIcon from '@mui/icons-material/People';
import GroupsIcon from '@mui/icons-material/Groups';
import AssignmentIcon from '@mui/icons-material/Assignment';
import RequestPageIcon from '@mui/icons-material/RequestPage';
import BuildIcon from '@mui/icons-material/Build';
import NotificationImportantIcon from '@mui/icons-material/NotificationImportant';

/**
 * Main FleetWave Admin Application
 * Uses React-Admin framework with Material-UI
 */
function App() {
  return (
    <Admin
      dataProvider={dataProvider}
      authProvider={authProvider}
      dashboard={Dashboard}
      title="FleetWave Admin"
    >
      {/* Radio Inventory Management */}
      <Resource
        name="radios"
        list={RadioList}
        edit={RadioEdit}
        create={RadioCreate}
        show={RadioShow}
        icon={RadioIcon}
        options={{ label: 'Radios' }}
      />

      {/* Personnel Management */}
      <Resource
        name="persons"
        list={PersonList}
        edit={PersonEdit}
        create={PersonCreate}
        show={PersonShow}
        icon={PeopleIcon}
        options={{ label: 'Persons' }}
      />

      {/* Workgroup Management */}
      <Resource
        name="workgroups"
        list={WorkgroupList}
        edit={WorkgroupEdit}
        create={WorkgroupCreate}
        show={WorkgroupShow}
        icon={GroupsIcon}
        options={{ label: 'Workgroups' }}
      />

      {/* Assignment Tracking */}
      <Resource
        name="assignments"
        list={AssignmentList}
        edit={AssignmentEdit}
        create={AssignmentCreate}
        show={AssignmentShow}
        icon={AssignmentIcon}
        options={{ label: 'Assignments' }}
      />

      {/* Request Management */}
      <Resource
        name="requests"
        list={RequestList}
        edit={RequestEdit}
        create={RequestCreate}
        show={RequestShow}
        icon={RequestPageIcon}
        options={{ label: 'Requests' }}
      />

      {/* Work Order Management */}
      <Resource
        name="workorders"
        list={WorkOrderList}
        edit={WorkOrderEdit}
        create={WorkOrderCreate}
        show={WorkOrderShow}
        icon={BuildIcon}
        options={{ label: 'Work Orders' }}
      />

      {/* Alert Management */}
      <Resource
        name="alerts"
        list={AlertList}
        edit={AlertEdit}
        create={AlertCreate}
        show={AlertShow}
        icon={NotificationImportantIcon}
        options={{ label: 'Alerts' }}
      />
    </Admin>
  );
}

export default App;
