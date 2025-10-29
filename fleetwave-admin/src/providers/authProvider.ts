import { AuthProvider } from 'react-admin';

/**
 * Auth provider for session-based authentication with Spring Security.
 * Assumes the user is already authenticated via the /login page.
 */
export const authProvider: AuthProvider = {
  /**
   * Called when the user attempts to log in
   * For session-based auth, we redirect to Spring Security login page
   */
  login: async () => {
    // Redirect to Spring Security login page
    window.location.href = '/login';
    return Promise.resolve();
  },

  /**
   * Called when the user clicks on the logout button
   */
  logout: async () => {
    // Call Spring Security logout endpoint
    await fetch('/logout', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
    });
    window.location.href = '/login?logout';
    return Promise.resolve();
  },

  /**
   * Called when the API returns an error
   */
  checkError: async (error) => {
    const status = error.status;
    if (status === 401 || status === 403) {
      // Unauthorized or forbidden - redirect to login
      window.location.href = '/login';
      return Promise.reject();
    }
    return Promise.resolve();
  },

  /**
   * Called when the user navigates to a new location to check authentication
   */
  checkAuth: async () => {
    // Check if user is authenticated by calling a protected endpoint
    try {
      const response = await fetch('/api/persons', {
        method: 'HEAD',
        headers: {
          'X-Tenant': 'ocps',
        },
      });

      if (response.ok) {
        return Promise.resolve();
      } else {
        return Promise.reject();
      }
    } catch (error) {
      return Promise.reject();
    }
  },

  /**
   * Called when the user navigates to get user permissions
   */
  getPermissions: async () => {
    // TODO: Get permissions from session/user context
    // For now, assume admin has all permissions
    return Promise.resolve('admin');
  },

  /**
   * Get user identity (name, avatar, etc.)
   */
  getIdentity: async () => {
    try {
      // Try to get user info from session attribute in a hidden element
      // or from a dedicated /api/me endpoint (TODO: implement)
      return Promise.resolve({
        id: 'admin',
        fullName: 'Admin User',
      });
    } catch (error) {
      return Promise.reject();
    }
  },
};
