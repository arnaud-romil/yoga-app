/// <reference types="cypress" />

describe('Create Session Spec', () => {

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

    const teachers = [
        {
            id: 1,
            lastName: 'DELAHAYE',
            firstName: 'Margot'
        },
        {
            id: 2,
            lastName: 'THIERCELIN',
            firstName: 'Hélène'
        }
    ];

    const isAdmin = true;

    it('should allow admin to create a yoga session', () => {

        cy.login(isAdmin, sessions);

        cy.intercept({
            method: 'GET',
            url: '/api/teacher',
        }, teachers).as('teachers');

        cy.intercept('POST', '/api/session', {
            body: session
        });

        cy.intercept(
            {
                method: 'GET',
                url: '/api/session',
            },
            [session]
        ).as('sessions')

        cy.get('button[routerlink=create]').click();
        cy.url().should('include', '/sessions/create');

        cy.get('input[formControlName=name]').type("New session");
        cy.get('input[formControlName=date]').type("2025-02-01");
        cy.get('mat-select').click();
        cy.get('mat-option').contains('Margot').click();
        cy.get('textarea[formControlName=description]').type("session description");

        cy.get('button[type=submit]').click();

        cy.url().should('include', '/sessions');

        cy.get('.items mat-card-title').should('have.text', session.name);
        cy.get('.items mat-card-content p').should('have.text', ` ${session.description} `);
    });

    it('should not allow user to create a yoga session', () => {

        cy.login(!isAdmin, sessions);

        cy.get('button[routerlink=create]').should('not.exist');
    });

    it('should allow admin to update a yoga session', () => {

        sessions.push(session);

        cy.login(isAdmin, sessions);

        cy.intercept('GET', '/api/teacher', teachers).as('teachers');

        cy.intercept('GET', '/api/session/25', session).as('session');

        cy.contains('span', 'Edit').click();

        cy.url().should('include', '/sessions/update');

        cy.get('textarea[formControlName=description]').clear().type("session description updated!");
        session.description = "session description updated!";

        cy.intercept('PUT', '/api/session/25', session).as('updateSession');

        cy.intercept('GET', '/api/session', [session]).as('sessions');

        cy.get('button[type=submit]').click();

        cy.url().should('include', '/sessions');

        cy.get('.items mat-card-title').should('have.text', session.name);
        cy.get('.items mat-card-content p').should('have.text', ` ${session.description} `);
    });

})