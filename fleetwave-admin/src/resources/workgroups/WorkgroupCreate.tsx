import {
  Create,
  SimpleForm,
  TextInput,
  required,
} from 'react-admin';

export const WorkgroupCreate = () => (
  <Create>
    <SimpleForm>
      <TextInput source="name" label="Name" validate={required()} />
    </SimpleForm>
  </Create>
);
