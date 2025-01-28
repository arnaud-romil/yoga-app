describe('Register Spec', () => {

    function fillRegisterForm(): void {
        cy.get('input[formControlName=firstName]').type('Myfirstname');
        cy.get('input[formControlName=lastName]').type('Mylastname');
        cy.get('input[formControlName=email]').type('user@test.com');
        cy.get('input[formControlName=password]').type('password');
    }

    function submitRegisterForm(): void {
        cy.get('button[type=submit]').click();
    }

    beforeEach(() => {
        cy.visit('/register');
    })

    it('should display login page on successful registration', () => {
        cy.intercept('POST', '/api/auth/register', {
            body: {
                message: 'User registered successfully!'
            },
        });

        fillRegisterForm();
        submitRegisterForm();

        cy.url().should('include', 'login');
    })

    it('should display error message on unsuccessful registration', () => {
        cy.intercept('POST', '/api/auth/register', {
            statusCode: 400
        });

        fillRegisterForm();
        submitRegisterForm();

        cy.get('span.error').should('have.text', 'An error occurred');
    })



})