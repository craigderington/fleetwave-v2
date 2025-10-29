import {
  Edit,
  SimpleForm,
  TextInput,
  ReferenceInput,
  SelectInput,
  DateTimeInput,
  required,
} from 'react-admin';

export const RequestEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="id" disabled />

      <ReferenceInput source="workgroupId" reference="workgroups" label="Workgroup">
        <SelectInput optionText="name" />
      </ReferenceInput>

      <TextInput source="requesterId" label="Requester ID" />
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
        validate={required()}
      />

      <DateTimeInput source="neededUntil" label="Needed Until" />
      <DateTimeInput source="createdAt" label="Created At" disabled />
    </SimpleForm>
  </Edit>
);
