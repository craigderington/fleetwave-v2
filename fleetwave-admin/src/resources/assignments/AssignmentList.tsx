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

const assignmentFilters = [
  <SelectInput
    key="status"
    label="Status"
    source="status"
    choices={[
      { id: 'REQUESTED', name: 'Requested' },
      { id: 'APPROVED', name: 'Approved' },
      { id: 'ASSIGNED', name: 'Assigned' },
      { id: 'RETURNED', name: 'Returned' },
      { id: 'CANCELLED', name: 'Cancelled' },
    ]}
    alwaysOn
  />,
  <ReferenceInput key="radio" label="Radio" source="radioId" reference="radios">
    <SelectInput optionText="serialNum" />
  </ReferenceInput>,
  <ReferenceInput key="person" label="Person" source="assigneePersonId" reference="persons">
    <SelectInput optionText={(record) => `${record.firstName} ${record.lastName}`} />
  </ReferenceInput>,
];

const ListActions = () => (
  <TopToolbar>
    <FilterButton />
    <CreateButton />
    <ExportButton />
  </TopToolbar>
);

export const AssignmentList = () => (
  <List filters={assignmentFilters} actions={<ListActions />}>
    <Datagrid>
      <TextField source="id" label="ID" />
      <ReferenceField source="radioId" reference="radios" label="Radio">
        <TextField source="serialNum" />
      </ReferenceField>
      <ReferenceField source="assigneePersonId" reference="persons" label="Assignee Person">
        <TextField source="firstName" />
      </ReferenceField>
      <ReferenceField source="assigneeWorkgroupId" reference="workgroups" label="Assignee Workgroup">
        <TextField source="name" />
      </ReferenceField>
      <ChipField source="status" label="Status" />
      <DateField source="startAt" label="Start" showTime />
      <DateField source="expectedEnd" label="Expected End" showTime />
      <DateField source="endAt" label="Actual End" showTime />
      <EditButton />
      <ShowButton />
      <DeleteButton />
    </Datagrid>
  </List>
);
