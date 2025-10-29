import {
  List,
  Datagrid,
  TextField,
  DateField,
  ChipField,
  EditButton,
  ShowButton,
  DeleteButton,
  FilterButton,
  TopToolbar,
  CreateButton,
  ExportButton,
  TextInput,
  SelectInput,
} from 'react-admin';

const radioFilters = [
  <TextInput key="serial" label="Serial Number" source="serialNum" alwaysOn />,
  <TextInput key="model" label="Model" source="model" />,
  <SelectInput
    key="status"
    label="Status"
    source="status"
    choices={[
      { id: 'AVAILABLE', name: 'Available' },
      { id: 'ASSIGNED', name: 'Assigned' },
      { id: 'IN_REPAIR', name: 'In Repair' },
      { id: 'RETIRED', name: 'Retired' },
    ]}
  />,
];

const ListActions = () => (
  <TopToolbar>
    <FilterButton />
    <CreateButton />
    <ExportButton />
  </TopToolbar>
);

export const RadioList = () => (
  <List filters={radioFilters} actions={<ListActions />}>
    <Datagrid>
      <TextField source="id" label="ID" />
      <TextField source="serialNum" label="Serial Number" />
      <TextField source="model" label="Model" />
      <ChipField source="status" label="Status" />
      <TextField source="notes" label="Notes" />
      <DateField source="createdAt" label="Created" showTime />
      <DateField source="updatedAt" label="Updated" showTime />
      <EditButton />
      <ShowButton />
      <DeleteButton />
    </Datagrid>
  </List>
);
