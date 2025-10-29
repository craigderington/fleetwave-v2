import {
  Edit,
  SimpleForm,
  TextInput,
  SelectInput,
  NumberInput,
  DateTimeInput,
  required,
} from 'react-admin';

export const AlertEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="id" disabled />

      <TextInput source="subjectType" label="Subject Type" />
      <TextInput source="subjectId" label="Subject ID" />

      <SelectInput
        source="status"
        label="Status"
        choices={[
          { id: 'OPEN', name: 'Open' },
          { id: 'ACK', name: 'Acknowledged' },
          { id: 'CLOSED', name: 'Closed' },
        ]}
        validate={required()}
      />

      <NumberInput source="count" label="Count" />
      <DateTimeInput source="firstSeen" label="First Seen" />
      <DateTimeInput source="lastSeen" label="Last Seen" />
    </SimpleForm>
  </Edit>
);
