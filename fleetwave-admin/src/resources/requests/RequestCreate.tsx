import {
  Create,
  SimpleForm,
  TextInput,
  ReferenceInput,
  SelectInput,
  DateTimeInput,
  required,
} from 'react-admin';

export const RequestCreate = () => (
  <Create>
    <SimpleForm>
      <ReferenceInput source="workgroupId" reference="workgroups" label="Workgroup">
        <SelectInput optionText="name" />
      </ReferenceInput>

      <TextInput source="requesterId" label="Requester ID" validate={required()} />
      <TextInput source="radioModelPref" label="Radio Model Preference" />
      <TextInput source="reason" label="Reason" multiline rows={3} />

      <SelectInput
        source="status"
        label="Status"
        choices={[
          { id: 'PENDING', name: 'Pending' },
          { id: 'APPROVED', name: 'Approved' },
          { id: 'REJECTED', name: 'Rejected' },
          { id: 'FULFILLED', name: 'Fulfilled' },
          { id: 'CANCELLED', name: 'Cancelled' },
        ]}
        defaultValue="PENDING"
        validate={required()}
      />

      <DateTimeInput source="neededUntil" label="Needed Until" />
    </SimpleForm>
  </Create>
);
