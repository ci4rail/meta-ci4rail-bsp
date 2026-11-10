SUMMARY = "Restricted administrator account moducop"
DESCRIPTION = "Creates the account moducop without a usable password. Login, \
sudo rights and the password are provided by an os-customization, so the \
account is inert in images without such a customization."

RESTRICTED_USER = "moducop"

require recipes-core/moducop-user/restricted-user.inc
