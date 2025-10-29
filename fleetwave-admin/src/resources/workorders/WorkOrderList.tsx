import {
  List,
  Datagrid,
  TextField,
  DateField,
  ChipField,
  ReferenceField,
  EditButton,
  ShowButton,
  DeleteButton,
  FilterButton,
  TopToolbar,
  CreateButton,
  ExportButton,
  SelectInput,
  ReferenceInput,
  TextInput,
} from 'react-admin';

const workOrderFilters = [
  <SelectInput
    key="status"
    label="Status"
    source="status"
    choices={[
      { id: 'OPEN', name: 'Open' },
      { id: 'IN_PROGRESS', name: 'In Progress' },
      { id: 'DONE', name: 'Done' },
      { id: 'CANCELLED', name: 'Cancelled' },
    ]}
    alwaysOn
  />,
  <ReferenceInput key="radio" label="Radio" source="radioId" reference="radios">
    <SelectInput optionText="serialNum" />
  </ReferenceInput>,
  <TextInput key="createdBy" label="Created By" source="createdBy" />,
];

const ListActions = () => (
  <TopToolbar>
    <FilterButton />
    <CreateButton />
    <ExportButton />
  </TopToolbar>
);

export const WorkOrderList = () => (
  <List filters={workOrderFilters} actions={<ListActions />}>
    <Datagrid>
      <TextField source="id" label="ID" />
      <TextField source="title" label="Title" />
      <ReferenceField source="radioId" reference="radios" label="Radio">
        <TextField source="serialNum" />
      </ReferenceField>
      <ChipField source="status" label="Status" />
      <TextField source="createdBy" label="Created By" />
      <DateField source="createdAt" label="Created" showTime />
      <DateField source="dueAt" label="Due" showTime />
      <DateField source="closedAt" label="Closed" showTime />
      <EditButton />
      <ShowButton />
      <DeleteButton />
    </Datagrid>
  </List>
);
