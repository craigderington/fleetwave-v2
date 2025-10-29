import {
  Edit,
  SimpleForm,
  TextInput,
  SelectInput,
  required,
  DateTimeInput,
} from 'react-admin';

export const RadioEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="id" disabled />
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
        validate={required()}
      />
      <TextInput source="notes" label="Notes" multiline rows={3} />
      <DateTimeInput source="createdAt" label="Created At" disabled />
      <DateTimeInput source="updatedAt" label="Updated At" disabled />
    </SimpleForm>
  </Edit>
);
