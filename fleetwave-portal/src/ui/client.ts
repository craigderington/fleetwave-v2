export type Assignment = { id: string, radioId: string, expectedEnd?: string, status: 'ACTIVE'|'RETURNED'|'CANCELLED' }
export type Request = { id: string, status: string, requesterId: string, workgroupId: string, radioModelPref?: string }
