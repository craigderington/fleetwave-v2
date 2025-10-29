import {
  Edit,
  SimpleForm,
  TextInput,
  ReferenceInput,
  SelectInput,
  DateTimeInput,
  required,
} from 'react-admin';

export const AssignmentEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="id" disabled />

      <ReferenceInput source="radioId" reference="radios" label="Radio">
        <SelectInput optionText="serialNum" validate={required()} />
      </ReferenceInput>

      <ReferenceInput source="assigneePersonId" reference="persons" label="Assignee Person">
        <SelectInput optionText={(record) => `${record.firstName} ${record.lastName}`} />
      </ReferenceInput>

      <ReferenceInput source="assigneeWorkgroupId" reference="workgroups" label="Assignee Workgroup">
        <SelectInput optionText="name" />
      </ReferenceInput>

      <SelectInput
        source="status"
        label="Status"
        choices={[
          { id: 'REQUESTED', name: 'Requested' },
          { id: 'APPROVED', name: 'Approved' },
          { id: 'ASSIGNED', name: 'Assigned' },
          { id: 'RETURNED', name: 'Returned' },
          { id: 'CANCELLED', name: 'Cancelled' },
        ]}
        validate={required()}
      />

      <DateTimeInput source="startAt" label="Start At" validate={required()} />
      <DateTimeInput source="expectedEnd" label="Expected End" />
      <DateTimeInput source="endAt" label="Actual End" />
    </SimpleForm>
  </Edit>
);
