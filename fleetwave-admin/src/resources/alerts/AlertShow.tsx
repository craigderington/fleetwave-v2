import {
  Show,
  SimpleShowLayout,
  TextField,
  DateField,
  ChipField,
  NumberField,
} from 'react-admin';

export const AlertShow = () => (
  <Show>
    <SimpleShowLayout>
      <TextField source="id" label="ID" />
      <TextField source="subjectType" label="Subject Type" />
      <TextField source="subjectId" label="Subject ID" />
      <ChipField source="status" label="Status" />
      <NumberField source="count" label="Count" />
      <DateField source="firstSeen" label="First Seen" showTime />
      <DateField source="lastSeen" label="Last Seen" showTime />
    </SimpleShowLayout>
  </Show>
);
