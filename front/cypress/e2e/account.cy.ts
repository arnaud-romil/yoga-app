describe('Account Spec', () => {

    it('should display user information', () => {
        cy.login();

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
    })

})