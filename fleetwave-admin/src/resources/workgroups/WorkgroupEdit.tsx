import {
  Edit,
  SimpleForm,
  TextInput,
  required,
} from 'react-admin';

export const WorkgroupEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="id" disabled />
      <TextInput source="name" label="Name" validate={required()} />
    </SimpleForm>
  </Edit>
);
