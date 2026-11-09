SUMMARY = "Restricted administrator account ci4rail"
DESCRIPTION = "Creates the account ci4rail without a usable password. Login, \
sudo rights and the password are provided by the factory os-customization, \
so the account is inert in images without such a customization."

RESTRICTED_USER = "ci4rail"

require recipes-core/moducop-user/restricted-user.inc
