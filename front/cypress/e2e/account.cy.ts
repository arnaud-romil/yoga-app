/// <reference types="cypress" />

describe('Account Spec', () => {

    const isAdmin = true;
    const sessions: any = [];

    it('should display user information', () => {
        cy.login(isAdmin, sessions);

        cy.intercept(
            {
                method: 'GET',
                url: '/api/user/1',
            },
            {
                id: 1,
                email: "yoga@studio.com",
                lastName: "Admin",
                firstName: "Admin",
                admin: true,
                createdAt: "2025-01-20T09:36:43",
                updatedAt: "2025-01-20T09:36:43"
            });

        cy.get('span[routerlink=me]').click();
        cy.url().should('include', '/me');
        cy.get('h1').should('have.text', 'User information');
        cy.contains('p', 'Name:').should('have.text', 'Name: Admin ADMIN');
        cy.contains('p', 'Email:').should('have.text', 'Email: yoga@studio.com');
        cy.contains('p', 'You are admin').should('exist');
    })

    it('should go back to sessions from user information', () => {
        cy.login(isAdmin, sessions);

        cy.intercept(
            {
                method: 'GET',
                url: '/api/user/1',
            },
            {
                id: 1,
                email: "yoga@studio.com",
                lastName: "Admin",
                firstName: "Admin",
                admin: true,
                createdAt: "2025-01-20T09:36:43",
                updatedAt: "2025-01-20T09:36:43"
            });

        cy.get('span[routerlink=me]').click();

        cy.url().should('include', '/me');

        cy.get('button[mat-icon-button]').click();

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            []
        ).as('sessions');

        cy.url().should('include', '/sessions');

    })

    it('it should delete user account', () => {
        cy.login(!isAdmin, sessions);

        cy.intercept(
            {
                method: 'GET',
                url: '/api/user/1',
            },
            {
                id: 1,
                email: "user@studio.com",
                lastName: "user",
                firstName: "user",
                admin: false,
                createdAt: "2025-01-20T09:36:43",
                updatedAt: "2025-01-20T09:36:43"
            }
        );

        cy.intercept(
            {
                method: 'DELETE',
                url: '/api/user/1',
            },
            {}
        );

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            []
        ).as('sessions');

        cy.get('span[routerlink=me]').click();

        cy.url().should('include', '/me');

        cy.get('button[mat-raised-button]').click();

        cy.url().should('include', '/');
    })

})