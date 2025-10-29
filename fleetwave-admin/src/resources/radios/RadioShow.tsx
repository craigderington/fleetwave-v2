import {
  Show,
  SimpleShowLayout,
  TextField,
  DateField,
  ChipField,
  RichTextField,
} from 'react-admin';

export const RadioShow = () => (
  <Show>
    <SimpleShowLayout>
      <TextField source="id" label="ID" />
      <TextField source="serialNum" label="Serial Number" />
      <TextField source="model" label="Model" />
      <ChipField source="status" label="Status" />
      <RichTextField source="notes" label="Notes" />
      <DateField source="createdAt" label="Created At" showTime />
      <DateField source="updatedAt" label="Updated At" showTime />
    </SimpleShowLayout>
  </Show>
);
