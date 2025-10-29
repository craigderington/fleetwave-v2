import {
  List,
  Datagrid,
  TextField,
  EmailField,
  DateField,
  EditButton,
  ShowButton,
  DeleteButton,
  FilterButton,
  TopToolbar,
  CreateButton,
  ExportButton,
  TextInput,
  BooleanField,
} from 'react-admin';

const personFilters = [
  <TextInput key="firstName" label="First Name" source="firstName" alwaysOn />,
  <TextInput key="lastName" label="Last Name" source="lastName" alwaysOn />,
  <TextInput key="email" label="Email" source="email" />,
  <TextInput key="username" label="Username" source="username" />,
];

const ListActions = () => (
  <TopToolbar>
    <FilterButton />
    <CreateButton />
    <ExportButton />
  </TopToolbar>
);

export const PersonList = () => (
  <List filters={personFilters} actions={<ListActions />}>
    <Datagrid>
      <TextField source="id" label="ID" />
      <TextField source="firstName" label="First Name" />
      <TextField source="lastName" label="Last Name" />
      <EmailField source="email" label="Email" />
      <TextField source="username" label="Username" />
      <TextField source="roles" label="Roles" />
      <BooleanField source="enabled" label="Enabled" />
      <DateField source="createdAt" label="Created" showTime />
      <EditButton />
      <ShowButton />
      <DeleteButton />
    </Datagrid>
  </List>
);
