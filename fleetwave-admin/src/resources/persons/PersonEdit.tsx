import {
  Edit,
  SimpleForm,
  TextInput,
  BooleanInput,
  required,
  email,
} from 'react-admin';

export const PersonEdit = () => (
  <Edit>
    <SimpleForm>
      <TextInput source="id" disabled />
      <TextInput source="firstName" label="First Name" validate={required()} />
      <TextInput source="lastName" label="Last Name" validate={required()} />
      <TextInput source="email" label="Email" validate={[required(), email()]} />
      <TextInput source="username" label="Username" validate={required()} />
      <TextInput
        source="roles"
        label="Roles"
        helperText="Comma-separated roles, e.g., ROLE_USER,ROLE_ADMIN"
      />
      <BooleanInput source="enabled" label="Account Enabled" />
    </SimpleForm>
  </Edit>
);
