import {
  Edit,
  SimpleForm,
  TextInput,
  ReferenceInput,
  SelectInput,
  DateTimeInput,
  required,
} from 'react-admin';

export const WorkOrderEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="id" disabled />

      <TextInput source="title" label="Title" validate={required()} />

      <ReferenceInput source="radioId" reference="radios" label="Radio">
        <SelectInput optionText="serialNum" />
      </ReferenceInput>

      <TextInput source="description" label="Description" multiline rows={5} />

      <SelectInput
        source="status"
        label="Status"
        choices={[
          { id: 'OPEN', name: 'Open' },
          { id: 'IN_PROGRESS', name: 'In Progress' },
          { id: 'DONE', name: 'Done' },
          { id: 'CANCELLED', name: 'Cancelled' },
        ]}
        validate={required()}
      />

      <TextInput source="createdBy" label="Created By" />
      <DateTimeInput source="createdAt" label="Created At" disabled />
      <DateTimeInput source="dueAt" label="Due At" />
      <DateTimeInput source="closedAt" label="Closed At" />
    </SimpleForm>
  </Edit>
);
