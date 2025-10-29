import { DataProvider, fetchUtils } from 'react-admin';

const apiUrl = '/api';
const httpClient = fetchUtils.fetchJson;

/**
 * Custom data provider for FleetWave REST API.
 * Maps React-Admin data provider methods to FleetWave API endpoints.
 */
export const dataProvider: DataProvider = {
  /**
   * Get a list of resources with pagination, sorting, and filtering
   */
  getList: async (resource, params) => {
    const { page, perPage } = params.pagination;
    const { field, order } = params.sort;
    const query = {
      page: page - 1, // Spring uses 0-based pagination
      size: perPage,
      sort: `${field},${order.toLowerCase()}`,
      ...params.filter,
    };

    const url = `${apiUrl}/${resource}?${fetchUtils.queryParameters(query)}`;
    const { json } = await httpClient(url, {
      headers: new Headers({
        'X-Tenant': 'ocps', // TODO: Get from auth context
      }),
    });

    // Handle Spring Boot Page response
    return {
      data: json.content || json,
      total: json.totalElements || json.length,
    };
  },

  /**
   * Get a single resource by ID
   */
  getOne: async (resource, params) => {
    const url = `${apiUrl}/${resource}/${params.id}`;
    const { json } = await httpClient(url, {
      headers: new Headers({
        'X-Tenant': 'ocps',
      }),
    });

    return { data: json };
  },

  /**
   * Get multiple resources by IDs
   */
  getMany: async (resource, params) => {
    const query = {
      ids: params.ids.join(','),
    };
    const url = `${apiUrl}/${resource}?${fetchUtils.queryParameters(query)}`;
    const { json } = await httpClient(url, {
      headers: new Headers({
        'X-Tenant': 'ocps',
      }),
    });

    return { data: json };
  },

  /**
   * Get resources referenced by another resource
   */
  getManyReference: async (resource, params) => {
    const { page, perPage } = params.pagination;
    const { field, order } = params.sort;
    const query = {
      page: page - 1,
      size: perPage,
      sort: `${field},${order.toLowerCase()}`,
      [params.target]: params.id,
      ...params.filter,
    };

    const url = `${apiUrl}/${resource}?${fetchUtils.queryParameters(query)}`;
    const { json } = await httpClient(url, {
      headers: new Headers({
        'X-Tenant': 'ocps',
      }),
    });

    return {
      data: json.content || json,
      total: json.totalElements || json.length,
    };
  },

  /**
   * Create a new resource
   */
  create: async (resource, params) => {
    const { json } = await httpClient(`${apiUrl}/${resource}`, {
      method: 'POST',
      body: JSON.stringify(params.data),
      headers: new Headers({
        'Content-Type': 'application/json',
        'X-Tenant': 'ocps',
      }),
    });

    return { data: json };
  },

  /**
   * Update an existing resource
   */
  update: async (resource, params) => {
    const url = `${apiUrl}/${resource}/${params.id}`;
    const { json } = await httpClient(url, {
      method: 'PUT',
      body: JSON.stringify(params.data),
      headers: new Headers({
        'Content-Type': 'application/json',
        'X-Tenant': 'ocps',
      }),
    });

    return { data: json };
  },

  /**
   * Update multiple resources
   */
  updateMany: async (resource, params) => {
    const promises = params.ids.map((id) =>
      httpClient(`${apiUrl}/${resource}/${id}`, {
        method: 'PUT',
        body: JSON.stringify(params.data),
        headers: new Headers({
          'Content-Type': 'application/json',
          'X-Tenant': 'ocps',
        }),
      })
    );

    await Promise.all(promises);
    return { data: params.ids };
  },

  /**
   * Delete a resource
   */
  delete: async (resource, params) => {
    await httpClient(`${apiUrl}/${resource}/${params.id}`, {
      method: 'DELETE',
      headers: new Headers({
        'X-Tenant': 'ocps',
      }),
    });

    return { data: params.previousData as any };
  },

  /**
   * Delete multiple resources
   */
  deleteMany: async (resource, params) => {
    const promises = params.ids.map((id) =>
      httpClient(`${apiUrl}/${resource}/${id}`, {
        method: 'DELETE',
        headers: new Headers({
          'X-Tenant': 'ocps',
        }),
      })
    );

    await Promise.all(promises);
    return { data: params.ids };
  },
};
