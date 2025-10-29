import {
  Show,
  SimpleShowLayout,
  TextField,
  DateField,
  ChipField,
  ReferenceField,
  RichTextField,
} from 'react-admin';

export const WorkOrderShow = () => (
  <Show>
    <SimpleShowLayout>
      <TextField source="id" label="ID" />
      <TextField source="title" label="Title" />

      <ReferenceField source="radioId" reference="radios" label="Radio" link="show">
        <TextField source="serialNum" />
      </ReferenceField>

      <RichTextField source="description" label="Description" />
      <ChipField source="status" label="Status" />
      <TextField source="createdBy" label="Created By" />

      <DateField source="createdAt" label="Created At" showTime />
      <DateField source="dueAt" label="Due At" showTime />
      <DateField source="closedAt" label="Closed At" showTime />
    </SimpleShowLayout>
  </Show>
);
