import {
  Create,
  SimpleForm,
  ReferenceInput,
  SelectInput,
  DateTimeInput,
  required,
} from 'react-admin';

export const AssignmentCreate = () => (
  <Create>
    <SimpleForm>
      <ReferenceInput source="radioId" reference="radios" label="Radio">
        <SelectInput optionText="serialNum" validate={required()} />
      </ReferenceInput>

      <ReferenceInput source="assigneePersonId" reference="persons" label="Assignee Person">
        <SelectInput
          optionText={(record) => `${record.firstName} ${record.lastName}`}
          helperText="Assign to a person OR a workgroup (not both)"
        />
      </ReferenceInput>

      <ReferenceInput source="assigneeWorkgroupId" reference="workgroups" label="Assignee Workgroup">
        <SelectInput
          optionText="name"
          helperText="Assign to a person OR a workgroup (not both)"
        />
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
        defaultValue="ASSIGNED"
        validate={required()}
      />

      <DateTimeInput
        source="startAt"
        label="Start At"
        defaultValue={new Date().toISOString()}
        validate={required()}
      />
      <DateTimeInput source="expectedEnd" label="Expected End" />
    </SimpleForm>
  </Create>
);
