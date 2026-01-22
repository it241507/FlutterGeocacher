describe('template spec', () => {
  it('passes', () => {
    cy.visit('http://localhost:3000/auth/login')

    cy.get('#email-field').type('test@gmail.com');

    cy.get('#password-field').type('testpw');

    cy.contains('button', 'LOGIN').click();

  })
})