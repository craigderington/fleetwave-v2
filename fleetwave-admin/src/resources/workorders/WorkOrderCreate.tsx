import {
  Create,
  SimpleForm,
  TextInput,
  ReferenceInput,
  SelectInput,
  DateTimeInput,
  required,
} from 'react-admin';

export const WorkOrderCreate = () => (
  <Create>
    <SimpleForm>
      <TextInput source="title" label="Title" validate={required()} />

      <ReferenceInput source="radioId" reference="radios" label="Radio">
        <SelectInput optionText="serialNum" validate={required()} />
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
        defaultValue="OPEN"
        validate={required()}
      />

      <TextInput source="createdBy" label="Created By" />
      <DateTimeInput source="dueAt" label="Due At" />
    </SimpleForm>
  </Create>
);
