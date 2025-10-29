import {
  Create,
  SimpleForm,
  TextInput,
  SelectInput,
  required,
} from 'react-admin';

export const RadioCreate = () => (
  <Create>
    <SimpleForm>
      <TextInput source="serialNum" label="Serial Number" validate={required()} />
      <TextInput source="model" label="Model" />
      <SelectInput
        source="status"
        label="Status"
        choices={[
          { id: 'AVAILABLE', name: 'Available' },
          { id: 'ASSIGNED', name: 'Assigned' },
          { id: 'IN_REPAIR', name: 'In Repair' },
          { id: 'RETIRED', name: 'Retired' },
        ]}
        defaultValue="AVAILABLE"
        validate={required()}
      />
      <TextInput source="notes" label="Notes" multiline rows={3} />
    </SimpleForm>
  </Create>
);
