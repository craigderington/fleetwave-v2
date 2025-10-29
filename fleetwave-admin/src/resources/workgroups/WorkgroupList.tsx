import {
  List,
  Datagrid,
  TextField,
  DateField,
  EditButton,
  ShowButton,
  DeleteButton,
  FilterButton,
  TopToolbar,
  CreateButton,
  ExportButton,
  TextInput,
} from 'react-admin';

const workgroupFilters = [
  <TextInput key="name" label="Name" source="name" alwaysOn />,
];

const ListActions = () => (
  <TopToolbar>
    <FilterButton />
    <CreateButton />
    <ExportButton />
  </TopToolbar>
);

export const WorkgroupList = () => (
  <List filters={workgroupFilters} actions={<ListActions />}>
    <Datagrid>
      <TextField source="id" label="ID" />
      <TextField source="name" label="Name" />
      <DateField source="createdAt" label="Created" showTime />
      <DateField source="updatedAt" label="Updated" showTime />
      <EditButton />
      <ShowButton />
      <DeleteButton />
    </Datagrid>
  </List>
);
