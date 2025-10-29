import {
  Create,
  SimpleForm,
  TextInput,
  BooleanInput,
  PasswordInput,
  required,
  email,
} from 'react-admin';

export const PersonCreate = () => (
  <Create>
    <SimpleForm>
      <TextInput source="firstName" label="First Name" validate={required()} />
      <TextInput source="lastName" label="Last Name" validate={required()} />
      <TextInput source="email" label="Email" validate={[required(), email()]} />
      <TextInput source="username" label="Username" validate={required()} />
      <PasswordInput
        source="password"
        label="Password"
        validate={required()}
        helperText="Password will be hashed by the server"
      />
      <TextInput
        source="roles"
        label="Roles"
        defaultValue="ROLE_USER"
        helperText="Comma-separated roles, e.g., ROLE_USER,ROLE_ADMIN"
      />
      <BooleanInput source="enabled" label="Account Enabled" defaultValue={true} />
    </SimpleForm>
  </Create>
);
