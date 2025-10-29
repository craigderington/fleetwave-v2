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
} from 'react-admin';

const requestFilters = [
  <SelectInput
    key="status"
    label="Status"
    source="status"
    choices={[
      { id: 'PENDING', name: 'Pending' },
      { id: 'APPROVED', name: 'Approved' },
      { id: 'REJECTED', name: 'Rejected' },
      { id: 'FULFILLED', name: 'Fulfilled' },
      { id: 'CANCELLED', name: 'Cancelled' },
    ]}
    alwaysOn
  />,
  <ReferenceInput key="workgroup" label="Workgroup" source="workgroupId" reference="workgroups">
    <SelectInput optionText="name" />
  </ReferenceInput>,
];

const ListActions = () => (
  <TopToolbar>
    <FilterButton />
    <CreateButton />
    <ExportButton />
  </TopToolbar>
);

export const RequestList = () => (
  <List filters={requestFilters} actions={<ListActions />}>
    <Datagrid>
      <TextField source="id" label="ID" />
      <ReferenceField source="workgroupId" reference="workgroups" label="Workgroup">
        <TextField source="name" />
      </ReferenceField>
      <TextField source="requesterId" label="Requester ID" />
      <TextField source="radioModelPref" label="Model Preference" />
      <TextField source="reason" label="Reason" />
      <ChipField source="status" label="Status" />
      <DateField source="neededUntil" label="Needed Until" showTime />
      <DateField source="createdAt" label="Created" showTime />
      <EditButton />
      <ShowButton />
      <DeleteButton />
    </Datagrid>
  </List>
);
