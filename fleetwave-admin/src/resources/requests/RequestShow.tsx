import {
  Show,
  SimpleShowLayout,
  TextField,
  DateField,
  ChipField,
  ReferenceField,
  RichTextField,
} from 'react-admin';

export const RequestShow = () => (
  <Show>
    <SimpleShowLayout>
      <TextField source="id" label="ID" />

      <ReferenceField source="workgroupId" reference="workgroups" label="Workgroup" link="show">
        <TextField source="name" />
      </ReferenceField>

      <TextField source="requesterId" label="Requester ID" />
      <TextField source="radioModelPref" label="Radio Model Preference" />
      <RichTextField source="reason" label="Reason" />

      <ChipField source="status" label="Status" />

      <DateField source="neededUntil" label="Needed Until" showTime />
      <DateField source="createdAt" label="Created At" showTime />
    </SimpleShowLayout>
  </Show>
);
