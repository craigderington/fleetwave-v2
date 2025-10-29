import {
  Show,
  SimpleShowLayout,
  TextField,
  DateField,
  ChipField,
  ReferenceField,
} from 'react-admin';

export const AssignmentShow = () => (
  <Show>
    <SimpleShowLayout>
      <TextField source="id" label="ID" />

      <ReferenceField source="radioId" reference="radios" label="Radio" link="show">
        <TextField source="serialNum" />
      </ReferenceField>

      <ReferenceField source="assigneePersonId" reference="persons" label="Assignee Person" link="show">
        <TextField source="firstName" />
      </ReferenceField>

      <ReferenceField source="assigneeWorkgroupId" reference="workgroups" label="Assignee Workgroup" link="show">
        <TextField source="name" />
      </ReferenceField>

      <ChipField source="status" label="Status" />

      <DateField source="startAt" label="Start At" showTime />
      <DateField source="expectedEnd" label="Expected End" showTime />
      <DateField source="endAt" label="Actual End" showTime />

      <DateField source="createdAt" label="Created At" showTime />
      <DateField source="updatedAt" label="Updated At" showTime />
    </SimpleShowLayout>
  </Show>
);
