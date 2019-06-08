export enum Authorities {
    // first two values are reflected by backend
    TRAINER = 'TRAINER',
    ADMIN = 'ADMIN',
    // simply autentication needed, no matter which specific role, mixture of former two
    AUTHENTICATED = 'AUTHENTICATED',
    UNAUTHENTICATED = 'UNAUTHENTICATED',
    NONE = 'NONE',
}
