import {
  Create,
  SimpleForm,
  TextInput,
  SelectInput,
  NumberInput,
  required,
} from 'react-admin';

export const AlertCreate = () => (
  <Create>
    <SimpleForm>
      <TextInput source="subjectType" label="Subject Type" validate={required()} />
      <TextInput source="subjectId" label="Subject ID" validate={required()} />

      <SelectInput
        source="status"
        label="Status"
        choices={[
          { id: 'OPEN', name: 'Open' },
          { id: 'ACK', name: 'Acknowledged' },
          { id: 'CLOSED', name: 'Closed' },
        ]}
        defaultValue="OPEN"
        validate={required()}
      />

      <NumberInput source="count" label="Count" defaultValue={1} />
    </SimpleForm>
  </Create>
);
