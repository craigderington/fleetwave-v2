import { Admin, Resource, ListGuesser, EditGuesser, ShowGuesser } from 'react-admin';
import { dataProvider } from './providers/dataProvider';
import { authProvider } from './providers/authProvider';
import { Dashboard } from './components/Dashboard';

// Radio resources
import { RadioList, RadioEdit, RadioCreate, RadioShow } from './resources/radios';

// Person resources
import { PersonList, PersonEdit, PersonCreate, PersonShow } from './resources/persons';

// Workgroup resources
import { WorkgroupList, WorkgroupEdit, WorkgroupCreate, WorkgroupShow } from './resources/workgroups';

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
        list={ListGuesser}
        edit={EditGuesser}
        show={ShowGuesser}
        icon={AssignmentIcon}
        options={{ label: 'Assignments' }}
      />

      {/* Request Management */}
      <Resource
        name="requests"
        list={ListGuesser}
        edit={EditGuesser}
        show={ShowGuesser}
        icon={RequestPageIcon}
        options={{ label: 'Requests' }}
      />

      {/* Work Order Management */}
      <Resource
        name="workorders"
        list={ListGuesser}
        edit={EditGuesser}
        show={ShowGuesser}
        icon={BuildIcon}
        options={{ label: 'Work Orders' }}
      />

      {/* Alert Management */}
      <Resource
        name="alerts"
        list={ListGuesser}
        edit={EditGuesser}
        show={ShowGuesser}
        icon={NotificationImportantIcon}
        options={{ label: 'Alerts' }}
      />
    </Admin>
  );
}

export default App;
