/// <reference types="cypress" />

describe('Session Detail Spec', () => {

    let sessions: any = [];

    const session = {
        id: 25,
        name: 'New session',
        description: 'session description',
        date: '2025-02-01',
        teacher_id: 1,
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
    };

    const isAdmin = true;

    it('should allow user to see a yoga session', () => {

        sessions.push(session);

        cy.login(isAdmin, sessions);

        cy.intercept('GET', '/api/session/25', {
            body: session
        });

        cy.intercept('get', '/api/teacher/1', {
            id: 1,
            lastName: 'DELAHAYE',
            firstName: 'Margot'
        }).as('teacher');

        cy.contains('span', 'Detail').click();

        cy.url().should('include', '/sessions/detail/25');

        cy.get('h1').should('have.text', 'New Session');
        cy.get('span').contains('Margot DELAHAYE').should('exist');

    });
});