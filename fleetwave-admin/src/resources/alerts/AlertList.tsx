import {
  List,
  Datagrid,
  TextField,
  DateField,
  ChipField,
  NumberField,
  EditButton,
  ShowButton,
  DeleteButton,
  FilterButton,
  TopToolbar,
  CreateButton,
  ExportButton,
  SelectInput,
  TextInput,
} from 'react-admin';

const alertFilters = [
  <SelectInput
    key="status"
    label="Status"
    source="status"
    choices={[
      { id: 'OPEN', name: 'Open' },
      { id: 'ACK', name: 'Acknowledged' },
      { id: 'CLOSED', name: 'Closed' },
    ]}
    alwaysOn
  />,
  <TextInput key="subjectType" label="Subject Type" source="subjectType" />,
];

const ListActions = () => (
  <TopToolbar>
    <FilterButton />
    <CreateButton />
    <ExportButton />
  </TopToolbar>
);

export const AlertList = () => (
  <List filters={alertFilters} actions={<ListActions />}>
    <Datagrid>
      <TextField source="id" label="ID" />
      <TextField source="subjectType" label="Subject Type" />
      <TextField source="subjectId" label="Subject ID" />
      <ChipField source="status" label="Status" />
      <NumberField source="count" label="Count" />
      <DateField source="firstSeen" label="First Seen" showTime />
      <DateField source="lastSeen" label="Last Seen" showTime />
      <EditButton />
      <ShowButton />
      <DeleteButton />
    </Datagrid>
  </List>
);
