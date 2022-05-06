import custos.clients.utils.utilities as utl
from custos.transport.settings import CustosServerClientSettings

'''
* CUSTOS base class
* instantiate the server details and token
'''


class custos:
    def __init__(self):
        # read settings
        self.settings = CustosServerClientSettings(
            custos_host='js-156-79.jetstream-cloud.org',
            custos_port='30367',
            custos_client_id="custos-ylsk4jsxqrds6k3ejpig-10000001",
            custos_client_sec="9A3UepbakbEI6YTgxL3bgu9zjhA5h2NoDo6SLAyp")

        # obtain base 64 encoded token for tenant
        self.b64_encoded_custos_token = utl.get_token(
            custos_settings=self.settings)
        print(self.b64_encoded_custos_token)
