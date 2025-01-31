/// <reference types="cypress" />
describe('Login spec', () => {

  function fillLoginForm(): void {
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
  }

  function submitLoginForm(): void {
    cy.get('button[type=submit]').click();
  }


  beforeEach(() => {
    cy.visit('/login');
  })

  it('should display rentals available on successful login', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('sessions')

    fillLoginForm();
    submitLoginForm();

    cy.url().should('include', '/sessions')
  })

  it('should display error message on unsuccessful login', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401
    })

    fillLoginForm();
    submitLoginForm();

    cy.get('p.error').should('have.text', 'An error occurred');
  })
});